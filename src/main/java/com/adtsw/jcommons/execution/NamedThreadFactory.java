package com.adtsw.jcommons.execution;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;

/**
 * A simple named thread factory.
 */
public class NamedThreadFactory implements ThreadFactory {
    @Getter
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public NamedThreadFactory(String name) {
        final SecurityManager s = System.getSecurityManager();
        this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = name + "-thread-";
    }

    public Thread newThread(Runnable r) {
        final Thread t = new Thread(
            group, r, namePrefix + threadNumber.getAndIncrement(), 0
        );
        t.setDaemon(true);
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}