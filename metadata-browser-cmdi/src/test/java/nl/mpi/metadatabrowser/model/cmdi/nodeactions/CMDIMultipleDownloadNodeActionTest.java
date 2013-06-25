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
package nl.mpi.metadatabrowser.model.cmdi.nodeactions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.DownloadActionRequest;
import nl.mpi.metadatabrowser.services.cmdi.ZipService;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIMultipleDownloadNodeActionTest {

    private final Mockery context = new JUnit4Mockery();
    
    public CMDIMultipleDownloadNodeActionTest() {
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
     * Test of getName method, of class CMDIMultipleDownloadNodeAction.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");

        CorpusStructureProvider csdb = context.mock(CorpusStructureProvider.class);
        ZipService zipService = context.mock(ZipService.class);
        CMDIMultipleDownloadNodeAction instance = new CMDIMultipleDownloadNodeAction(csdb, zipService);

        String expResult = "multidownload";
        String result = instance.getName();
        assertEquals(expResult, result);
    }
    private final static URI NODE_ID = URI.create("node:1");

    /**
     * Test of execute method, of class CMDIMultipleDownloadNodeAction.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");

        final CorpusStructureProvider csdb = context.mock(CorpusStructureProvider.class);
        final ZipService zipService = context.mock(ZipService.class);
        final TypedCorpusNode node = context.mock(TypedCorpusNode.class, "parent");
        final TypedCorpusNode child1 = context.mock(TypedCorpusNode.class, "child1");
        final TypedCorpusNode child2 = context.mock(TypedCorpusNode.class, "child2");
        final List<TypedCorpusNode> childrenList = Arrays.asList(child1,child2);
        final String userId = null;

        final File zipFile = File.createTempFile("test", "txt");
        final FileWriter fileWriter = new FileWriter(zipFile);
        fileWriter.write("Test content");
        fileWriter.close();
        
        context.checking(new Expectations() {

            {
                allowing(node).getUri();
                will(returnValue(new URI("nodeUri")));
                allowing(node).getNodeId();
                will(returnValue(NODE_ID));
                allowing(node).getName();
                will(returnValue("nodeName"));
                
                oneOf(csdb).getChildrenNodes(NODE_ID);
                will(returnValue(childrenList));
                
                oneOf(zipService).createZipFileForNodes(childrenList, userId);
                will(returnValue(zipFile));
            }
        });

        CMDIMultipleDownloadNodeAction instance = new CMDIMultipleDownloadNodeAction(csdb, zipService);
        NodeActionResult result = instance.execute(node);
        ControllerActionRequest actionRequest = result.getControllerActionRequest();
        assertNotNull(actionRequest);
        assertThat(actionRequest, instanceOf(DownloadActionRequest.class));
        
        DownloadActionRequest downloadActionRequest = (DownloadActionRequest)actionRequest;
        assertEquals("package_nodeName", downloadActionRequest.getFileName());
        IResourceStream downloadStream = downloadActionRequest.getDownloadStream();
        assertThat(downloadStream, instanceOf(FileResourceStream.class));
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(downloadStream.getInputStream()));
        String readContent = reader.readLine();
        assertEquals("Test content", readContent);
        
        assertTrue(zipFile.exists());
        downloadStream.close();
        assertFalse(zipFile.exists());
    }
}
