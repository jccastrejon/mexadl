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
