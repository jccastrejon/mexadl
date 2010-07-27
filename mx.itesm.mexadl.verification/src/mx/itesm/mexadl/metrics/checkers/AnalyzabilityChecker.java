package mx.itesm.mexadl.metrics.checkers;

import java.util.Map;

import mx.itesm.mexadl.metrics.EnvironmentProperty;
import mx.itesm.mexadl.metrics.tools.CkjmMetrics;
import mx.itesm.mexadl.metrics.tools.JavaNcssMetrics;

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
        this.checkMetric(context, JavaNcssMetrics.class, "linesOfCode");
    }

    /**
     * @param context
     */
    public void cyclomaticComplexityPerUnit(final Map<EnvironmentProperty, Object> context) {
        this.checkMetric(context, JavaNcssMetrics.class, "cyclomaticComplexityPerUnit");
    }

    /**
     * @param context
     */
    public void responseForClass(final Map<EnvironmentProperty, Object> context) {
        this.checkMetric(context, CkjmMetrics.class, "responseForClass");
    }

    /**
     * @param context
     */
    public void weightedMethodComplexity(final Map<EnvironmentProperty, Object> context) {
        this.checkMetric(context, CkjmMetrics.class, "weightedMethodComplexity");
    }

    /**
     * @param context
     */
    public void numberOfPublicMethods(final Map<EnvironmentProperty, Object> context) {
        this.checkMetric(context, CkjmMetrics.class, "numberOfPublicMethods");
    }

    @Override
    public String getName() {
        return "Analyzability";
    }
}
