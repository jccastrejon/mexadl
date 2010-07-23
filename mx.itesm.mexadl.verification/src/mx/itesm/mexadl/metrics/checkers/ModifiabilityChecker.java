package mx.itesm.mexadl.metrics.checkers;

import java.util.Map;

import javax.tools.Diagnostic.Kind;

import mx.itesm.mexadl.metrics.EnvironmentProperty;

/**
 * The ModifiabilityChecker class is responsible for the verification of the
 * expected and actual values for the quality metrics associated to the
 * Modifiability subcharacteristic of the Maintainability quality
 * characteristic.
 * 
 * @see mx.itesm.mexadl.metrics.MaintainabilityMetrics.ModifiabilityMetrics
 * @author jccastrejon
 * 
 */
public class ModifiabilityChecker extends BaseChecker {

    /**
     * @param context
     */
    public void duplicatedBlocks(final Map<EnvironmentProperty, Object> context) {
        int expectedValue;

        expectedValue = this.getExpectedValue(context);
        messager.printMessage(Kind.NOTE, "Checking duplicatedBlocks, expected value: " + expectedValue);
    }

    /**
     * @param context
     */
    public void couplingBetweenObjects(final Map<EnvironmentProperty, Object> context) {
        int expectedValue;

        expectedValue = this.getExpectedValue(context);
        messager.printMessage(Kind.NOTE, "Checking couplingBetweenObjects, expected value: " + expectedValue);
    }

    @Override
    public String getName() {
        return "Modifiability";
    }
}