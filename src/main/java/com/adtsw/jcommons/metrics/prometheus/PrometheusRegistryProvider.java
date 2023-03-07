package com.adtsw.jcommons.metrics.prometheus;

import io.prometheus.client.CollectorRegistry;

public class PrometheusRegistryProvider {

    public CollectorRegistry getDefaultRegistry() {
        return CollectorRegistry.defaultRegistry;
    }

    public CollectorRegistry createRegistry() {
        return new CollectorRegistry(true);
    }
}
