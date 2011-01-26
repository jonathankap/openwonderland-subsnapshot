/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jdesktop.wonderland.modules.subsnapshots.client;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author WonderlandWednesday
 */
public class ModelCellExporter extends SubsnapshotExporter {


    @Override
    protected List<String> getListOfContent(String s) {
        List<String> uriList = super.getListOfContent(s);
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
            //LOGGER.fine(s1);
        }

        return uriList;
    }

    List<String> extractDepFiles(String s){
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
}
