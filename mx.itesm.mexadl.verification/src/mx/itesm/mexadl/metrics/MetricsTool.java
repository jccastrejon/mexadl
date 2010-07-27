package mx.itesm.mexadl.metrics;

import java.io.File;
import java.util.Map;

/**
 * Interface for the classes in charge of retrieving quality metrics from
 * reporting tools.
 * 
 * @author jccastrejon
 * 
 */
public interface MetricsTool {

    /**
     * Get quality metrics from the reports stored in the specified results
     * directory.
     * 
     * @param resultsDir
     * @return
     * @throws Exception
     */
    public Map<String, Map<String, Integer>> getMetrics(final File resultsDir) throws Exception;
}
