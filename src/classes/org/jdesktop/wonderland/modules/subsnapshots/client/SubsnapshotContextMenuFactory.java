/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdesktop.wonderland.modules.subsnapshots.client;

import com.jme.math.Vector3f;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import org.jdesktop.wonderland.client.cell.Cell;
import org.jdesktop.wonderland.client.contextmenu.ContextMenuActionListener;
import org.jdesktop.wonderland.client.contextmenu.ContextMenuItem;
import org.jdesktop.wonderland.client.contextmenu.ContextMenuItemEvent;
import org.jdesktop.wonderland.client.contextmenu.SimpleContextMenuItem;
import org.jdesktop.wonderland.client.contextmenu.annotation.ContextMenuFactory;
import org.jdesktop.wonderland.client.contextmenu.spi.ContextMenuFactorySPI;
import org.jdesktop.wonderland.client.jme.ViewManager;
import org.jdesktop.wonderland.client.scenemanager.event.ContextEvent;

/**
 *
 * @author WonderlandWednesday
 */
@ContextMenuFactory
public class SubsnapshotContextMenuFactory implements ContextMenuFactorySPI {

    private static final Logger LOGGER =
            Logger.getLogger(SubsnapshotArchive.class.getName());
    
    private static final ResourceBundle bundle =
            ResourceBundle.getBundle("org/jdesktop/wonderland/modules/subsnapshots/client/resources/Bundle");

    public ContextMenuItem[] getContextMenuItems(ContextEvent event) {
        return new ContextMenuItem[]{
            new SimpleContextMenuItem(bundle.getString("EXPORT"), new ContextMenuActionListener() {

                public void actionPerformed(ContextMenuItemEvent event) {
                    Cell cell = event.getCell();
                    if (cell == null) {
                        return;
                    }
                    Vector3f origin = new Vector3f();
                    ViewManager.getViewManager().getPrimaryViewCell().getWorldTransform().getTranslation(origin);
                    //exportCell(cell, origin);
                    SubsnapshotExporter.getInstance(cell).exportCell(cell);
                   
                }
            })
        };
    }  
}
