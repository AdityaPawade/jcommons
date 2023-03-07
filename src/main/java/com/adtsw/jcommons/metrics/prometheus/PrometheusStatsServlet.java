package com.adtsw.jcommons.metrics.prometheus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.MetricsServlet;

public class PrometheusStatsServlet extends MetricsServlet {

    private final List<PrometheusStatsCollector> collectorList;

    public PrometheusStatsServlet(CollectorRegistry registry) {
        super(registry);
        this.collectorList = new ArrayList<>();
    }

    public void addStatsCollector(PrometheusStatsCollector statsCollector) {
        this.collectorList.add(statsCollector);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        for (PrometheusStatsCollector statsCollector : this.collectorList) {
            statsCollector.update();
        }
        super.doGet(req, resp);
    }
}
