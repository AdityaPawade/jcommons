package com.adtsw.jcommons.jmx;

public interface JmxMetricsListener extends AutoCloseable {

    /**
     * This is called whenever a metric is updated or added
     *
     * @param metric instance of JmxMetric
     */
    void metricChange(JmxMetric metric);

    /**
     * This is called whenever a metric is removed
     *
     * @param metric instance of JmxMetric
     */
    void metricRemoval(JmxMetric metric);

    /**
     * Called when the metrics repository is closed.
     */
    void close();
}