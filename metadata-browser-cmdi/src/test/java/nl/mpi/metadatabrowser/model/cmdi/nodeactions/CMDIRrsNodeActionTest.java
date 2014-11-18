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
import nl.mpi.metadatabrowser.services.NodeIdFilter;
import nl.mpi.metadatabrowser.services.cmdi.mock.MockNodeIdFilter;
import static org.hamcrest.Matchers.instanceOf;
import org.jmock.Expectations;
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
public class CMDIRrsNodeActionTest {

    private NodeActionsConfiguration nodeActionsConfiguration;
    private NodeIdFilter nodeIdFilter;
    private Mockery context;
    private static URI NODE_ID;

    public CMDIRrsNodeActionTest() {
    }

    @Before
    public void setUp() {
        nodeActionsConfiguration = new NodeActionsConfiguration();
        context = new JUnit4Mockery();
        NODE_ID = URI.create("node:1");
        nodeIdFilter = new MockNodeIdFilter();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getName method, of class CMDIRrsNodeAction.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        CMDIRrsNodeAction instance = new CMDIRrsNodeAction(nodeActionsConfiguration, nodeIdFilter);
        String expResult = "Request Access";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of execute method, of class CMDIRrsNodeAction.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        final TypedCorpusNode node = context.mock(TypedCorpusNode.class, "parent");
        Collection<TypedCorpusNode> nodes = new ArrayList<TypedCorpusNode>();
        nodes.add(node);
        String id = NODE_ID.toString();
        nodeActionsConfiguration.setRrsURL("http://lux16.mpi.nl/ds/RRS_V1/RrsIndex");

        UriBuilder url = UriBuilder.fromUri(nodeActionsConfiguration.getRrsURL() + nodeActionsConfiguration.getRrsIndexURL());
        URI targetURI = url.queryParam("nodeid", id).queryParam("jsessionID", "session_id").build();

        context.checking(new Expectations() {
            {
                allowing(node).getNodeURI();
                will(returnValue(NODE_ID));
            }
        });

        CMDIRrsNodeAction instance = new CMDIRrsNodeAction(nodeActionsConfiguration, nodeIdFilter);
        NodeActionResult result = instance.execute(nodes);
        assertEquals("Request Access", instance.getName());

        ControllerActionRequest actionRequest = result.getControllerActionRequest();
        assertNotNull(actionRequest);
        assertThat(actionRequest, instanceOf(ShowComponentRequest.class));
//        assertThat(actionRequest, instanceOf(NavigationActionRequest.class));
//
//        NavigationActionRequest navigationActionRequest = (NavigationActionRequest) actionRequest;
//        assertNotNull(navigationActionRequest.getTargetURL());
//        assertEquals(targetURI.toString(), navigationActionRequest.getTargetURL().toString());
    }
}
