/*
 * Copyright (C) 2013 Max Planck Institute for Psycholinguistics
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.mpi.metadatabrowser.model.cmdi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.*;
import org.apache.wicket.util.time.Time;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class DownloadActionRequestTest {
    
    public DownloadActionRequestTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }


    /**
     * Test of setFileName method, of class DownloadActionRequest.
     */
    @Test
    public void testSetFileName() {
        System.out.println("setFileName");
        String name = "1.imdi";
        DownloadActionRequest.setFileName(name);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of setStreamContent method, of class DownloadActionRequest.
     */
    @Test
    public void testSetStreamContent() throws URISyntaxException {
        System.out.println("setStreamContent");
        URI nodeUri = new URI("/corpora/lams_demo/Corpusstructure/1.imdi");
        File file = new File(nodeUri.getPath());
        IResourceStream resStream = new FileResourceStream(file);
        DownloadActionRequest instance = new DownloadActionRequest();
        instance.setStreamContent(resStream);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getDownloadStream method, of class DownloadActionRequest.
     */
    @Test
    public void testGetDownloadStream() throws ResourceStreamNotFoundException, URISyntaxException {
        System.out.println("getDownloadStream");
        URI nodeUri = getClass().getClassLoader().getResource("IPROSLA_Nijmegen.cmdi").toURI();
        File file = new File(nodeUri.getPath());
        IResourceStream resStream = new FileResourceStream(file);
        DownloadActionRequest instance = new DownloadActionRequest("IPROSLA_Nijmegen", resStream);
        IResourceStream result = instance.getDownloadStream();
        System.out.println(instance.getFileName());
        assertNotNull(result);
        assertNotNull(result.getInputStream());
        assertNull(result.getContentType());
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getFileName method, of class DownloadActionRequest.
     */
    @Test
    public void testGetFileName() {
        System.out.println("getFileName");
        DownloadActionRequest instance = new DownloadActionRequest();
        String expResult = "1.imdi";
        String result = instance.getFileName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }
}
