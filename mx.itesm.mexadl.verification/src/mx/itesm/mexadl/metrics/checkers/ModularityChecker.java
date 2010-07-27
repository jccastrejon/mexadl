package mx.itesm.mexadl.metrics.checkers;

import java.util.Map;

import mx.itesm.mexadl.metrics.EnvironmentProperty;
import mx.itesm.mexadl.metrics.tools.CkjmMetrics;

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
        this.checkMetric(context, CkjmMetrics.class, "depthInInheritanceTree");
    }

    /**
     * @param context
     */
    public void numberOfChildren(final Map<EnvironmentProperty, Object> context) {
        this.checkMetric(context, CkjmMetrics.class, "numberOfChildren");
    }

    @Override
    public String getName() {
        return "Modularity";
    }
}
