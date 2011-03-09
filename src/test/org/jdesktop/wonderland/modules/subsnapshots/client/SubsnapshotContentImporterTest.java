package org.jdesktop.wonderland.modules.subsnapshots.client;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.File;
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
        //wlcontent://users/Ryan/Nicole/art/TeamRoomFloor2.kmz.dep
        String input = "<deployedModelURL>wlcontent://user@AA.BB.CC.DD/Nicole/art/TeamRoomFloor2.kmz.dep</deployedModelURL>";
        String expected = "<deployedModelURL>wlcontent://users/Ryan/Nicole/art/TeamRoomFloor2.kmz.dep</deployedModelURL>";

        importer = new SubsnapshotContentImporter();
        assertEquals(expected, importer.updateURI(input, "Ryan"));
    }

    @Test
    public void testProcessFile() {
        String input = "";
        String output = " ";
        String localPath = System.getProperty("file.separator") +
            getClass().getProtectionDomain().getCodeSource().getLocation().toString().substring(6) +
            ".." + System.getProperty("file.separator") + ".." + System.getProperty("file.separator");
        String inFilePath = localPath + "testFiles"+ System.getProperty("file.separator") +"testDep.dep";
        String outFilePath = localPath + "testFiles"+ System.getProperty("file.separator") + "testDepOut.dep";
        File inputFile = new File(inFilePath);
        File outputFile = new File(outFilePath);

        importer = new SubsnapshotContentImporter();
        try {
            InputStream testStream = importer.processFile(inputFile, "wldev");

            BufferedReader expected = new BufferedReader(new FileReader(outputFile));
            BufferedReader actual = new BufferedReader(new InputStreamReader(testStream));
            String expectedString ;
            String actualString;
            do {
                expectedString = expected.readLine();
                actualString = actual.readLine();
                assertEquals(expectedString, actualString);
            } while(expectedString != null && actualString != null);
            assertNull(expectedString);
            assertNull(actualString);
            expected.close();
            actual.close();

        } catch(Exception e) {
            fail(e.toString());
        }
    }
}