package com.adtsw.jcommons.metrics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TimerValue {

    private final Long max;
    private final Long min;
    private final Long avg;

    @Override
    public String toString() {
        return "{" +
            "max=" + max +
            ", min=" + min +
            ", avg=" + avg +
            '}';
    }
}
