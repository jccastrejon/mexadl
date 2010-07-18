package mx.itesm.mexadl.metrics;

import java.util.List;
import java.util.Map;

/**
 * The MetricsData class holds the information related to the quality metrics
 * that have been specified for a given type in an architecture definition.
 * 
 * @author jccastrejon
 * 
 */
public class MetricsData {

    /**
     * Type of the architecture definition for which the quality metrics have
     * been specified.
     */
    private String type;

    /**
     * Quality metrics data.
     */
    private Map<String, String> data;

    /**
     * List of quality metrics specified for a given type in an architecture
     * definition.
     */
    private List<MetricsData> metrics;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public List<MetricsData> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<MetricsData> metrics) {
        this.metrics = metrics;
    }
}
