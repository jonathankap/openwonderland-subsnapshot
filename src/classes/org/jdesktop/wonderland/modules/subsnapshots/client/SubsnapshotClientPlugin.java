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

import org.jdesktop.wonderland.client.BaseClientPlugin;
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
        SubsnapshotExporter exporter = SubsnapshotExporter.getInstance();
        importer = new SubsnapshotContentImporter();
        ContentImportManager.getContentImportManager().registerContentImporter(importer);
        ContentExportManager.INSTANCE.registerContentExporter(exporter);

    }

    @Override
    public void deactivate() {
        ContentImportManager.getContentImportManager().unregisterContentImporter(importer);
        ContentExportManager.INSTANCE.unregisterContentExporter(exporter);
        importer = null;
        exporter = null;
    }
    
}
