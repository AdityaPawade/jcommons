package com.adtsw.jcommons.logging;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.logging.log4j.LogManager;

import com.adtsw.jcommons.execution.BlockingThreadPoolExecutor;

public abstract class AbstractAsyncLogger<E> implements Logger<E> {

    private static final org.apache.logging.log4j.Logger logger = 
        LogManager.getLogger(AbstractAsyncLogger.class);
    
    protected final int queueSize;
    protected final CircularFifoQueue<E> buffer;
    private final int flushThreshold;
    private final BlockingThreadPoolExecutor executorPool;

    private boolean flushInProgress = false;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public AbstractAsyncLogger(int bufferSize, int flushThresholdPercentage) {

        this.queueSize = bufferSize;
        this.buffer = new CircularFifoQueue<>(bufferSize);
        int flushThresholdPercentageWithinLimits = Math.min(Math.max(flushThresholdPercentage, 10), 90);
        this.flushThreshold = (int) ((bufferSize * flushThresholdPercentageWithinLimits) / 100.0);

        this.executorPool = new BlockingThreadPoolExecutor(
            "log-flush", 1, 1
        );
    }

    @Override
    public void log(E event) {
        
        lock.writeLock().lock();
        buffer.add(event);
        lock.writeLock().unlock();
        
        int currentBufferSize = buffer.size();
        if(currentBufferSize > flushThreshold) {
            flush(false);
        }
    }

    public void flush() {
        flush(false);
    }

    public void flush(boolean async) {
        boolean success = false;
        if(async) {
            success = this.executorPool.executeButRejectIfFull(new Runnable() {
                @Override
                public void run() {
                    doFlush();
                }
            }); 
        } else {
            doFlush();
            success = true;
        }
        if(!success) {
            logger.warn(
                "Unable to trigger log flush. Current buffer size is " + 
                buffer.size()
            );
        }
    }

    private void doFlush() {

        List<E> flushedItems = new ArrayList<>();
        lock.writeLock().lock();
        int currentBufferSize = buffer.size();
        for (int i = 0; i < currentBufferSize; i++) {
            E remove = buffer.remove();
            flushedItems.add(remove);
        }
        lock.writeLock().unlock();
        doFlush(flushedItems);
        flushInProgress = false;
    }

    protected abstract void doFlush(List<E> events);

    @Override
    public void shutdown() {
        this.executorPool.shutdown();
    }
}
