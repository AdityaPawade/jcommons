package com.adtsw.jcommons.execution;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ThreadPoolStats {

    // estimate of the number of threads waiting to acquire
    private final int semaphoreQueueLength;
    // the current number of permits available in this semaphore
    private final int semaphorePermitsAvailable;
    // size of worker threads in the pool
    private final int workerPoolSize;
    // the task queue size used by the executor.
    private final int taskQueueLength;
    // the task queue remaining capacity used by the executor.    
    private final int taskQueueRemainingCapacity;
}
