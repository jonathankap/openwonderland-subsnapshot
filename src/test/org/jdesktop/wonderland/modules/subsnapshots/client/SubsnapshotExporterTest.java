package org.jdesktop.wonderland.modules.subsnapshots.client;

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

}
