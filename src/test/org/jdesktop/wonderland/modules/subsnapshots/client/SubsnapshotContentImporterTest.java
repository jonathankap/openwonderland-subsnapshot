package org.jdesktop.wonderland.modules.subsnapshots.client;

import org.jdesktop.wonderland.common.cell.CellTransform;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
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
    
   @Test
    public void testApplyRelativeTransform() {
       Vector3f avatarTranslation = new Vector3f(0, 0, 0);
         Quaternion avatarRotation = new Quaternion(new float[] { 0f, 0f, 0f });
        CellTransform avatarTransform = new CellTransform(avatarRotation,
                                                          avatarTranslation);

        Vector3f objectTranslation = new Vector3f(0, 0, 10f);
        Quaternion objectRotation = new Quaternion(new float[] { 0f, 0f, 0f });
        CellTransform objectTransform = new CellTransform(objectRotation,
                                                          objectTranslation);

        Vector3f resultTranslation = new Vector3f(0, 0, 10f);
        Quaternion resultRotation = new Quaternion(new float[] { 0f, 0f, 0f });
        CellTransform resultTransform = new CellTransform(resultRotation,
                                                          resultTranslation);

        CellTransform out =  importer.applyRelativeTransform(avatarTransform, objectTransform);
        assertTrue(out.epsilonEquals(resultTransform));

        
        
        // object is still 10 meters in front of the avatar, so the
        // result should be the same
        avatarTranslation = new Vector3f(0, 0, 0);
        avatarRotation = new Quaternion(new float[] { 0f, (float) Math.toRadians(90), 0f });
        avatarTransform = new CellTransform(avatarRotation, avatarTranslation);

        objectTranslation = new Vector3f(0, 0, 10f);
        objectRotation = new Quaternion(new float[] { 0f, 0f, 0f });
        objectTransform = new CellTransform(objectRotation, objectTranslation);

        resultTranslation = new Vector3f(10f, 0, 0);
        resultRotation = new Quaternion(new float[] { 0f, (float) Math.toRadians(90), 0f });
        resultTransform = new CellTransform(resultRotation, resultTranslation);

        out = importer.applyRelativeTransform(avatarTransform, objectTransform);

        System.out.println("avatar " + avatarTransform);
        System.out.println("object " + objectTransform);
        
        float[] oa = out.getRotation(null).toAngles(null);
        float[] ga = resultTransform.getRotation(null).toAngles(null);
        
        System.out.println("out    " + out + " " + oa[0] + " " + oa[1] + " " + oa[2]);
        System.out.println("goal   " + resultTransform + " " + ga[0] + " " + ga[1] + " " + ga[2]);
        System.out.println("Result: " + out.epsilonEquals(resultTransform));
        assertTrue(out.epsilonEquals(resultTransform));

        // NOT YET IMPLEMENTED
        // object is rotated also
//        fail("Not Yet Implemented");
        // object is still 10 meters in front of the avatar, so the
        // result should be the same
        avatarTranslation = new Vector3f(0, 0, 0);
        avatarRotation = new Quaternion(new float[] { 0f, (float) Math.toRadians(90), 0f });
        avatarTransform = new CellTransform(avatarRotation, avatarTranslation);

        objectTranslation = new Vector3f(0, 0, 10f);
        objectRotation = new Quaternion(new float[] { 0f, (float)Math.toRadians(-90), 0f });
        objectTransform = new CellTransform(objectRotation, objectTranslation);

        resultTranslation = new Vector3f(10f, 0, 0);
        resultRotation = new Quaternion(new float[] { 0f, 0f, 0f });
        resultTransform = new CellTransform(resultRotation, resultTranslation);

        out = importer.applyRelativeTransform(avatarTransform, objectTransform);
        assertTrue(out.epsilonEquals(resultTransform));
    }
   
    
}