/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jdesktop.wonderland.modules.subsnapshots.client;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
//import java.lang.reflect.Type;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.jdesktop.wonderland.client.cell.Cell;
import org.jdesktop.wonderland.client.cell.CellEditChannelConnection;
import org.jdesktop.wonderland.client.cell.utils.CellPlacementUtils;
import org.jdesktop.wonderland.client.comms.WonderlandSession;
import org.jdesktop.wonderland.client.content.spi.ContentImporterSPI;
import org.jdesktop.wonderland.client.jme.ClientContextJME;
import org.jdesktop.wonderland.client.jme.ViewManager;
import org.jdesktop.wonderland.client.jme.utils.ScenegraphUtils;
import org.jdesktop.wonderland.client.login.LoginManager;
import org.jdesktop.wonderland.client.login.ServerSessionManager;
import org.jdesktop.wonderland.common.cell.CellEditConnectionType;
import org.jdesktop.wonderland.common.cell.CellTransform;
import org.jdesktop.wonderland.common.cell.messages.CellCreateMessage;
import org.jdesktop.wonderland.common.cell.state.CellServerState;
import org.jdesktop.wonderland.common.cell.state.CellServerStateFactory;
import org.jdesktop.wonderland.common.cell.state.PositionComponentServerState;
import org.jdesktop.wonderland.common.utils.ScannedClassLoader;
import org.jdesktop.wonderland.modules.contentrepo.client.ContentRepository;
import org.jdesktop.wonderland.modules.contentrepo.client.ContentRepositoryRegistry;
import org.jdesktop.wonderland.modules.contentrepo.common.ContentCollection;
import org.jdesktop.wonderland.modules.contentrepo.common.ContentNode;
import org.jdesktop.wonderland.modules.contentrepo.common.ContentRepositoryException;
import org.jdesktop.wonderland.modules.contentrepo.common.ContentResource;
import org.jdesktop.wonderland.modules.contentrepo.common.ContentNode.Type;

/**
 *
 * @author spcworld
 */
public class SubsnapshotContentImporter implements ContentImporterSPI {

    private static final Logger LOGGER =
            Logger.getLogger(SubsnapshotContentImporter.class.getName());
    public String[] getExtensions() {
        return  new String[] {"wlexport"};
    }

    public String importFile(File file, String extension) {
        return importFile(file, extension, true);
    }

    public String importFile(File file, String extension, boolean createCells) {
        //1) Unpackage the .wlexport archive
            //Unpack into temporary directory
            //upload resources to server

        SubsnapshotArchive archive = new SubsnapshotArchive();
        try {
            archive.unpackArchive(file);

        } catch(IOException e) {
            LOGGER.severe("Error processing archive.");
            e.printStackTrace();
        }

        //dir = unpackArchive(file);
        uploadContent(archive);
        //2) Recreate server state from xml

         List <CellServerState> serverStates = restoreServerStates(archive);
        //3) Create cells from server states

         if (createCells) {
             createCells(serverStates);
         }
        return new String("");
    }

    public void uploadContent(SubsnapshotArchive archive) {
        Cell cell = ClientContextJME.getViewManager().getPrimaryViewCell();
        WonderlandSession session = cell.getCellCache().getSession();
        ContentRepositoryRegistry registry = ContentRepositoryRegistry.getInstance();
        ContentRepository repo = registry.getRepository(session.getSessionManager());

        ContentCollection userRoot;

        // First try to find the resource, if it exists, then simply upload the
        // new bits. Otherwise create the resource and upload the new bits

        //@TODO something should be done about file.getName() to make sure our
        //archive's resources get uploaded correctly even if they might have
        // duplicate names.

        try {
            //get content/user directory from .wonderland-server
            userRoot = repo.getUserRoot();

            //for each resource file in the archive...
            for(File file : archive.getContent()) {
                //grab directory pointer if available
                File fileRoot = new File(archive.getArchiveRoot(),"content");
                ContentCollection cDir = populateDirectories(userRoot,
                                                            fileRoot,
                                                            file);
                ContentNode node = (ContentNode)cDir.getChild(file.getName());
                if (node == null) {
                    //if not avaible, create it.

                    node = (ContentNode)cDir.createChild(file.getName(), Type.RESOURCE);
                }
                //do the heavy lifting.
            ((ContentResource)node).put(processFile(file, LoginManager.getPrimary().getUsername()));
            }

        } catch (ContentRepositoryException excp) {
            LOGGER.log(Level.WARNING, "Error uploading file in uploadResources()", excp);
            throw new RuntimeException();
            //throw new IOException("Error uploading file in uploadResources()");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error uploading file in uploadResources()", e);
            throw new RuntimeException();
        }
    }

    protected InputStream processFile(File file, String username) throws IOException {
        //handle ModelCoponent case
        if(file.getName().toLowerCase().endsWith(".dep")) {
            return processDEPFile(file, username);
        }

        return new FileInputStream(file);
    }

    protected InputStream processDEPFile(File file, String username) throws IOException {
        String fileContents = updateContentURIs(file, username);
        return new ByteArrayInputStream(fileContents.getBytes());
    }

