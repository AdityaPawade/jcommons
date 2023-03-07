package com.adtsw.jcommons.jmx;

import com.adtsw.jcommons.execution.NamedThreadFactory;
import com.adtsw.jcommons.jmx.exceptions.EmptyJmxMetricListenerListException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;
import java.io.Closeable;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A Class to get all the JMX Metrics from JMX MBeanServer. Also this class continuously monitor the MBeanServer
 * with the given period and watch for any changes in metrics. It notify metrics add or delete events to
 * {@link JmxMetricsListener JmxMetricsListener}.
 */
public class JmxMetricProvider implements Closeable {

    private static final Logger LOG = LogManager.getLogger(JmxMetricProvider.class);

    private static final List<String> blacklistedAttributes = Arrays.asList(
        "BootClassPath",
        "UsageThresholdExceeded", "UsageThreshold", "UsageThresholdCount",
        "CollectionUsageThresholdExceeded", "CollectionUsageThreshold", "CollectionUsageThresholdCount"
    );

    private static final AtomicInteger FACTORY_ID = new AtomicInteger();

    private final ScheduledExecutorService executor;
    private final MBeanServer server;
    private final List<JmxMetricsListener> metricsUpdateListeners;
    private final HashMap<ObjectName, List<JmxMetric>> metricsCache;

    public JmxMetricProvider(List<JmxMetricsListener> metricsUpdateListeners) {
        this(
            metricsUpdateListeners,
            Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(
                "jxm-metric-provider-service-" + FACTORY_ID.incrementAndGet())
            )
        );
    }

    public JmxMetricProvider(List<JmxMetricsListener> metricsUpdateListeners,
                             ScheduledExecutorService executor) {
        this.executor = executor;
        this.metricsUpdateListeners = metricsUpdateListeners;
        this.metricsCache = new HashMap<>();
        this.server = ManagementFactory.getPlatformMBeanServer();
    }

    /**
     * start jmx metric provider service with given initialDelay and then 
     * check for update in metrics with the given period.
     *
     * @param initialDelay initialDelay in starting this service, so that most of the metrics should already be
     *                     present in the jmx MbeanServer
     * @param period       period after which continuously check for metric updates
     * @param unit         unit of time for period and initialDelay
     * @throws EmptyJmxMetricListenerListException throws EmptyMetricUpdateListenerListException when
     *                                             metricsUpdateListeners is null or empty
     */
    public void start(long initialDelay, long period, TimeUnit unit) throws EmptyJmxMetricListenerListException {
        if (this.metricsUpdateListeners == null || this.metricsUpdateListeners.size() == 0) {
            throw new EmptyJmxMetricListenerListException();
        }
        executor.scheduleAtFixedRate(() -> {
            try {
                checkForMetricsUpdate();
            } catch (Exception ex) {
                LOG.error("Exception thrown from #report. Exception was suppressed.", ex);
            }
        }, initialDelay, period, unit);
    }

    /**
     * checkForMetricsUpdate from JMX MBean Server. It collects all the mbeans from mBean server 
     * and then check for new one or if any mbean is removed from Mbean Server. 
     * It also notifies to all the JmxMetricListeners for new metric and deletion of a metrics
     */
    private void checkForMetricsUpdate() {
        
        Set<ObjectName> allMBeans = server.queryNames(null, null);
        Set<ObjectName> removedMBeans = new HashSet<>(metricsCache.keySet());
        
        for (ObjectName mBean : allMBeans) {

            //if current mBean is already handled then ignore it
            if (metricsCache.containsKey(mBean)) {
                removedMBeans.remove(mBean);
                continue;
            }

            try {
                List<JmxMetric> metrics = getMetricsForMBean(mBean);
                //storing to cache
                metricsCache.put(mBean, metrics);
                LOG.debug("Metrics : {}", metrics.toString());
                invokeMetricChangeListeners(metrics);
            } catch (JMException e) {
                LOG.error("Exception in registering for MBean {}", mBean, e);
            }
        }

        // invoking metric removal for removed metrics
        for (ObjectName mBean : removedMBeans) {
            metricsCache.get(mBean).forEach(metric -> {
                metricsUpdateListeners.forEach(listener -> listener.metricRemoval(metric));
            });
            metricsCache.remove(mBean);
        }

        metricsCache.forEach((mBean, jmxMetrics) -> {
            invokeMetricChangeListeners(jmxMetrics);
        });
    }

    private void invokeMetricChangeListeners(List<JmxMetric> metrics) {
        //invoking metric change to listeners for new metrics
        metrics.forEach(metric -> metricsUpdateListeners.forEach(listener -> {
            try {
                listener.metricChange(metric);
            }catch (Exception e){
                LOG.error("error while calling listener.metricChange for metric:{}, listener:{}",
                    metric.toString(), listener.getClass().getCanonicalName());
            }
        }));
    }

    /**
     * returns list of JmxMetric for given mbean name.
     *
     * @param mbean mbean object name
     * @return returns list of JmxMetric for given mbean name.
     * @throws JMException throws JMException
     */
    private List<JmxMetric> getMetricsForMBean(final ObjectName mbean) throws JMException {
        MBeanAttributeInfo[] attrInfo = server.getMBeanInfo(mbean).getAttributes();
        List<JmxMetric> metrics = new ArrayList<>();
        for (MBeanAttributeInfo attr : attrInfo) {
            String attrName = attr.getName();
            try {
                if(!blacklistedAttributes.contains(attrName)) {
                   Object value = server.getAttribute(mbean, attrName);
                   if (value instanceof Number) {
                       metrics.add(new JmxMetric(mbean, attr, null, server));
                   }
                   if (value instanceof CompositeDataSupport) {
                       CompositeDataSupport cd = (CompositeDataSupport) value;
                       cd.getCompositeType().keySet().forEach(item -> {
                           Object value1 = cd.get(item);
                           if (value1 instanceof Number) {
                               metrics.add(new JmxMetric(mbean, attr, item, server));
                           }
                       });
                   }
               }
            } catch (Exception e) {
                LOG.error("Error in getting metric value of {} for attr {}, {}", 
                    mbean, attrName, e.getMessage());
            }
        }
        return metrics;
    }

    /**
     * Stops the metric provider service and shuts down its thread of execution.
     * <p>
     * Uses the shutdown pattern from http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html
     */
    public void stop() {
        executor.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    System.err.println(getClass().getSimpleName() +
                        ": ScheduledExecutorService did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            executor.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Stops the reporter and shuts down its thread of execution.
     */
    @Override
    public void close() {
        stop();
    }
}