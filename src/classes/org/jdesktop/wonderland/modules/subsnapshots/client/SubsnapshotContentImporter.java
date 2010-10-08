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

        //2) Recreate server state from xml

        //3) Create cells from server states
        return new String("");
    }

    public void unpackArchive() { }

    public void uploadResources() { }

    public void restoreServerStates(File[] files) { }

    public void createCells(CellServerState[] serverStates) { }

}