    protected String updateContentURIs(File file, String username) throws IOException {
        BufferedReader reader = null;
        StringBuilder fileString = null;
        try {
            fileString = new StringBuilder();
            //StringBuffer fileString = new StringBuffer();
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                //line = line.replace("wlcontent://users", "wlcontent://users/" + LoginManager.getPrimary().getUsername());

                fileString.append(updateURI(line, username)).append("\n");
            }
            return fileString.toString();
        } finally {
            reader.close();
        }
    }

    protected ContentCollection populateDirectories(ContentCollection node, File root, File file)
    throws ContentRepositoryException {
       Deque<File> directories = new LinkedList();

       //get the initial directory
       file = file.getParentFile();
       //while file is not the root directory
       while(!file.equals(root)) {

          //add the current directory to the deque
          directories.push(file);

          file = file.getParentFile();
       }

       ContentCollection cc = node;
       while(!directories.isEmpty()) {

           String name = directories.pop().getName();
           ContentNode cn = cc.getChild(name);
           if(cn == null) {
                cc = (ContentCollection)cc.createChild(name, Type.COLLECTION);
           }
           else if(cn instanceof ContentCollection) {
               cc = (ContentCollection)cn;
           }
           else  {
               throw new ContentRepositoryException(name + " already exists");
           }
       }

       return cc;
    }

    public List restoreServerStates(SubsnapshotArchive archive) {
        // get unmarshaller
        ScannedClassLoader  loader =
                LoginManager.getPrimary().getClassloader();
        Unmarshaller unmarshaller = CellServerStateFactory.getUnmarshaller(loader);

        ArrayList <CellServerState> serverStates = new ArrayList <CellServerState>();
        // decode each file into a sever state

        for (File serverState: archive.getServerStates()){
            {
                try {
                String serverStateString = updateContentURIs(serverState, LoginManager.getPrimary().getUsername());

                    // ByteArrayInputStream stream = new ByteArrayInputStream(serverStateString.toString().getBytes());
                    // CellServerState state = (CellServerState) unmarshaller.unmarshal(stream);
                   CellServerState state = CellServerState.decode(
                       new StringReader(serverStateString),
                       unmarshaller);
 
                   serverStates.add(state);

                } catch (IOException ex) {
                    Logger.getLogger(SubsnapshotContentImporter.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JAXBException ex) {
                    Logger.getLogger(SubsnapshotContentImporter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return serverStates;
    }

    protected String updateURI(String text, String username) {
        int startIndex = text.indexOf("wlcontent://");
        if (startIndex == -1) {
            return text;
        }

        //get
        //wlcontent://users@AA.BB.CC.DD/Nicole/art/TeamRoomFloor2.kmz.dep

        //put
        //wlcontent://users/Ryan/Nicole/art/TeamRoomFloor2.kmz.dep
        int i1 = text.indexOf("/", startIndex + "wlcontent://".length() + 1);
        //i1 = text.indexOf("/", i1 + 1);

        return text.substring(0, startIndex) + "wlcontent://users/"+ username + text.substring(i1);

    }

    public void createCells(List <CellServerState> serverStates) {
        // ?? CellUtils.createCell(state)
        CellTransform avatar = ViewManager.getViewManager().getPrimaryViewCell().getWorldTransform();

        for (CellServerState state:serverStates) {
            try {
                //CellUtils.createCell(state);
                // normalize the location
                    //position should never be null.
                PositionComponentServerState position = (PositionComponentServerState)state.getComponentServerState(PositionComponentServerState.class);
                if (position == null) {
                    position = new PositionComponentServerState();
                }

                CellTransform object = new CellTransform(position.getRotation(), position.getTranslation(), position.getScaling().x);
                CellTransform applied = applyRelativeTransform(avatar, object);

                // set the position to the new position
                position.setTranslation(applied.getTranslation(null));
                position.setRotation(applied.getRotation(null));
                state.addComponentServerState(position);

                ServerSessionManager manager = LoginManager.getPrimary();
                WonderlandSession session = manager.getPrimarySession();
                CellEditChannelConnection connection = (CellEditChannelConnection)
                session.getConnection(CellEditConnectionType.CLIENT_TYPE);
                CellCreateMessage msg = new CellCreateMessage(null, state);
                connection.send(msg);

            } catch (Exception ex) {
                Logger.getLogger(SubsnapshotContentImporter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }
    protected CellTransform applyRelativeTransform(CellTransform avatar,
                                                 CellTransform object)
    {
        Vector3f objectTranslation = null;
        objectTranslation = object.getTranslation(null);


        Quaternion avatarRotation = avatar.getRotation(null);
        Vector3f rotatedTranslation = avatarRotation.mult(objectTranslation);
        rotatedTranslation.addLocal(avatar.getTranslation(null));
        //rotatedTranslation = translation we want to apply
        //rotatedRotation is the correct rotation we want to apply to object
        // in relation to the avatar
        Quaternion rotatedRotation = avatarRotation.mult(object.getRotation(null));

        return new CellTransform(rotatedRotation, rotatedTranslation);
        //return ScenegraphUtils.computeChildTransform(object, avatar);
    }
}
