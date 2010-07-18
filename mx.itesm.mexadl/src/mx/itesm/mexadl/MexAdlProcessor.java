package mx.itesm.mexadl;

import java.io.IOException;

import org.jdom.Document;
import org.jdom.JDOMException;

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
     * @throws JDOMException
     * @throws IOException
     */
    public void processDocument(final Document document, final String xArchFilePath) throws JDOMException, IOException;
}
