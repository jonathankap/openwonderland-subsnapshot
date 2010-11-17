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
import java.util.ArrayList;
import java.util.Arrays;
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

    List<File> content = null;
    List<File> serverStates = null;
    private File archive = null;
    private File root = null;

    public SubsnapshotArchive(){
    }
    /**
     * Process through the file contents in the root directory and place
     * resources and serverStates in the appropriate structures.
     * @param rootDir
     */
    public SubsnapshotArchive(File archive) throws IOException {
        this.archive = archive;
        unpackArchive(archive);
    }


    /**
     * Under construction.
     *
     * @param content
     * @param serverStates
     */
    public SubsnapshotArchive(List<File> content, List<File> serverStates) {
        this.content = content;
        this.serverStates = serverStates;
    }

    public void unpackArchive(File archive) throws IOException {
        // create the destination
        File dest = File.createTempFile("wlexport", "tmp");
        root = dest;
        dest.delete();
        dest.mkdir();

        // unzip the archive file into destination
        unzipFile(archive, dest.getPath());

        // find resources
        this.content = findContent(dest);

        // find server states
        this.serverStates = findServerStates(dest);

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
                continue;
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
    public List<File> getContent() {
        return this.content;
    }

    /**
     * Method to return server states in archive
     * @return list of server state files
     */
    public List<File> getServerStates() {
        return this.serverStates;
    }

    /**
     * Find content in an unpacked directory
     * @param dest the directory the archive has been unpacked into
     * @return a list of content to upload to the server
     */
    List<File> findContent(File dest) {
        List<File> out = new ArrayList<File>();

        File contentDir = new File(dest, "content");
        addAllFiles(contentDir, out);

        return out;
    }

    /**
     * Recursively add all files in a directory to the list of output files
     * @param dir the directory to look at
     * @param list the files to add
     */
    private void addAllFiles(File dir, List<File> list) {
        for (File file : dir.listFiles()) {
            list.add(file);

            if (file.isDirectory()) {
                addAllFiles(file, list);
            }
        }
    }

    /**
     * Find server states in an unpacked directory
     * @param dest the directory the archive has been unpacked into
     * @return a list of server state objects
     */
    List<File> findServerStates(File dest) {
        File serverStatesDir = new File(dest, "server-states");
        List<File> out = Arrays.asList(serverStatesDir.listFiles());

        return out;
    }
    public File getArchiveRoot() {
        return root;
    }
}
