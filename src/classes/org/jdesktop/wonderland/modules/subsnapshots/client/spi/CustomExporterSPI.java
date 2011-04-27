/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jdesktop.wonderland.modules.subsnapshots.client.spi;

import java.util.List;

/**
 *
 * @author WonderlandWednesday
 */
public interface CustomExporterSPI {

    /**
     * Extracts URIs of all external content
     * @param serverState as a string
     * @return a list of dependencies as string URIs
     */
    public List<String> getListOfContent(String serverState);

}
