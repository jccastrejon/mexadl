package mx.itesm.mexadl.metrics.checkers;

import java.util.Map;

import javax.tools.Diagnostic.Kind;

import mx.itesm.mexadl.metrics.EnvironmentProperty;

/**
 * The TestabilityChecker class is responsible for the verification of the
 * expected and actual values for the quality metrics associated to the
 * Testability subcharacteristic of the Maintainability quality characteristic.
 * 
 * @see mx.itesm.mexadl.metrics.MaintainabilityMetrics.TestabilityMetrics
 * @author jccastrejon
 * 
 */
public class TestabilityChecker extends BaseChecker {

    /**
     * @param context
     */
    public void completenessOfBuiltInFunction(final Map<EnvironmentProperty, Object> context) {
        int expectedValue;

        expectedValue = this.getExpectedValue(context);
        messager.printMessage(Kind.NOTE, "Checking completenessOfBuiltInFunction, expected value: " + expectedValue);
    }

    /**
     * @param context
     */
    public void autonomyOfTestability(final Map<EnvironmentProperty, Object> context) {
        int expectedValue;

        expectedValue = this.getExpectedValue(context);
        messager.printMessage(Kind.NOTE, "Checking autonomyOfTestability, expected value: " + expectedValue);
    }

    /**
     * @param context
     */
    public void unitTestCoverage(final Map<EnvironmentProperty, Object> context) {
        int expectedValue;

        expectedValue = this.getExpectedValue(context);
        messager.printMessage(Kind.NOTE, "Checking unitTestCoverage, expected value: " + expectedValue);
    }

    @Override
    public String getName() {
        return "Testability";
    }
}