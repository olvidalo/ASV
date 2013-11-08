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

import java.net.URI;
import java.net.URL;
import nl.mpi.archiving.corpusstructure.core.AccessInfo;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.DownloadActionRequest;
import org.apache.wicket.util.resource.IResourceStream;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.*;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIDownloadNodeActionTest {

    private final Mockery context = new JUnit4Mockery();
    private final static URI NODE_ID = URI.create("node:1");

    public CMDIDownloadNodeActionTest() {
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
     * Test of getName method, of class CMDIDownloadNodeAction.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        NodeResolver nodeResolver = context.mock(NodeResolver.class);
        CMDIDownloadNodeAction instance = new CMDIDownloadNodeAction(nodeResolver);
        String expResult = "Download";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of execute method, of class CMDIDownloadNodeAction.
     */
    @Test
    public void testExecute() throws Exception {
        final TypedCorpusNode node = context.mock(TypedCorpusNode.class, "parent");
        //final AccessInfo ai = AccessInfo.create(AccessInfo.EVERYBODY, AccessInfo.EVERYBODY, 1);
        final AccessInfo ai = context.mock(AccessInfo.class);
        final NodeResolver nodeResolver = context.mock(NodeResolver.class);

        context.checking(new Expectations() {
            {
                oneOf(nodeResolver).getUrl(node);
                will(returnValue(new URL("http://my/nodeUri")));

                allowing(node).getNodeURI();
                will(returnValue(NODE_ID));

                allowing(node).getName();
                will(returnValue("nodeName"));

                allowing(node).getAuthorization();
                will(returnValue(ai));

                allowing(ai).hasReadAccess("everybody");
                will(returnValue(true));
            }
        });

        CMDIDownloadNodeAction instance = new CMDIDownloadNodeAction(nodeResolver);
        NodeActionResult result = instance.execute(node);
        ControllerActionRequest actionRequest = result.getControllerActionRequest();
        assertNotNull(actionRequest);
        assertThat(actionRequest, instanceOf(DownloadActionRequest.class));

        assertEquals("Download", instance.getName());

        DownloadActionRequest downloadActionRequest = (DownloadActionRequest) actionRequest;
        assertEquals("nodeUri", downloadActionRequest.getFileName());
        IResourceStream downloadStream = downloadActionRequest.getDownloadStream();
        assertNotNull(downloadStream);
    }
}
