package org.jdesktop.wonderland.modules.subsnapshots.client;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jos
 */
public class SubsnapshotExporterTest {
    
    private SubsnapshotExporter f;
    private String localPath = System.getProperty("file.separator") +
            getClass().getProtectionDomain().getCodeSource().getLocation().toString().substring(6) +
            ".." + System.getProperty("file.separator") + ".." +System.getProperty("file.separator");

    @Before
    public void setUp() {
        f = SubsnapshotExporter.getInstance(null);
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
        f.createPackage(file, outFile);


        file = new File( localPath + "testFiles");
        outFile = File.createTempFile("testDirFile", ".zip");
        System.out.println("Files are; fileToZip: " + file + " File to write to: " + outFile);
        f.createPackage(file, outFile);

    }

}
