/*
 * Copyright 2010 jccastrejon
 *  
 * This file is part of MexADL.
 *
 * MexADL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MexADL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with MexADL.  If not, see <http://www.gnu.org/licenses/>.
*/
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
