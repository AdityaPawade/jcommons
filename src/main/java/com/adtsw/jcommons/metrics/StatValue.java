package com.adtsw.jcommons.metrics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StatValue {

    private final Long val;

    @Override
    public String toString() {
        return "{" +
            "val=" + val +
            '}';
    }
}
