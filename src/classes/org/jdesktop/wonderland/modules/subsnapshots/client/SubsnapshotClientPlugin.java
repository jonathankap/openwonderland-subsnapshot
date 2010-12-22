/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jdesktop.wonderland.modules.subsnapshots.client;

import org.jdesktop.wonderland.client.BaseClientPlugin;
import org.jdesktop.wonderland.client.content.ContentImportManager;
import org.jdesktop.wonderland.common.annotation.Plugin;

/**
 *
 * @author spcworld
 */
@Plugin
public class SubsnapshotClientPlugin extends BaseClientPlugin {

    SubsnapshotContentImporter importer = null;
    @Override
    public void activate() {
        importer = new SubsnapshotContentImporter();
        ContentImportManager.getContentImportManager().registerContentImporter(importer);

    }

    @Override
    public void deactivate() {
        ContentImportManager.getContentImportManager().unregisterContentImporter(importer);
        importer = null;
    }
    
}
