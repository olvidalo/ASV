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
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIMetadataType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDISessionType;
import nl.mpi.metadatabrowser.services.NodeIdFilter;
import nl.mpi.metadatabrowser.services.cmdi.mock.MockNodeIdFilter;
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
public class CMDIAMSNodeActionTest {

    private NodeActionsConfiguration nodeActionsConfiguration;
    private NodeIdFilter filterIdProvider;
    private Mockery context;
    private static URI NODE_ID;

    public CMDIAMSNodeActionTest() {
    }

    @Before
    public void setUp() {
        context = new JUnit4Mockery();
        NODE_ID = URI.create("node:1");
        nodeActionsConfiguration = new NodeActionsConfiguration();
        filterIdProvider = new MockNodeIdFilter();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getName method, of class CMDIAMSNodeAction.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        CMDIAMSNodeAction instance = new CMDIAMSNodeAction(nodeActionsConfiguration, filterIdProvider);
        String expResult = "Manage Access";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of execute method, of class CMDIAMSNodeAction.
     */
    @Test
    public void testExecuteAms() throws Exception {
        System.out.println("execute");
        final TypedCorpusNode node = context.mock(TypedCorpusNode.class, "parent");
//        MockFilterNodeId filterIdProvider = new MockFilterNodeId();
        Collection<TypedCorpusNode> nodes = new ArrayList<TypedCorpusNode>();

        nodes.add(node);

        context.checking(
                new Expectations() {
                    {
                        allowing(node).getNodeType();
                        will(returnValue(new IMDISessionType()));

                        allowing(node).getNodeURI();
                        will(returnValue(NODE_ID));

//                        oneOf(node).getPID();
//                        will(returnValue(NODE_ID.toString()));
                    }
                });

        String id = filterIdProvider.getURIParam(NODE_ID);

        nodeActionsConfiguration.setAmsURL(
                "http://lux16.mpi.nl/am/ams2/index.face");
        UriBuilder url = UriBuilder.fromUri(nodeActionsConfiguration.getAmsURL());
        URI targetURI = url.queryParam("nodeid", id).queryParam("jsessionID", new URI("session_id")).build();
        CMDIAMSNodeAction instance = new CMDIAMSNodeAction(nodeActionsConfiguration, filterIdProvider);
        NodeActionResult result = instance.execute(node);

        assertEquals(
                "Manage Access", instance.getName());

        ControllerActionRequest actionRequest = result.getControllerActionRequest();
        assertNotNull(actionRequest);
        assertThat(actionRequest, instanceOf(ShowComponentRequest.class));

//        assertThat(actionRequest, instanceOf(NavigationActionRequest.class));
//
//        NavigationActionRequest navigationActionRequest = (NavigationActionRequest) actionRequest;
//
//        assertNotNull(navigationActionRequest.getTargetURL());
//        assertEquals(targetURI.toString(), navigationActionRequest.getTargetURL().toString());
    }

    /**
     * Test of execute method, of class CMDIAMSNodeAction.
     */
    @Test
    public void testExecuteAmscs2() throws Exception {
        System.out.println("execute");
        final TypedCorpusNode node = context.mock(TypedCorpusNode.class, "parent");
//        MockFilterNodeId filterIdProvider = new MockFilterNodeId();
        Collection<TypedCorpusNode> nodes = new ArrayList<TypedCorpusNode>();

        nodes.add(node);
//        String id = filterIdProvider.getURIParam(NODE_ID);
        nodeActionsConfiguration.setAmsURLForcs2("http://lux16.mpi.nl/am/ams2-cmdi/index.face");
        UriBuilder url = UriBuilder.fromUri(nodeActionsConfiguration.getAmsURLForcs2());
        URI targetURI = url.queryParam("nodeid", NODE_ID.toString()).build();

        context.checking(
                new Expectations() {
                    {
                        allowing(node).getNodeType();
                        will(returnValue(new CMDIMetadataType()));

                        allowing(node).getNodeURI();
                        will(returnValue(NODE_ID));

//                        oneOf(node).getPID();
//                        will(returnValue(NODE_ID.toString()));
                    }
                });


        CMDIAMSNodeAction instance = new CMDIAMSNodeAction(nodeActionsConfiguration, filterIdProvider);
        NodeActionResult result = instance.execute(node);

        assertEquals(
                "Manage Access", instance.getName());

        ControllerActionRequest actionRequest = result.getControllerActionRequest();
        assertNotNull(actionRequest);
        assertThat(actionRequest, instanceOf(ShowComponentRequest.class));

//        assertThat(actionRequest, instanceOf(NavigationActionRequest.class));
//
//        NavigationActionRequest navigationActionRequest = (NavigationActionRequest) actionRequest;
//
//        assertNotNull(navigationActionRequest.getTargetURL());
//        assertEquals(targetURI.toString(), navigationActionRequest.getTargetURL().toString());
    }
}
