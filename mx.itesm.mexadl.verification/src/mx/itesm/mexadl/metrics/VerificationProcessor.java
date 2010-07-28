package mx.itesm.mexadl.metrics;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import mx.itesm.mexadl.metrics.util.Util;

/**
 * The VerificationProcessor class is responsible for the processing of the
 * Annotations associated to the MexADL verification constructs.
 * 
 * @author jccastrejon
 * 
 */
@SupportedOptions("mexadl.reports.dir")
@SupportedAnnotationTypes("mx.itesm.mexadl.*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class VerificationProcessor extends AbstractProcessor {

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment environment) {
        Method metricsSet;
        Messager messager;
        TypeElement typeElement;
        MetricsChecker metricsChecker;
        Map<EnvironmentProperty, Object> context;
        MaintainabilityMetrics maintainabilityMetrics;

        context = new HashMap<EnvironmentProperty, Object>();
        messager = processingEnv.getMessager();
        context.put(EnvironmentProperty.MESSAGER, messager);

        for (TypeElement annotation : annotations) {
            for (Element element : environment.getElementsAnnotatedWith(annotation)) {
                typeElement = ((TypeElement) element);
                maintainabilityMetrics = typeElement.getAnnotation(MaintainabilityMetrics.class);

                // Check metrics only if real data can be collected
                if (this.collectMetrics(context)) {
                    context.put(EnvironmentProperty.EXPECTED_METRICS_DATA, maintainabilityMetrics);
                    context.put(EnvironmentProperty.TYPE, maintainabilityMetrics.type());
                    messager.printMessage(Kind.NOTE, "Checking metrics for: " + maintainabilityMetrics.type());
                    for (Method method : maintainabilityMetrics.getClass().getDeclaredMethods()) {
                        try {
                            metricsSet = method;
                            context.put(EnvironmentProperty.METRICS_SET_NAME, metricsSet);
                            metricsChecker = VerificationProcessor.getMetricsChecker(metricsSet);

                            if (metricsChecker != null) {
                                metricsChecker.check(context);
                            }
                        } catch (Exception e) {
                            messager.printMessage(Kind.ERROR, "Error in Metrics checker: " + method.getName() + " : "
                                    + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        // Claim the MexADL annotations
        return true;
    }

    /**
     * Collect metrics from the tools registered in the configuration properties
     * file.
     * 
     * @param context
     * @throws Exception
     */
    private boolean collectMetrics(final Map<EnvironmentProperty, Object> context) {
        String[] tools;
        boolean returnValue;
        File reportsDirectory;
        MetricsTool metricsTool;
        Map<String, Map<String, Integer>> metrics;
        Map<String, Map<String, Map<String, Integer>>> realMetrics;

        try {
            returnValue = true;
            reportsDirectory = this.getReportsDirectory();
            tools = Util.getConfigurationProperty(this.getClass(), "tools").split(",");
            realMetrics = new HashMap<String, Map<String, Map<String, Integer>>>(tools.length);
            context.put(EnvironmentProperty.REAL_METRICS_DATA, realMetrics);
            for (String tool : tools) {
                metricsTool = (MetricsTool) Class.forName(tool.trim()).newInstance();
                metrics = metricsTool.getMetrics(reportsDirectory);
                realMetrics.put(tool.trim(), metrics);
            }
        } catch (Exception e) {
            returnValue = false;
        }

        return returnValue;
    }

    /**
     * Get the directory where the metrics reports are stored.
     * 
     * @return
     */
    private File getReportsDirectory() {
        String path;
        File returnValue;

        returnValue = null;
        path = processingEnv.getOptions().get("mexadl.reports.dir");
        if (path != null) {
            returnValue = new File(path);
            if (!returnValue.exists()) {
                returnValue = null;
            }
        }

        return returnValue;
    }

    /**
     * Get the checker class for the metrics that correspond to the specified
     * quality sub-characteristic.
     * 
     * @param characteristic
     * @param subcharacteristic
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static MetricsChecker getMetricsChecker(final Method subcharacteristic) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        String checkerClass;
        MetricsChecker returnValue;

        returnValue = null;
        checkerClass = Util.getConfigurationProperty(subcharacteristic.getReturnType(), "checker");
        if (checkerClass != null) {
            returnValue = (MetricsChecker) Class.forName(checkerClass).newInstance();
        }

        return returnValue;
    }
}