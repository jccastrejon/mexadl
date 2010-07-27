package mx.itesm.mexadl.metrics.checkers;

import java.util.Map;

import mx.itesm.mexadl.metrics.EnvironmentProperty;
import mx.itesm.mexadl.metrics.tools.CkjmMetrics;

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
        this.checkMetric(context, CkjmMetrics.class, "lackOfCohesionOfMethods");
    }

    /**
     * @param context
     */
    public void afferentCoupling(final Map<EnvironmentProperty, Object> context) {
        this.checkMetric(context, CkjmMetrics.class, "afferentCoupling");
    }

    @Override
    public String getName() {
        return "Reusability";
    }
}
