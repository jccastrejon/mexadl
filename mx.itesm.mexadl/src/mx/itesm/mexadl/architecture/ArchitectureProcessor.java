package mx.itesm.mexadl.architecture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.itesm.mexadl.MexAdlProcessor;
import mx.itesm.mexadl.util.Util;

import org.apache.velocity.Template;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;

/**
 * The ArchitectureProcessor class generates an aspect that represents the
 * logical architecture of a system, as defined in an xADL architecture
 * definition.
 * 
 * @author jccastrejon
 * 
 */
public class ArchitectureProcessor implements MexAdlProcessor {

    /**
     * XPath expression to identify the groups defined in an xADL document.
     */
    private static XPath groupPath;

    /**
     * AspectJ template that defines a system's architecture.
     */
    private static Template aspectTemplate;

    /**
     * Initialize velocity and JDOM properties.
     */
    static {
        try {
            ArchitectureProcessor.aspectTemplate = Util.getVelocityTemplate(ArchitectureProcessor.class, "aspect");
            ArchitectureProcessor.groupPath = XPath.newInstance("//types:group");
        } catch (Exception e) {
            System.out.println("Error loading ArchitectureProcessor");
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void processDocument(final Document document, final String xArchFilePath) throws Exception {
        String member;
        Element element;
        List<Element> groups;
        List<String> groupMembers;
        Map<String, Object> properties;
        List<Map<String, Object>> groupsList;

        // If there are any groups defined in the architecture description,
        // create an aspect to represent their structure and valid interactions
        groups = ArchitectureProcessor.groupPath.selectNodes(document);
        if ((groups != null) && (!groups.isEmpty())) {
            groupsList = new ArrayList<Map<String, Object>>(groups.size());

            for (Element group : groups) {
                properties = new HashMap<String, Object>();
                groupMembers = new ArrayList<String>();
                properties.put("elements", groupMembers);
                for (Object child : group.getChildren()) {
                    element = (Element) child;
                    if (element.getQualifiedName().equals("instance:description")) {
                        properties.put("name", Util.getValidName(element.getValue()));
                    } else if (element.getQualifiedName().equals("instance:member")) {
                        member = Util.getLinkImplementationClass(document, Util.getHrefValue(element));
                        if (member != null) {
                            groupMembers.add(member);
                        }
                    }
                }

                groupsList.add(properties);
            }

            // Create the architecture aspect only if valid associations were
            // found
            if (!groupsList.isEmpty()) {
                properties = new HashMap<String, Object>();
                properties.put("groupsList", groupsList);
                Util.createFile(document, xArchFilePath, ArchitectureProcessor.aspectTemplate, properties,
                        "ArchitectureAspect", Util.getDocumentName(document), Util.JAVA_EXTENSION);
            }
        }
    }
}
