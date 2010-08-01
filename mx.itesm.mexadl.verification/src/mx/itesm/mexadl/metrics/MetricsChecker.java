package mx.itesm.mexadl.metrics;

import java.util.Map;

import mx.itesm.mexadl.metrics.util.Util;

/**
 * The MetricsChecker class is responsible of verifying expected and real
 * values, for the quality metrics associated to Java classes.
 * 
 * @author jccastrejon
 * 
 */
public class MetricsChecker {

    /**
     * Check quality metrics for a Java class.
     * 
     * @param metricsSet
     * @param expectedMetricsData
     * @param realMetrics
     * @throws Exception
     */
    public void check(final String metricsSet, Map<String, Map<String, Object>> expectedMetricsData,
            Map<String, Map<String, Map<String, Integer>>> realMetrics) throws Exception {
        String type;
        String tool;
        Integer realValue;
        String metricCode;
        Integer expectedValue;
        ComparissonOperator operator;
        Map<String, Integer> resultsType;
        Map<String, Object> currentMetrics;
        Map<String, Map<String, Integer>> results;

        currentMetrics = expectedMetricsData.get(metricsSet);
        if (currentMetrics != null) {
            type = expectedMetricsData.get(MaintainabilityMetrics.class.getName()).get("type").toString();
            System.out.println("---- Beginning " + metricsSet + " check ----");
            for (String metric : currentMetrics.keySet()) {
                operator = ComparissonOperator.valueOf(Util.getConfigurationProperty(MaintainabilityMetrics.class,
                        metric + ".operator"));
                tool = Util.getConfigurationProperty(MaintainabilityMetrics.class, metric + ".tool");
                metricCode = Util.getConfigurationProperty(MaintainabilityMetrics.class, metric + ".code");
                if (metricCode != null) {
                    results = (Map<String, Map<String, Integer>>) realMetrics.get(tool);

                    if (results != null) {
                        resultsType = results.get(type);
                        if (resultsType != null) {
                            realValue = resultsType.get(metricCode);
                            expectedValue = Integer.parseInt(currentMetrics.get(metric).toString());

                            if (operator.isValid(expectedValue, realValue)) {
                                System.out.println("Valid value for " + metric + ". (expected: " + expectedValue
                                        + ", real: " + realValue + ")");
                            } else {
                                System.out.println("Invalid value for " + metric + ". (expected: " + expectedValue
                                        + ", real: " + realValue + ")");
                            }
                        } else {
                            System.out.println("No metrics found for type: " + type);
                        }
                    } else {
                        System.out.println("No metrics result found for tool: " + tool);
                    }
                } else {
                    System.out.println("No code found for metric: " + metric);
                }
            }
            System.out.println("---- Ending " + metricsSet + " check -------");
        }
    }
}