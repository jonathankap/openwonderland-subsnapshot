/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jdesktop.wonderland.modules.subsnapshots.client;

import org.jdesktop.wonderland.client.BaseClientPlugin;
import org.jdesktop.wonderland.client.cell.Cell;
import org.jdesktop.wonderland.client.content.ContentExportManager;
import org.jdesktop.wonderland.client.content.ContentImportManager;
import org.jdesktop.wonderland.common.annotation.Plugin;

/**
 *
 * @author spcworld
 */
@Plugin
public class SubsnapshotClientPlugin extends BaseClientPlugin {

    SubsnapshotContentImporter importer = null;
    SubsnapshotExporter exporter = null;
    
    @Override
    public void activate() {
        exporter = SubsnapshotExporter.getInstance();
        importer = new SubsnapshotContentImporter();
        ContentImportManager.getContentImportManager().registerContentImporter(importer);
        ContentExportManager.INSTANCE.registerContentExporter(exporter);
        ContentExportManager.INSTANCE.setDefaultContentExporter(exporter);
    }

    @Override
    public void deactivate() {
        ContentImportManager.getContentImportManager().unregisterContentImporter(importer);
        ContentExportManager.INSTANCE.unregisterContentExporter(exporter);
        importer = null;
        exporter = null;
    }
    
}
