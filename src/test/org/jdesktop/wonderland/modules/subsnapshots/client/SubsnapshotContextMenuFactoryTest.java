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
public class SubsnapshotContextMenuFactoryTest {
    
    private SubsnapshotContextMenuFactory f;

    @Before
    public void setUp() {

        System.out.println("Trying to setup");
        f = new SubsnapshotContextMenuFactory();
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

        System.out.println("Trying to execute a test");

        File file = new File("/Users/jos/development/java/netbeansprojects/src/0.5/trunk/openwonderland-subsnapshot/testFiles/secondLevelFolder/thirdLevelFolder/");
        File outFile = File.createTempFile("testOneFile", ".zip");
        System.out.println("Files are; fileToZip: " + file + " File to write to: " + outFile);
        f.createPackage(file, outFile);


        file = new File("/Users/jos/development/java/netbeansprojects/src/0.5/trunk/openwonderland-subsnapshot/testFiles");
        outFile = File.createTempFile("testDirFile", ".zip");
        System.out.println("Files are; fileToZip: " + file + " File to write to: " + outFile);
        f.createPackage(file, outFile);

    }

}