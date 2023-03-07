package com.adtsw.jcommons.metrics;

import lombok.Getter;

public class TimerContext {
    
    @Getter
    private final String timerName;
    private final long startTime;
    private long endTime = -1;

    TimerContext(String timerName) {
        this.timerName = timerName;
        this.startTime = System.currentTimeMillis();
    }
    
    long stop() {
        if(endTime != -1) {
            throw new RuntimeException("Timer " + timerName + " already stopped");
        }
        this.endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
