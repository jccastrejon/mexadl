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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.itesm.mexadl.metrics.util.Util;

import org.objectweb.asm.ClassReader;

/**
 * The VerificationProcessor class is responsible of collection, analysis and
 * verification of quality metrics associated to java classes.
 * 
 * @author jccastrejon
 * 
 */
public class VerificationProcessor {

    /**
     * 
     */
    private static Logger logger = Logger.getLogger(VerificationProcessor.class.getName());

    /**
     * Sets of metrics that should be analyzed.
     */
    private static final String[] METRICS_SETS = Util.getConfigurationProperty(MaintainabilityMetrics.class,
            "metricsSets").split(",");

    /**
     * Process the quality metrics of the classes found in the
     * <em>classesDir</em> parameter, according to the metrics reports found in
     * the <em>reportsDir</em> parameter.
     * 
     * @param classesDir
     * @param reportsDir
     * @throws Exception
     */
    public static void processMetrics(final File classesDir, final File reportsDir) throws Exception {
        String currentType;
        List<String> classes;
        InputStream inputStream;
        MetricsVisitor metricsVisitor;
        MetricsChecker metricsChecker;
        Map<String, Map<String, Object>> expectedMetrics;
        Map<String, Map<String, Map<String, Integer>>> realMetrics;

        classes = Util.getClassesInDirectory(classesDir);
        if (classes != null) {
            realMetrics = VerificationProcessor.collectMetrics(reportsDir);
            metricsChecker = new MetricsChecker();

            for (String clazz : classes) {
                metricsVisitor = new MetricsVisitor();
                inputStream = new FileInputStream(clazz);
                new ClassReader(inputStream).accept(metricsVisitor, ClassReader.SKIP_DEBUG);
                expectedMetrics = metricsVisitor.getMetrics();
                currentType = metricsVisitor.getType();

                // Analyze only user classes associated to the
                // MaintainabilityMetrics annotation
                if ((!expectedMetrics.isEmpty()) && (!currentType.startsWith("mx.itesm.mexadl"))) {
                    VerificationProcessor.logger.log(Level.INFO, "** Beginning analysis for: " + currentType + " **");
                    for (String metricsSet : VerificationProcessor.METRICS_SETS) {
                        metricsChecker.check(currentType, metricsSet, expectedMetrics, realMetrics);
                    }
                    VerificationProcessor.logger.log(Level.INFO, "** Ending analysis for: " + currentType + " *****");
                }
            }
        }
    }

    /**
     * Collect quality metrics reports from registered metrics tools.
     * 
     * @param directory
     * @return
     */
    private static Map<String, Map<String, Map<String, Integer>>> collectMetrics(final File directory) {
        String[] tools;
        MetricsTool metricsTool;
        Map<String, Map<String, Integer>> metrics;
        Map<String, Map<String, Map<String, Integer>>> returnValue;

        try {
            tools = Util.getConfigurationProperty(MaintainabilityMetrics.class, "tools").split(",");
            returnValue = new HashMap<String, Map<String, Map<String, Integer>>>(tools.length);
            for (String tool : tools) {
                metricsTool = (MetricsTool) Class.forName(tool.trim()).newInstance();
                metrics = metricsTool.getMetrics(directory);
                returnValue.put(tool.trim(), metrics);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnValue = null;
        }

        return returnValue;
    }
}