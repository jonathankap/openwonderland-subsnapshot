/**
 * Open Wonderland
 *
 * Copyright (c) 2011 - 2012, Open Wonderland Foundation, All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * The Open Wonderland Foundation designates this particular file as
 * subject to the "Classpath" exception as provided by the Open Wonderland
 * Foundation in the License file that accompanied this code.
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
