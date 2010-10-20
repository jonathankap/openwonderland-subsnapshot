/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jdesktop.wonderland.modules.subsnapshots.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Class to contain subsnapshot contents.
 * @author spcworld
 */
public class SubsnapshotArchive {
    private static final Logger LOGGER =
            Logger.getLogger(SubsnapshotArchive.class.getName());

    List<File> resources = null;
    List<File> serverStates = null;
    private File archive = null;
    /**
     * Process through the file contents in the root directory and place
     * resources and serverStates in the appropriate structures.
     * @param rootDir
     */
    public SubsnapshotArchive(File archive) {
        this.archive = archive;
        unpackArchive();

        
    }


    /**
     * Under construction.
     *
     * @param resources
     * @param serverStates
     */
    public SubsnapshotArchive(List<File> resources, List<File> serverStates) {
        this.resources = resources;
        this.serverStates = serverStates;
    }

    public void unpackArchive() {


    }

    static void unzipFile(File fileToUnzip, String destination) throws IOException {
        File destinationFile = new File(destination);
        destinationFile.mkdirs();

        // unzip fileToUnzip into destination
        final int BUFFER = 2048;
        BufferedOutputStream dest = null;
        FileInputStream streamToZipFile = new FileInputStream(fileToUnzip);
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(streamToZipFile));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            if (entry.isDirectory()) {
                File dir = new File(destination + entry.getName());
                dir.mkdirs();
            }

            int count;
            byte data[] = new byte[BUFFER];
            // write the files to disk
            FileOutputStream fos = new FileOutputStream(destination + entry.getName());
            dest = new BufferedOutputStream(fos, BUFFER);
            while ((count = zis.read(data, 0, BUFFER)) != -1) {
                dest.write(data, 0, count);
            }
            dest.flush();
            dest.close();
        }
        zis.close();
    }


    /**
     * Method to return resources contained in archive
     * @return list of resource files
     */
    public List<File> getResources() {
        return this.resources;
    }

    /**
     * Method to return server states in archive
     * @return list of server state files
     */
    public List<File> getServerStates() {
        return this.serverStates;
    }
}
