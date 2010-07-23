package mx.itesm.mexadl.metrics;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
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

                context.put(EnvironmentProperty.METRICS_DATA, maintainabilityMetrics);
                messager.printMessage(Kind.NOTE, "Checking metrics for: " + maintainabilityMetrics.type());
                for (Method method : maintainabilityMetrics.getClass().getDeclaredMethods()) {
                    try {
                        metricsSet = method;
                        context.put(EnvironmentProperty.METRICS_SET, metricsSet);
                        metricsChecker = VerificationProcessor.getMetricsChecker(metricsSet);

                        if (metricsChecker != null) {
                            metricsChecker.check(context);
                        }
                    } catch (Exception e) {
                        messager.printMessage(Kind.ERROR, "Invalid checker found for " + method.getName() + " : "
                                + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }

        return true;
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

        checkerClass = Util.getConfigurationProperty(subcharacteristic.getReturnType(), "checker");

        returnValue = null;
        if (checkerClass != null) {
            returnValue = (MetricsChecker) Class.forName(checkerClass).newInstance();
        }

        return returnValue;
    }
}