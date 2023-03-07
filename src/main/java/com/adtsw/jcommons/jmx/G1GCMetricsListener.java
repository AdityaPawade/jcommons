package com.adtsw.jcommons.jmx;

import com.adtsw.jcommons.jmx.exceptions.MetricNotAvailableException;

import java.util.Arrays;
import java.util.List;

public abstract class G1GCMetricsListener implements JmxMetricsListener {

    private final static List<String> memoryPools = Arrays.asList(
        "java.lang:name=G1 Old Gen,type=MemoryPool",
        "java.lang:name=G1 Eden Space,type=MemoryPool",
        "java.lang:name=G1 Survivor Space,type=MemoryPool"
    );
    private static final double MEGABYTE_FACTOR = 1024L * 1024L;

    @Override
    public void metricChange(JmxMetric metric) {
        if (memoryPools.contains(metric.getObjectName()) &&
            metric.getAttributeName().equals("Usage")) {
            try {
                long value = (long) (((long) metric.getValue()) / MEGABYTE_FACTOR);
                String metricName = metric.getName().replaceAll(" ", "_").toLowerCase()
                    + "." + metric.getAttributeName().toLowerCase()
                    + "." + metric.getItemName().toLowerCase();
                onMetricChange(metricName, value);
            } catch (MetricNotAvailableException ignored) {
            }
        }
    }

    @Override
    public void metricRemoval(JmxMetric metric) {
    }

    @Override
    public void close() {
    }
    
    public abstract void onMetricChange(String metricName, long value);
}
