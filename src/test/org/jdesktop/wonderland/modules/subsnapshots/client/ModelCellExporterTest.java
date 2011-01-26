/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jdesktop.wonderland.modules.subsnapshots.client;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author spcworld
 */
public class ModelCellExporterTest {


    @Before
    public void setUp() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //@Test
    
    
    @Test
    public void testExtractDepFile(){
        ModelCellExporter modelExporter = new ModelCellExporter();
        List<String> outcome = modelExporter.extractDepFiles("<ModelURL>wlcontent://users/Jonathan/art/Robot.kmz/Robot.kmz.dep</ModelURL>");
        List<String> expected = new ArrayList<String>();
        expected.add("//users/Jonathan/art/Robot.kmz/");

        assertEquals(expected.get(0),outcome.get(0));

    }


    // public void hello() {}

}