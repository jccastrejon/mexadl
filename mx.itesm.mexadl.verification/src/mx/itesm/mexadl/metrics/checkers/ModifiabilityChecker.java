package mx.itesm.mexadl.metrics.checkers;

import java.util.Map;

import mx.itesm.mexadl.metrics.EnvironmentProperty;
import mx.itesm.mexadl.metrics.tools.CkjmMetrics;
import mx.itesm.mexadl.metrics.tools.PmdMetrics;

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
        this.checkMetric(context, PmdMetrics.class, "duplicatedBlocks");
    }

    /**
     * @param context
     */
    public void couplingBetweenObjects(final Map<EnvironmentProperty, Object> context) {
        this.checkMetric(context, CkjmMetrics.class, "couplingBetweenObjects");
    }

    @Override
    public String getName() {
        return "Modifiability";
    }
}