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
public class SubsnapshotArchiveTest {
    
    private SubsnapshotArchive archive;
    private File fileToImport = new File("testFiles/fileToImport");

    @Before
    public void setUp() {
        archive = new SubsnapshotArchive(fileToImport);
    }

    @After
    public void tearDown() {
    }

    //TODO Add tests here!
    @Test
    public void testAddToZip() throws IOException{
    }

}