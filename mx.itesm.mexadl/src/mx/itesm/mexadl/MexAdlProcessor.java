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
package mx.itesm.mexadl;

import org.jdom.Document;

/**
 * Interface for the processors in charge of analyzing an xADL architecture.
 * 
 * @author jccastrejon
 * 
 */
public interface MexAdlProcessor {

    /**
     * Process a xADL architecture document.
     * 
     * @param document
     * @param xArchFilePath
     * @throws Exception
     */
    public void processDocument(final Document document, final String xArchFilePath) throws Exception;
}
