package com.adtsw.jcommons.utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ScheduledTask implements Runnable {

    private static final Logger logger = LogManager.getLogger(ScheduledTask.class);
    private final String taskName;
    private final Task actor;
    private final int variationInSeconds;
    private final int timeoutInSeconds;
    private final ExecutorService taskExecutorService;

    @Override
    public void run() {
        if(variationInSeconds > 1) {
            try {
                ThreadLocalRandom random = ThreadLocalRandom.current();
                Thread.sleep(random.nextInt(1, variationInSeconds + 1) * 1000L);
            } catch (InterruptedException e) {
                logger.info("Interruption while sleeping", e);
            }
        }
        logger.info("Running scheduled task " + taskName);

        try {
            Runnable taskRunnable = new Runnable() {
                @Override
                public void run() {
                    actor.execute();
                }
            };
            FutureTask<Boolean> taskFuture = new FutureTask<>(taskRunnable, null);
            Future<?> taskFutureHandle = taskExecutorService.submit(taskFuture);
            long startTs = System.currentTimeMillis();
            try {
                taskFutureHandle.get(timeoutInSeconds, TimeUnit.SECONDS);
            } catch (ExecutionException e) {
                logger.warn("Terminating task " + taskName + " due to execution exception");
                actor.onTimeout();
            } catch (TimeoutException e) {
                logger.warn("Terminating task " + taskName + " due to timeout exception");
                taskFutureHandle.cancel(true);
                actor.onTimeout();
            }
            long endTs = System.currentTimeMillis();
            logger.info("Task " + taskName + " took " + (endTs - startTs) + " ms");
        } catch (InterruptedException e) {
            logger.warn("Terminating task " + taskName + " due to interruption");
        }
    }
}
