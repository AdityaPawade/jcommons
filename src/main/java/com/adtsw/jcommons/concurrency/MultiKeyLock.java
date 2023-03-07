package com.adtsw.jcommons.concurrency;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.javatuples.Pair;

public class MultiKeyLock {
    
    private final ReentrantReadWriteLock serviceLock;
    private final Map<String, Pair<ReentrantReadWriteLock, Integer>> keyLocks;

    public MultiKeyLock() {
        this.serviceLock = new ReentrantReadWriteLock();
        this.keyLocks = new HashMap<>();
    }

    public ReentrantReadWriteLock getLock(String key) {
        serviceLock.writeLock().lock();
        Pair<ReentrantReadWriteLock, Integer> keyLockCount = keyLocks.get(key);
        ReentrantReadWriteLock keyLock = null;
        if(keyLockCount == null) {
            keyLock = new ReentrantReadWriteLock();
            keyLockCount = Pair.with(keyLock, 1);
            keyLocks.put(key, keyLockCount);
        } else {
            keyLock = keyLockCount.getValue0();
            Integer keyMutationCount = keyLockCount.getValue1();
            keyLocks.put(key, Pair.with(keyLock, keyMutationCount + 1));
        }
        serviceLock.writeLock().unlock();
        return keyLock;
    }

    public void releaseLock(String key) {
        serviceLock.writeLock().lock();
        Pair<ReentrantReadWriteLock, Integer> keyLockCount = keyLocks.get(key);
        if(keyLockCount != null) {
            ReentrantReadWriteLock keyLock = keyLockCount.getValue0();
            Integer keyMutationCount = keyLockCount.getValue1();
            int newKeyMutationCount = keyMutationCount - 1;
            if(newKeyMutationCount == 0) {
                keyLocks.remove(key);
            } else {
                keyLocks.put(key, Pair.with(keyLock, newKeyMutationCount));
            }
        }
        serviceLock.writeLock().unlock();
    }
}
