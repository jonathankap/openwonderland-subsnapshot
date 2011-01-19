/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jdesktop.wonderland.modules.subsnapshots.client.spi;

import java.util.List;
import org.jdesktop.wonderland.client.cell.Cell;
import org.jdesktop.wonderland.client.cell.CellComponent;
import org.jdesktop.wonderland.common.cell.state.CellComponentServerState;
import org.jdesktop.wonderland.common.cell.state.CellServerState;

/**
 *
 * @author WonderlandWednesday
 */
public interface CustomExporterSPI {
    /**
     * Returns custom URIs (as strings) for complex cells
     * @param cell
     * @param serverState
     * @return list of strings
     */
    public List<String> getListOfContent(Cell cell, CellServerState serverState);
    /**
     * Returns custom URIs (as strings) for complex cell components
     * @param component
     * @param serverState
     * @return list of strings
     */
    public List<String> getListOfContent(CellComponent component,
                                        CellComponentServerState serverState);
}
