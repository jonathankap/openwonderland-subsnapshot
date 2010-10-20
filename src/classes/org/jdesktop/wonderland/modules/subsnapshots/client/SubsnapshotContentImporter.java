/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jdesktop.wonderland.modules.subsnapshots.client;

import java.io.File;
import org.jdesktop.wonderland.client.content.spi.ContentImporterSPI;
import org.jdesktop.wonderland.common.cell.state.CellServerState;

/**
 *
 * @author spcworld
 */
public class SubsnapshotContentImporter implements ContentImporterSPI {

    public String[] getExtensions() {
        return  new String[] {".wlexport"};
    }

    public String importFile(File file, String extension) {
        //1) Unpackage the .wlexport archive
            //Unpack into temporary directory
            //upload resources to server
        File dir = null;
        dir = unpackArchive(file);
        uploadResources(dir);
        //2) Recreate server state from xml


        //3) Create cells from server states
        return new String("");
    }

    public File unpackArchive(File file) {

        return new File("");
    }

    public void uploadResources(File dir) { }

    public void restoreServerStates(File[] files) { }

    public void createCells(CellServerState[] serverStates) { }

}
