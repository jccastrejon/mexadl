package mx.itesm.mexadl.metrics;

import java.util.Map;

/**
 * Interface for the classes in charge of checking metrics real values.
 * 
 * @author jccastrejon
 * 
 */
public interface MetricsChecker {

    /**
     * Check a set of metrics values.
     * 
     * @throws Exception
     */
    public void check(final Map<EnvironmentProperty, Object> context) throws Exception;
}
