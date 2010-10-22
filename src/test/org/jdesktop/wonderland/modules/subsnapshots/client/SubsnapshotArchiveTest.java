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
public class SubsnapshotArchiveTest {
    
    private String localPath = System.getProperty("file.separator") +
            getClass().getProtectionDomain().getCodeSource().getLocation().toString().substring(6) +
            ".." + System.getProperty("file.separator") + ".." + System.getProperty("file.separator");

    private File fileToImport = new File( localPath + "testFiles" +
            System.getProperty("file.separator") + "fileToUnzip.zip");

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void testUnzipFile() throws IOException{

        String writingTo = localPath + "testFiles" + System.getProperty("file.separator") +
                "unzippedFile" + System.getProperty("file.separator");
        SubsnapshotArchive.unzipFile(fileToImport, writingTo);
        String fileExpectedToBeUnzipped = writingTo + "file1.txt" ;
        assertTrue(new File(fileExpectedToBeUnzipped).isFile());
    }

}