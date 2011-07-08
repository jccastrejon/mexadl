package mx.itesm.mexadl.metrics.util;

import java.util.logging.LogManager;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * The MexADLReportsTask class is responsible for generating HTML report for
 * user-friendly access.
 * 
 * @author jccastrejon
 * 
 */
public class MexADLReportsTask extends Task {
    /**
     * Base directory
     */
    private String basedir;

    /**
     * Task execution.
     */
    public void execute() throws BuildException {

        try {
            // Logging configuration
            LogManager.getLogManager().readConfiguration(
                    MexAdlTask.class.getClassLoader().getResourceAsStream(
                            "mx/itesm/mexadl/metrics/logging-reports.properties"));

            // Generate HTML reports
            Util.generateHtmlReport("interactions-verification", this.basedir);
            Util.generateHtmlReport("metrics-verification", this.basedir);
        } catch (Exception e) {
            // no-op
        }
    }

    public void setBasedir(final String basedir) {
        this.basedir = basedir;
    }

}
