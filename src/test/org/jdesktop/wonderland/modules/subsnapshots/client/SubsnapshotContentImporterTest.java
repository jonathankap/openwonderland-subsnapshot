package org.jdesktop.wonderland.modules.subsnapshots.client;

import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jos
 */
public class SubsnapshotContentImporterTest {
    
    private SubsnapshotContentImporter importer;

    @Before
    public void setUp() {
        importer = new SubsnapshotContentImporter();
    }

    @After
    public void tearDown() {
    }

    //TODO Add tests here!
    @Test
    public void testAddToZip() throws IOException{
    }

    @Test
    public void testUpdateURI() {
        //get
        //wlcontent://users@AA.BB.CC.DD/Nicole/art/TeamRoomFloor2.kmz.dep

        //put
        //wlcontent://users/Ryan/art/TeamRoomFloor2.kmz.dep
        String input = "<deployedModelURL>wlcontent://user@AA.BB.CC.DD/Nicole/art/TeamRoomFloor2.kmz.dep</deployedModelURL>";
        String expected = "<deployedModelURL>wlcontent://users/Ryan/art/TeamRoomFloor2.kmz.dep</deployedModelURL>";

        importer = new SubsnapshotContentImporter();
        assertEquals(expected, importer.updateURI(input, "Ryan"));
    }
}