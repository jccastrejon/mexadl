package mx.itesm.mexadl.metrics.checkers;

import java.util.Map;

import javax.tools.Diagnostic.Kind;

import mx.itesm.mexadl.metrics.EnvironmentProperty;

/**
 * The ReusabilityChecker class is responsible for the verification of the
 * expected and actual values for the quality metrics associated to the
 * Reusability subcharacteristic of the Maintainability quality characteristic.
 * 
 * @see mx.itesm.mexadl.metrics.MaintainabilityMetrics.ReusabilityMetrics
 * @author jccastrejon
 * 
 */
public class ReusabilityChecker extends BaseChecker {

    /**
     * @param context
     */
    public void lackOfCohesionOfMethods(final Map<EnvironmentProperty, Object> context) {
        int expectedValue;

        expectedValue = this.getExpectedValue(context);
        messager.printMessage(Kind.NOTE, "Checking lackOfCohesionOfMethods, expected value: " + expectedValue);
    }

    /**
     * @param context
     */
    public void afferentCoupling(final Map<EnvironmentProperty, Object> context) {
        int expectedValue;

        expectedValue = this.getExpectedValue(context);
        messager.printMessage(Kind.NOTE, "Checking afferentCoupling, expected value: " + expectedValue);
    }

    @Override
    public String getName() {
        return "Reusability";
    }
}
