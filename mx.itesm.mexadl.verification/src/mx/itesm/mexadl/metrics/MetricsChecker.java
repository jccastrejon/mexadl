/*
 * Copyright 2010 jccastrejon
 *  
 * This file is part of MexADL.
 *
 * MexADL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MexADL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with MexADL.  If not, see <http://www.gnu.org/licenses/>.
 */
package mx.itesm.mexadl.metrics;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
     * Class logger.
     */
    private Logger logger = Logger.getLogger(MetricsChecker.class.getName());

    /**
     * Check quality metrics for a Java class.
     * 
     * @param metricsSet
     * @param expectedMetricsData
     * @param realMetrics
     * @throws Exception
     */
    public void check(final String type, final String metricsSet,
            final Map<String, Map<String, Object>> expectedMetricsData,
            final Map<String, Map<String, Map<String, Integer>>> realMetrics) throws Exception {
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
            logger.log(Level.INFO, "---- Beginning " + metricsSet + " check ----");
            for (String metric : currentMetrics.keySet()) {
                try {
                    operator = ComparissonOperator.valueOf(Util.getConfigurationProperty(MaintainabilityMetrics.class,
                            metric + ".operator"));
                    tool = Util.getConfigurationProperty(MaintainabilityMetrics.class, metric + ".tool");
                    metricCode = Util.getConfigurationProperty(MaintainabilityMetrics.class, metric + ".code");
                    if (metricCode != null) {
                        results = (Map<String, Map<String, Integer>>) realMetrics.get(tool);
                        expectedValue = Integer.parseInt(currentMetrics.get(metric).toString());

                        if (results != null) {
                            resultsType = results.get(type);
                            if (resultsType != null) {
                                realValue = resultsType.get(metricCode);

                                if (operator.isValid(realValue, expectedValue)) {
                                    logger.log(Level.INFO, "Valid value for " + metric + ". (expected: "
                                            + expectedValue + ", real: " + realValue + "). {MetricsSet: " + metricsSet
                                            + ", Type: [ " + type.replace('.', '/') + " ], Name: " + type + "}");
                                } else {
                                    logger.log(Level.INFO, "Invalid value for " + metric + ". (expected: "
                                            + expectedValue + ", real: " + realValue + "). {MetricsSet: " + metricsSet
                                            + ", Type: [ " + type.replace('.', '/') + " ], Name: " + type + "}");
                                }
                            } else {
                                logger.log(Level.WARNING, "No real value found for metric " + metric + ", expected: "
                                        + expectedValue + ", by tool: " + tool + ". {MetricsSet: " + metricsSet
                                        + ", Type: [ " + type.replace('.', '/') + " ], Name: " + type + "}");
                            }
                        } else {
                            logger.log(Level.WARNING, "No real value found for metric " + metric + ", expected: "
                                    + expectedValue + ", by tool: " + tool + ". {[MetricsSet: " + metricsSet
                                    + ", Type: [ " + type.replace('.', '/') + " ], Name: " + type + "}");
                        }
                    } else {
                        logger.log(Level.WARNING, "No code found for metric: " + metric);
                    }
                } catch (Exception e) {
                    logger.log(Level.WARNING, "An error ocurred whilte trying to analyze metric: " + metric);
                }
            }
            logger.log(Level.INFO, "---- Ending " + metricsSet + " check -------");
        }
    }
}