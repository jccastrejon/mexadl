package mx.itesm.mexadl.metrics.util;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import mx.itesm.mexadl.metrics.VerificationProcessor;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * The MexAdlTask is responsible of executing the quality metrics' verification
 * process from an Ant target.
 * 
 * @author jccastrejon
 * 
 */
public class MexAdlTask extends Task {

    /**
     * Class logger.
     */
    Logger logger = Logger.getLogger(MexAdlTask.class.getName());

    /**
     * Directory where the Java classes to be analyzed are stored.
     */
    private String classes;

    /**
     * Directory where the metrics reports are stored.
     */
    private String reports;

    static {
        // Logging configuration
        try {
            LogManager.getLogManager()
                    .readConfiguration(
                            MexAdlTask.class.getClassLoader().getResourceAsStream(
                                    "mx/itesm/mexadl/metrics/logging.properties"));
        } catch (Exception e) {
            System.out.println("Unable to register logging configuration: " + e);
        }
    }

    /**
     * Task execution.
     */
    public void execute() throws BuildException {
        try {
            VerificationProcessor.processMetrics(new File(this.classes), new File(this.reports));
        } catch (Exception e) {
            logger.log(Level.WARNING, "An error ocurred while executing verification process: " + e);
        }
    }

    // Setters

    public void setClasses(final String classes) {
        this.classes = classes;
    }

    public void setReports(final String reports) {
        this.reports = reports;
    }
}
