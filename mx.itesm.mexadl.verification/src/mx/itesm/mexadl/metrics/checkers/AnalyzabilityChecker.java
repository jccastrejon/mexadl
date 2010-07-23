package mx.itesm.mexadl.metrics.checkers;

import java.util.Map;

import javax.tools.Diagnostic.Kind;

import mx.itesm.mexadl.metrics.EnvironmentProperty;

/**
 * The AnalyzabilityChecker class is responsible for the verification of the
 * expected and actual values for the quality metrics associated to the
 * Analyzability subcharacteristic of the Maintainability quality
 * characteristic.
 * 
 * @see mx.itesm.mexadl.metrics.MaintainabilityMetrics.AnalyzabilityMetrics
 * @author jccastrejon
 * 
 */
public class AnalyzabilityChecker extends BaseChecker {

    /**
     * @param context
     */
    public void linesOfCode(final Map<EnvironmentProperty, Object> context) {
        int expectedValue;

        expectedValue = this.getExpectedValue(context);
        messager.printMessage(Kind.NOTE, "Checking linesOfCode, expected value: " + expectedValue);
    }

    /**
     * @param context
     */
    public void cyclomaticComplexityPerUnit(final Map<EnvironmentProperty, Object> context) {
        int expectedValue;

        expectedValue = this.getExpectedValue(context);
        messager.printMessage(Kind.NOTE, "Checking cyclomaticComplexityPerUnit, expected value: " + expectedValue);
    }

    /**
     * @param context
     */
    public void responseForClass(final Map<EnvironmentProperty, Object> context) {
        int expectedValue;

        expectedValue = this.getExpectedValue(context);
        messager.printMessage(Kind.NOTE, "Checking responseForClass, expected value: " + expectedValue);
    }

    /**
     * @param context
     */
    public void weightedMethodComplexity(final Map<EnvironmentProperty, Object> context) {
        int expectedValue;

        expectedValue = this.getExpectedValue(context);
        messager.printMessage(Kind.NOTE, "Checking weightedMethodComplexity, expected value: " + expectedValue);
    }

    /**
     * @param context
     */
    public void numberOfPublicMethods(final Map<EnvironmentProperty, Object> context) {
        int expectedValue;

        expectedValue = this.getExpectedValue(context);
        messager.printMessage(Kind.NOTE, "Checking numberOfPublicMethods, expected value: " + expectedValue);
    }

    @Override
    public String getName() {
        return "Analyzability";
    }
}
