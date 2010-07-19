package mx.itesm.mexadl;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.IBNAMenuListener;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.xarchflat.ObjRef;

/**
 * Manage the invocation of the MexADL services.
 * 
 * @author jccastrejon
 * 
 */
public class MexAdlLogic extends AbstractThingLogic implements IBNAMenuListener {

    /**
     * xADL document.
     */
    private ObjRef xArchRef;

    /**
     * Archipelago services.
     */
    private ArchipelagoServices archipelagoServices;

    /**
     * Full constructor.
     * 
     * @param archipelagoServices
     *            ArchipelagoServices.
     * @param xArchRef
     *            xADL document reference.
     */
    public MexAdlLogic(ArchipelagoServices archipelagoServices, ObjRef xArchRef) {
        this.archipelagoServices = archipelagoServices;
        this.xArchRef = xArchRef;
    }

    @Override
    public void fillMenu(IBNAView view, IMenuManager menuManager, int localX, int localY, IThing thing, int worldX,
            int worldY) {

        // This menu is only valid at a global level
        if (thing == null) {
            // adds a menu item
            menuManager.add(new Action("Generate MexADL artifacts") {
                @Override
                public void run() {
                    MexAdlAnalyzer.analyzeXArch(archipelagoServices.xarch.serialize(xArchRef), ResourcesPlugin
                            .getWorkspace().getRoot().getRawLocation()
                            + archipelagoServices.xarch.getXArchURI(xArchRef));
                }
            });

            // adds a menu separator
            menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        }
    }
}
