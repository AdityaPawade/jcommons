package com.adtsw.jcommons.metrics.prometheus;

import javax.servlet.Servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import io.prometheus.client.CollectorRegistry;

public class PrometheusExportServer {

    protected static Logger logger = LogManager.getLogger(PrometheusExportServer.class);

    private final int port;
    private final Server server;
    private final ServletContextHandler context;
    private final PrometheusStatsServlet defaultServlet;
    private boolean started = false;

    public PrometheusExportServer(int port, CollectorRegistry defaultRegistry) {
        this(port, new PrometheusStatsServlet(defaultRegistry), "/metrics");
    }

    public PrometheusExportServer(int port, PrometheusStatsServlet defaultMetricsServlet) {
        this(port, defaultMetricsServlet, "/metrics");
    }

    public PrometheusExportServer(int port, PrometheusStatsServlet defaultMetricsServlet, String defaultPathSpec) {
        this.port = port;
        this.server = new Server(port);
        this.context = new ServletContextHandler();
        this.context.setContextPath("/");
        this.server.setHandler(context);
        this.defaultServlet = defaultMetricsServlet;
        this.addServlet(defaultPathSpec, defaultMetricsServlet);
    }

    public void addServlet(String pathSpec, Servlet servlet) {
        context.addServlet(new ServletHolder(servlet), pathSpec);
    }

    public void addCollector(PrometheusStatsCollector statsCollector) {
        defaultServlet.addStatsCollector(statsCollector);
    }

    public void start() {
        try {
            server.start();
            started = true;
            logger.info("Prometeus export server is running on port " + port);
        } catch (Exception ex) {
            logger.warn("Failed to start Prometeus export server on port " + port, ex);
        }
    }

    public void stop() {
        try {
            if(started) {
                server.stop();
                started = false;
                logger.info("Prometeus export server has been stopped");
            }
        } catch (Exception ex) {
            logger.warn("Failed to stop Prometeus export server", ex);
        }
    }
}
