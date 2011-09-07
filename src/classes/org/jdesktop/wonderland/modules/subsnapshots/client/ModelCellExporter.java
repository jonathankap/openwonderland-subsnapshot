/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jdesktop.wonderland.modules.subsnapshots.client;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.wonderland.client.cell.Cell;
import org.jdesktop.wonderland.client.comms.WonderlandSession;
import org.jdesktop.wonderland.client.jme.ClientContextJME;
import org.jdesktop.wonderland.modules.contentrepo.client.ContentRepository;
import org.jdesktop.wonderland.modules.contentrepo.client.ContentRepositoryRegistry;
import org.jdesktop.wonderland.modules.contentrepo.common.ContentCollection;
import org.jdesktop.wonderland.modules.contentrepo.common.ContentNode;
import org.jdesktop.wonderland.modules.contentrepo.common.ContentRepositoryException;
import org.jdesktop.wonderland.modules.contentrepo.common.ContentResource;
import org.jdesktop.wonderland.modules.subsnapshots.client.spi.CustomExporterSPI;

/**
 *
 * @author WonderlandWednesday
 */
public class ModelCellExporter implements CustomExporterSPI {




    
    public List<String> getListOfContent(String s) {
        List<String> uriList = new ArrayList<String>();
        List <String> depDirectories = extractDepDirectories(s);

        for (String depDirectory : depDirectories) {
            uriList.addAll(getListOfContentForDepDirectory(depDirectory));
        }

        return uriList;
    }

    List<String> extractDepDirectories(String s){
         List<String> depList = new ArrayList<String>();
        int uri0 = 0;
        int uri1 = 0;

        // <ModelURL>wlcontent://users/Jonathan/art/Robot.kmz/Robot.kmz.dep</ModelURL>

        while (true) {
            uri0 = s.indexOf("wlcontent:", uri1);
            if (uri0 == -1) {
                break;
            }
            uri1 = s.indexOf(".dep</", uri0);
            if (uri1 == -1) {
                break;
            }
            String s1 = s.substring(uri0, uri1);
            
            int lastSlash = s1.lastIndexOf("/");
            s1 = s1.substring("wlcontent:".length(), lastSlash + 1);


            depList.add(s1);
            //LOGGER.fine(s1);
        }

        return depList;
    }
    /**
     * 
     * @param s in the form //users/Jonathan/art/Robot.kmz/
     * @return
     */
     List <String> getListOfContentForDepDirectory(String s){

        Cell cell = ClientContextJME.getViewManager().getPrimaryViewCell();
        WonderlandSession session = cell.getCellCache().getSession();
        ContentRepositoryRegistry registry = ContentRepositoryRegistry.getInstance();
        ContentRepository repo = registry.getRepository(session.getSessionManager());

        List <String> list = new ArrayList<String>();

        String [] directoryComponents = s.split("/");
        String user = directoryComponents[3];
        try {
            ContentCollection userContent = repo.getUserRoot(user);
            for (int i=4; i<directoryComponents.length;i++){
              userContent = (ContentCollection) userContent.getChild(directoryComponents[i]);
            }
            collectContents(list, userContent, "wlcontent:"+ s);
        } catch (ContentRepositoryException ex) {
            Logger.getLogger(ModelCellExporter.class.getName()).log(Level.SEVERE, null, ex);
            return list;
        }

         return list;

     }

     void collectContents(List <String> list, ContentCollection content, String  prefix) throws ContentRepositoryException {

         for (ContentNode node: content.getChildren()){
             if (node instanceof ContentCollection) {
                 collectContents(list, (ContentCollection) node, prefix + node.getName() + "/");
             } else if(node instanceof ContentResource)  {
                 list.add (prefix + node.getName());
             }

         }

     }
}
