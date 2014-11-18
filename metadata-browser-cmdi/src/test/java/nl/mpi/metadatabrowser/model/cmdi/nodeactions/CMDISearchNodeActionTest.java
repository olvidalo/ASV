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
import java.util.ArrayList;
import java.util.Collection;
import javax.ws.rs.core.UriBuilder;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIMetadataType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDISessionType;
import nl.mpi.metadatabrowser.services.NodeIdFilter;
import nl.mpi.metadatabrowser.services.URIFilter;
import static org.hamcrest.Matchers.instanceOf;
import org.jmock.Expectations;
import static org.jmock.Expectations.returnValue;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDISearchNodeActionTest {

    private NodeActionsConfiguration nodeActionsConfiguration;
    private URIFilter uriFilter;
    private NodeIdFilter filterIdProvider;
    private Mockery context;
    private static URI NODE_ID;
    private static URI NODE_PID;
//    private TypedCorpusNode node;

    public CMDISearchNodeActionTest() {
    }

    @Before
    public void setUp() {
        nodeActionsConfiguration = new NodeActionsConfiguration();
        context = new JUnit4Mockery();
        filterIdProvider = context.mock(NodeIdFilter.class);
        uriFilter = context.mock(URIFilter.class);
        NODE_ID = URI.create("node:1");
        NODE_PID = URI.create("11142/123456789101");
//        node = new MockTypedCorpusNode(new IMDISessionType(), "node:0", "parent");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getName method, of class CMDISearchNodeAction.
     */
    @Test
    public void testGetName() {
        final NodeResolver nodeResolver = context.mock(NodeResolver.class);
        System.out.println("getName");
        CMDISearchNodeAction instance = new CMDISearchNodeAction(nodeActionsConfiguration, filterIdProvider, nodeResolver, uriFilter);
        String expResult = "Metadata Search";
        String result = instance.getName();
        assertEquals(expResult, result);

    }

    /**
     * Test of execute method, of class CMDISearchNodeAction.
     * <p>
     * @throws java.lang.Exception
     */
    @Test
    public void testExecuteIMDISearch() throws Exception {
        System.out.println("execute");
        final NodeResolver nodeResolver = context.mock(NodeResolver.class);
        final TypedCorpusNode node = context.mock(TypedCorpusNode.class, "parent");
        Collection<TypedCorpusNode> nodes = new ArrayList<>();
        nodes.add(node);

        context.checking(new Expectations() {
            {
                allowing(node).getNodeType();
                will(returnValue(new IMDISessionType()));

                allowing(node).getNodeURI();
                will(returnValue(NODE_ID));

                allowing(filterIdProvider).getURIParam(NODE_ID);
                will(returnValue(NODE_ID.toString()));
            }
        });

        String id = filterIdProvider.getURIParam(NODE_ID);
        nodeActionsConfiguration.setMdSearchURL("http://lux16.mpi.nl/ds/imdi_search/strucsearch.jsp");
        UriBuilder url = UriBuilder.fromUri(nodeActionsConfiguration.getMdSearchURL());
        URI targetURI = url.queryParam("nodeid", id).queryParam("jsessionID", new URI("session_number")).build();

        CMDISearchNodeAction instance = new CMDISearchNodeAction(nodeActionsConfiguration, filterIdProvider, nodeResolver, uriFilter);
        NodeActionResult result = instance.execute(nodes);
        assertEquals("Metadata Search", instance.getName());

        ControllerActionRequest actionRequest = result.getControllerActionRequest();
        assertNotNull(actionRequest);
        assertThat(actionRequest, instanceOf(ShowComponentRequest.class));
//        assertThat(actionRequest, instanceOf(NavigationActionRequest.class));
//
//        NavigationActionRequest navigationActionRequest = (NavigationActionRequest) actionRequest;
//        assertNotNull(navigationActionRequest.getTargetURL());
//        assertEquals(targetURI.toString(), navigationActionRequest.getTargetURL().toString());
    }

    /**
     * Test of execute method, of class CMDISearchNodeAction.
     * <p>
     * @throws java.lang.Exception
     */
    @Test
    public void testExecuteCMDISearch() throws Exception {
        System.out.println("execute");
        final TypedCorpusNode node = context.mock(TypedCorpusNode.class,
                "parent");
        final NodeResolver resolver = context.mock(NodeResolver.class);
        Collection<TypedCorpusNode> nodes = new ArrayList<>();
        nodes.add(node);
        nodeActionsConfiguration
                .setYamsSearchURL("http://lux17.mpi.nl/ds/yaas/search.html");
        UriBuilder url = UriBuilder.fromUri(nodeActionsConfiguration
                .getYamsSearchURL());
        URI targetURI = url.queryParam("hdl", NODE_PID.toString()).build();

        context.checking(new Expectations() {
            {
                allowing(node).getNodeType();
                will(returnValue(new CMDIMetadataType()));

                allowing(resolver).getPID(node);
                will(returnValue(NODE_PID));
            }
        });

        CMDISearchNodeAction instance = new CMDISearchNodeAction(
                nodeActionsConfiguration, filterIdProvider, resolver, uriFilter);
        NodeActionResult result = instance.execute(nodes);
        assertEquals("Metadata Search", instance.getName());

        ControllerActionRequest actionRequest = result
                .getControllerActionRequest();
        assertNotNull(actionRequest);
        assertThat(actionRequest, instanceOf(ShowComponentRequest.class));
//		assertThat(actionRequest, instanceOf(NavigationActionRequest.class));
//
//		NavigationActionRequest navigationActionRequest = (NavigationActionRequest) actionRequest;
//		assertNotNull(navigationActionRequest.getTargetURL());
//		assertEquals(targetURI.toURL().toString(), navigationActionRequest
//				.getTargetURL().toString());
    }
}
