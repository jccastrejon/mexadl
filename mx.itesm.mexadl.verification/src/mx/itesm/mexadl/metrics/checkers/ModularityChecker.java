package mx.itesm.mexadl.metrics.checkers;

import java.util.Map;

import javax.tools.Diagnostic.Kind;

import mx.itesm.mexadl.metrics.EnvironmentProperty;

/**
 * The ModularityChecker class is responsible for the verification of the
 * expected and actual values for the quality metrics associated to the
 * Modularity subcharacteristic of the Maintainability quality characteristic.
 * 
 * @see mx.itesm.mexadl.metrics.MaintainabilityMetrics.ModularityMetrics
 * @author jccastrejon
 * 
 */
public class ModularityChecker extends BaseChecker {

    /**
     * @param context
     */
    public void depthInInheritanceTree(final Map<EnvironmentProperty, Object> context) {
        int expectedValue;

        expectedValue = this.getExpectedValue(context);
        messager.printMessage(Kind.NOTE, "Checking depthInInheritanceTree, expected value: " + expectedValue);
    }

    /**
     * @param context
     */
    public void numberOfChildren(final Map<EnvironmentProperty, Object> context) {
        int expectedValue;

        expectedValue = this.getExpectedValue(context);
        messager.printMessage(Kind.NOTE, "Checking numberOfChildren, expected value: " + expectedValue);
    }

    @Override
    public String getName() {
        return "Modularity";
    }
}
