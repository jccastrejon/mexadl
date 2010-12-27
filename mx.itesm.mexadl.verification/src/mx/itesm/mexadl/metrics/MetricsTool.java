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
