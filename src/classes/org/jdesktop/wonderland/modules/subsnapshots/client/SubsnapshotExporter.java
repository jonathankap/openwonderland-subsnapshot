/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jdesktop.wonderland.modules.subsnapshots.client;

import com.jme.math.Vector3f;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jdesktop.wonderland.client.cell.Cell;
import org.jdesktop.wonderland.client.cell.ModelCell;
import org.jdesktop.wonderland.client.cell.asset.AssetUtils;
import org.jdesktop.wonderland.common.cell.messages.CellServerStateRequestMessage;
import org.jdesktop.wonderland.common.cell.messages.CellServerStateResponseMessage;
import org.jdesktop.wonderland.common.cell.state.CellServerState;
import org.jdesktop.wonderland.common.cell.state.CellServerStateFactory;
import org.jdesktop.wonderland.common.cell.state.PositionComponentServerState;
import org.jdesktop.wonderland.common.messages.ResponseMessage;
import org.jdesktop.wonderland.common.utils.ScannedClassLoader;

/**
 *
 * @author WonderlandWednesday
 */
public class SubsnapshotExporter {
    private static final Logger LOGGER = Logger.getLogger(SubsnapshotExporter.class.getName());
    private static final ResourceBundle bundle =
            ResourceBundle.getBundle("org/jdesktop/wonderland/modules/subsnapshots/client/resources/Bundle");

    public static SubsnapshotExporter getInstance(Cell cell) {
    //TODO find appropriate instance of Exporter for cell
        if (cell instanceof ModelCell){
            return new ModelCellExporter();
        }
        return new SubsnapshotExporter();
    }
    /**
     *       / origin should be supplied for top level cells to be exported
    */
    public void exportCell(Cell cell, Vector3f origin) {

        ResponseMessage rm = cell.sendCellMessageAndWait(
                //CellServerState requst message here.
                new CellServerStateRequestMessage(cell.getCellID()));
        if (rm == null) {
            //    return null;
        }
        CellServerStateResponseMessage stateMessage = (CellServerStateResponseMessage) rm;
        CellServerState state = stateMessage.getCellServerState();

        if (origin != null) {
                // normalize the location

          PositionComponentServerState position = (PositionComponentServerState)state.getComponentServerState(PositionComponentServerState.class);
          if (position == null) {
              position = new PositionComponentServerState();
          }

          Vector3f translation = cell.getWorldTransform().getTranslation(null);
          translation.subtractLocal(origin);
          position.setTranslation(translation);
          state.addComponentServerState(position);
        }
        StringWriter sWriter = new StringWriter();
        try {
            ScannedClassLoader loader =
                    cell.getCellCache().getSession().getSessionManager().getClassloader();

            state.encode(sWriter, CellServerStateFactory.getMarshaller(loader));
            LOGGER.fine(sWriter.getBuffer() + "");
            String s = sWriter.getBuffer().toString();

//            List <String> uriList = new ArrayList();
            List<String> uriList = getListOfContent(s);

            File rootDir = File.createTempFile("subsnapshot", "tmp");
            rootDir.delete();
            rootDir.mkdir();
            downloadContent(rootDir, uriList);
            writeServerState(rootDir, s, cell, state);
            File f = getOutputFile();
            if (f != null) {
                createPackage(rootDir, f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //not complete
    }

    protected void createPackage(File rootDir, File outputFile)
            throws FileNotFoundException, IOException {
        ZipOutputStream zStream = new ZipOutputStream(new FileOutputStream(outputFile));
        File[] list = rootDir.listFiles();
        for (int i = 0; i < list.length; i++) {
            File file = list[i];
            addToZip(file, zStream, "");
        }

        zStream.close();
    }

    private void addToZip(File file, ZipOutputStream zStream, String parentDir) throws IOException {
        if (file.isDirectory()) {
            String dir = parentDir + file.getName() + "/";
            ZipEntry zEntry = new ZipEntry(dir);
            zStream.putNextEntry(zEntry);
            zStream.closeEntry();
            File[] list = file.listFiles();
            for (int i = 0; i < list.length; i++) {
                File fileInDir = list[i];
                addToZip(fileInDir, zStream, dir); // drills down into the next level of the directory
            }

        } else {
            // just a file
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry zEntry = new ZipEntry(parentDir + file.getName());
            zStream.putNextEntry(zEntry);
            byte[] bArray = new byte[2048];
            int count = 0;
            while ((count = bis.read(bArray, 0, 2048)) > -1) {
                zStream.write(bArray, 0, count);
            }
            bis.close();
            zStream.closeEntry();
        }
    }

    protected File getOutputFile() {
        JFileChooser jChooser = new JFileChooser();
        jChooser.setFileFilter(new FileNameExtensionFilter(bundle.getString("Wonderland_Export_File"), "wlexport"));
        int i = jChooser.showSaveDialog(null);

        if (i == JFileChooser.APPROVE_OPTION) {
            String fileWithExtensionAdded = jChooser.getSelectedFile() + ".wlexport";
            return new File(fileWithExtensionAdded);
        }
        return null;
    }

    protected void writeServerState(File rootDir, String s, Cell cell, CellServerState state)
            throws IOException {

        //CellServerStatenumbers
        String stateFile = "server-states/" + state.getName() + cell.getCellID();
        File serverState = new File(rootDir, stateFile);

        serverState.getParentFile().mkdirs();

        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(serverState)));
        pw.println(s);
        pw.close();


    }

    protected void downloadContent(File rootDir, List<String> uriList) {

        for (String uri : uriList) {
            String newName = uri.replace("wlcontent://users", "");
            newName = "content/" + newName; //content/bob
            File content = new File(rootDir, newName);

            // create all necessary parent directories
            content.getParentFile().mkdirs();



            try {
                URL url = AssetUtils.getAssetURL(uri);
                doDownloadContent(url.openStream(), new FileOutputStream(content));
            } catch (IOException ioe) {
                //TODO this should be logged properly!
                LOGGER.info("having technical difficulties saving your URI...\n"
                        + ioe.getMessage());

            }
        }
    }

    protected void doDownloadContent(InputStream instream, OutputStream outstream)
            throws IOException {
        byte[] buffer = new byte[16 * 1024];

        int read;
        while ((read = instream.read(buffer)) > 0) {
            outstream.write(buffer, 0, read);
        }

        outstream.flush();
    }

    protected List<String> getListOfContent(String s) {
        List<String> uriList = new ArrayList();
        int uri0 = 0;
        int uri1 = 0;

        while (true) {
            uri0 = s.indexOf("wlcontent:", uri1);
            if (uri0 == -1) {
                break;
            }
            uri1 = s.indexOf("</", uri0);
            if (uri1 == -1) {
                break;
            }
            String s1 = s.substring(uri0, uri1);
            uriList.add(s1);
            LOGGER.fine(s1);
        }

        return uriList;
    }
}
