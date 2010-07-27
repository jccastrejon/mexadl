package mx.itesm.mexadl.metrics.checkers;

import java.util.Map;

import mx.itesm.mexadl.metrics.EnvironmentProperty;
import mx.itesm.mexadl.metrics.tools.EmmaMetrics;

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
    public void unitTestCoverage(final Map<EnvironmentProperty, Object> context) {
        this.checkMetric(context, EmmaMetrics.class, "unitTestCoverage");
    }

    @Override
    public String getName() {
        return "Testability";
    }
}