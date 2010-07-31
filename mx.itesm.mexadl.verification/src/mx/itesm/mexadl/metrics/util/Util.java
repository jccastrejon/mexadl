package mx.itesm.mexadl.metrics.util;

import java.util.Map;
import java.util.ResourceBundle;

import mx.itesm.mexadl.metrics.MaintainabilityMetrics;

/**
 * The Util class provides helper methods for the MexADL verification process.
 * 
 * @author jccastrejon
 * 
 */
public class Util {

    /**
     * Verification properties.
     */
    private static ResourceBundle properties = ResourceBundle.getBundle("mx.itesm.mexadl.metrics.configuration");

    /**
     * Get the value associated to the specified property in the MexADL
     * configuration file.
     * 
     * @param clazz
     * @param name
     * @return
     */
    public static String getConfigurationProperty(final Class<?> clazz, final String name) {
        String returnValue;

        try {
            returnValue = Util.properties.getString(clazz.getName() + "." + name);
        } catch (Exception e) {
            returnValue = null;
        }

        return returnValue;
    }

    /**
     * Get the type associated to the specified quality metrics.
     * 
     * @param expectedMetrics
     * @return
     */
    public static String getExpectedMetricsType(Map<String, Map<String, Object>> expectedMetrics) {
        return expectedMetrics.get(MaintainabilityMetrics.class.getName()).get("type").toString();
    }
}
