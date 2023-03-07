package com.adtsw.jcommons.metrics;

import java.util.HashMap;

import com.adtsw.jcommons.metrics.prometheus.PrometheusStatsCollector;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;

public class MetricsRegistryStatsCollector implements PrometheusStatsCollector {

    private final String baseName;
    private final MetricsRegistry metricsRegistry;
    private final Gauge gaugeStats;

    public MetricsRegistryStatsCollector(String baseName, MetricsRegistry metricsRegistry, CollectorRegistry registry) {
        
        this.baseName = baseName.replaceAll(" ", "_").toLowerCase();
        this.metricsRegistry = metricsRegistry;

        this.gaugeStats = Gauge.build()
            .name(this.baseName)
            .help(baseName + " Statistics")
            .labelNames("ticker")
            .create().register(registry);
    }

    @Override
    public void update() {
        HashMap<String, Long> currentMetrics = metricsRegistry.toMap();
        currentMetrics.forEach((name, value) -> {
            gaugeStats.labels(name).set(value);
        });
        metricsRegistry.clearTimers();
        metricsRegistry.clearCounters();
        metricsRegistry.clearStats();
    }
}
