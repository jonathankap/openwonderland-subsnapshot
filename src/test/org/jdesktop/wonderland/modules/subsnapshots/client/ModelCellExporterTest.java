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
    public void testExtractDepDirectories(){
        ModelCellExporter modelExporter = new ModelCellExporter();
        List<String> outcome = modelExporter.extractDepDirectories("<ModelURL>wlcontent://users/Jonathan/art/Robot.kmz/Robot.kmz.dep</ModelURL>");
        List<String> expected = new ArrayList<String>();
        expected.add("//users/Jonathan/art/Robot.kmz/");

        assertEquals(expected.get(0),outcome.get(0));

    }

 @Test
 public void testExtractDirectoyComponents() {
     // //users@199.17.224.245:8080/Jonathan/art/Robot.kmz/
     String s = "//users/Jonathan/art/Robot.kmz/";

     String [] ss = s.split("/");

     assertEquals("should be users","users",ss[2]);
 //    assertEquals("should have 6 elements",6,ss.length);

     for (String s1:ss) {
         System.out.println (s1);
     }

 }
    // public void hello() {}

}