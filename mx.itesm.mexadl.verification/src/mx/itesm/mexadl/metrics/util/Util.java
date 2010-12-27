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
package mx.itesm.mexadl.metrics.util;

import java.util.ResourceBundle;

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
}
