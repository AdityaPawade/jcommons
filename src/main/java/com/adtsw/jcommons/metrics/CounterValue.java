package com.adtsw.jcommons.metrics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CounterValue {

    private final Long val;

    @Override
    public String toString() {
        return "{" +
            "val=" + val +
            '}';
    }
}
