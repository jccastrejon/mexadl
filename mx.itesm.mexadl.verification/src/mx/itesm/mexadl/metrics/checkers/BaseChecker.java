package mx.itesm.mexadl.metrics.checkers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic.Kind;

import mx.itesm.mexadl.metrics.EnvironmentProperty;
import mx.itesm.mexadl.metrics.MaintainabilityMetrics;
import mx.itesm.mexadl.metrics.MetricsChecker;

/**
 * The BaseChecker class contains the basic behavior expected from a metrics
 * checker. Subclasses should implement methods according to their associated
 * metrics set annotation.
 * 
 * @author jccastrejon
 * 
 */
public abstract class BaseChecker implements MetricsChecker {

    /**
     * Annotation processor messager.
     */
    protected Messager messager;

    /**
     * Metrics set method.
     */
    protected Method metricsSetMethod;

    /**
     * Maintainability metrics.
     */
    protected MaintainabilityMetrics maintainabilityMetrics;

    @Override
    public void check(final Map<EnvironmentProperty, Object> context) throws Exception {
        List<String> validMetrics;

        // Recover environment properties
        maintainabilityMetrics = (MaintainabilityMetrics) context.get(EnvironmentProperty.METRICS_DATA);
        metricsSetMethod = (Method) context.get(EnvironmentProperty.METRICS_SET);
        messager = (Messager) context.get(EnvironmentProperty.MESSAGER);

        // Check the valid metrics associated to the current metric set
        validMetrics = this.getValidMetrics(metricsSetMethod.getReturnType(), context);
        if (!validMetrics.isEmpty()) {
            messager.printMessage(Kind.NOTE, "---- Beginning " + this.getName() + " check ----");
            for (String metric : validMetrics) {
                this.executeCheckMethod(metric, context);
            }
            messager.printMessage(Kind.NOTE, "---- Ending " + this.getName() + " check -------");
        }
    }

    /**
     * Execute the corresponding check method for a given metric.
     * 
     * @param name
     * @param context
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private void executeCheckMethod(final String name, final Map<EnvironmentProperty, Object> context)
            throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        Method checkMethod;

        checkMethod = this.getClass().getMethod(name, Map.class);
        checkMethod.invoke(this, context);
    }

    /**
     * Get the collection of metrics that have been specified in the MexADL
     * constructs.
     * 
     * @param metricsSet
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private List<String> getValidMetrics(final Class<?> metricsSet, final Map<EnvironmentProperty, Object> context)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        int expectedValue;
        boolean methodFound;
        Object metricsSetRef;
        List<String> returnValue;

        returnValue = new ArrayList<String>();
        for (Method method : metricsSet.getDeclaredMethods()) {
            methodFound = false;
            for (Method classMethod : Class.class.getDeclaredMethods()) {
                if (method.getName().equals(classMethod.getName())) {
                    methodFound = true;
                    break;
                }
            }

            // Valid methods are those defined in the annotation body and that
            // have been given a value that is different from the default (-1)
            if (!methodFound) {
                metricsSetRef = metricsSetMethod.invoke(maintainabilityMetrics, (Object[]) null);
                expectedValue = (Integer) method.invoke(metricsSetRef, (Object[]) null);
                if (expectedValue >= 0) {
                    context.put(EnvironmentProperty.EXPECTED_VALUE, expectedValue);
                    returnValue.add(method.getName());
                }
            }
        }

        return returnValue;
    }

    /**
     * Get the expected value for a metric specified in a MexADL construct.
     * 
     * @param context
     * @return
     */
    protected int getExpectedValue(final Map<EnvironmentProperty, Object> context) {
        return (Integer) context.get(EnvironmentProperty.EXPECTED_VALUE);
    }

    /**
     * Checker name.
     * 
     * @return
     */
    public abstract String getName();
}