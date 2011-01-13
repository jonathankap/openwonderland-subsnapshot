package org.jdesktop.wonderland.modules.subsnapshots.client;

import java.util.List;
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

    private String rootDir = localPath + "testFiles" + System.getProperty("file.separator") +
                "exportedFileUnzipped";

    SubsnapshotArchive sa;

    @Before
    public void setUp() {
        sa = new SubsnapshotArchive();
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


    @Test
    public void testFindContent(){
        List<File> list = sa.findContent(new File(rootDir));
        assertTrue(list.size() == 3);

    }

    @Test
    public void testFindServerStates(){

        List<File> list = sa.findServerStates(new File(rootDir));
        assertTrue(list.size() == 2);

    }

}