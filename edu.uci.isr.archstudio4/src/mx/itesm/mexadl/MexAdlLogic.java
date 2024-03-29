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

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.ui.IWorkbenchActionConstants;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.IBNAMenuListener;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.logics.tracking.ModelBoundsTrackingLogic;
import edu.uci.isr.bna4.logics.util.ExportBitmapLogic;
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
     * 
     */
    private MexADLExportBitmapLogic exportLogic;

    /**
     * Manages the exporting of architecture images.
     * 
     * @author jccastrejon
     * 
     */
    private class MexADLExportBitmapLogic extends ExportBitmapLogic {
        public MexADLExportBitmapLogic(ModelBoundsTrackingLogic mbtl) {
            super(mbtl);
        }

        /**
         * Export the given view as a PNG file inside the
         * <em>MexADL_outputDir/Architecture.png</em> path.
         * 
         * @param view
         *            View to export.
         * @param xArchFilePath
         *            Path to the architecture file.
         */
        public void exportImage(final IBNAView view, final String xArchFilePath) {
            Image image;
            ImageLoader imageLoader;

            // Create and export image
            image = this.createImage(view);
            try {
                imageLoader = new ImageLoader();
                imageLoader.data = new ImageData[] { image.getImageData() };
                imageLoader.save(new File(mx.itesm.mexadl.util.Util.getOutputDir(xArchFilePath), "Architecture.png")
                        .getAbsolutePath(), SWT.IMAGE_PNG);
            } finally {
                if (image != null) {
                    image.dispose();
                }
            }
        }
    }

    /**
     * Full constructor.
     * 
     * @param archipelagoServices
     *            ArchipelagoServices.
     * @param xArchRef
     *            xADL document reference.
     * @param mbtl
     *            ModelBoundsTrackingLogic
     */
    public MexAdlLogic(final ArchipelagoServices archipelagoServices, final ObjRef xArchRef,
            final ModelBoundsTrackingLogic mbtl) {
        this.archipelagoServices = archipelagoServices;
        this.xArchRef = xArchRef;
        this.exportLogic = new MexADLExportBitmapLogic(mbtl);
    }

    @Override
    public void fillMenu(final IBNAView view, final IMenuManager menuManager, final int localX, final int localY,
            final IThing thing, final int worldX, final int worldY) {

        // This menu is only valid at a global level
        if (thing == null) {
            // adds a menu item
            menuManager.add(new Action("Generate MexADL artifacts") {
                @Override
                public void run() {
                    try {
                        String xArchFilePath;

                        // Analyze and generate MexADL artifacts
                        xArchFilePath = ResourcesPlugin.getWorkspace().getRoot().getRawLocation()
                                + archipelagoServices.xarch.getXArchURI(xArchRef);
                        MexAdlAnalyzer.analyzeXArch(archipelagoServices.xarch.serialize(xArchRef), xArchFilePath);

                        // Export the architecture as image
                        exportLogic.exportImage(view, xArchFilePath);

                        // Show success message and refresh workspace
                        MessageDialog.openInformation(null, "MexADL",
                                "The MexADL artifacts were successfully generated!");
                        MexAdlLogic.refreshWorkspace();
                    } catch (Exception e) {
                        MessageDialog.openInformation(null, "MexADL",
                                "An error ocurred while generating the MexADL artifacts, check log file.");
                    }
                }
            });

            // adds a menu separator
            menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        }
    }

    /**
     * Refresh all projects in the current workspace
     */
    public static void refreshWorkspace() {
        IProject[] projects;

        projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        for (IProject project : projects) {
            try {
                project.refreshLocal(IProject.DEPTH_INFINITE, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
