package org.jdesktop.wonderland.modules.subsnapshots.client;

import org.jdesktop.wonderland.common.cell.CellTransform;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
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
public class SubsnapshotExporterTest {
    
    private SubsnapshotExporter subExporter;
    private String localPath = System.getProperty("file.separator") +
            getClass().getProtectionDomain().getCodeSource().getLocation().toString().substring(6) +
            ".." + System.getProperty("file.separator") + ".." +System.getProperty("file.separator");

    @Before
    public void setUp() {
        subExporter = SubsnapshotExporter.getInstance(null);
    }

    @After
    public void tearDown() {
    }

    /**
     * This method uses hardcoded paths. Please modify appropriately.
     * @throws IOException
     */
    @Test
    public void testAddToZip() throws IOException{

        File file = new File( localPath + "testFiles/secondLevelFolder/thirdLevelFolder/");
        File outFile = File.createTempFile("testOneFile", ".zip");
        System.out.println("Files are; fileToZip: " + file + " File to write to: " + outFile);
        subExporter.createPackage(file, outFile);


        file = new File( localPath + "testFiles");
        outFile = File.createTempFile("testDirFile", ".zip");
        System.out.println("Files are; fileToZip: " + file + " File to write to: " + outFile);
        subExporter.createPackage(file, outFile);

    }

    @Test
    public void testExtractDirectory() {
        String input = "wlcontent://users@199.17.224.225:8080/Jonathan/art";

        String result = subExporter.extractDirectory(input);
        assertEquals("/Jonathan/art", result);

        input = "wlcontent://users/Jonathan/art";
        result = subExporter.extractDirectory(input);
        assertEquals("/Jonathan/art", result);
    }

    @Test
    public void testGetRelativeTransform() {
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

        CellTransform out = subExporter.getRelativeTransform(avatarTransform, objectTransform);
        assertTrue(out.epsilonEquals(resultTransform));

        // object is still 10 meters in front of the avatar, so the
        // result should be the same
        avatarTransform.setRotation(new Quaternion(new float[] { 0f, (float) Math.toRadians(90), 0f}));
        objectTransform.setTranslation(new Vector3f(10f, 0, 0));
        objectTransform.setRotation(new Quaternion(new float[] { 0f, (float) Math.toRadians(90), 0f}));
        out = subExporter.getRelativeTransform(avatarTransform, objectTransform);

        System.out.println("avatar " + avatarTransform);
        System.out.println("object " + objectTransform);
        
        float[] oa = out.getRotation(null).toAngles(null);
        float[] ga = resultTransform.getRotation(null).toAngles(null);
        
        System.out.println("out    " + out + " " + oa[0] + " " + oa[1] + " " + oa[2]);
        System.out.println("goal   " + resultTransform + " " + ga[0] + " " + ga[1] + " " + ga[2]);
        System.out.println("Result: " + out.epsilonEquals(resultTransform));
        assertTrue(out.epsilonEquals(resultTransform));

        // object is rotated also
        objectTransform.setRotation(new Quaternion(new float[] { 0f, 0f, 0f}));
        resultTransform.setRotation(new Quaternion(new float[] { 0f, (float) Math.toRadians(-90), 0f}));
        out = subExporter.getRelativeTransform(avatarTransform, objectTransform);
        System.out.println("Avatar = " + avatarTransform + " object = " + objectTransform +
                           " out = " + out);
        assertTrue(out.epsilonEquals(resultTransform));
    }

}
