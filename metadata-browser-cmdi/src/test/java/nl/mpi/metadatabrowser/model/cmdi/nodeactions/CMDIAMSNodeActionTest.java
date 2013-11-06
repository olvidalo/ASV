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
import java.util.ArrayList;
import java.util.Collection;
import javax.ws.rs.core.UriBuilder;
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.NavigationActionRequest;
import static org.hamcrest.Matchers.instanceOf;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import static org.junit.Assert.*;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIAMSNodeActionTest {

    @Autowired
    private NodeActionsConfiguration nodeActionsConfiguration = new NodeActionsConfiguration();
    private final Mockery context = new JUnit4Mockery();
    private final static URI NODE_ID = URI.create("node:1");

    public CMDIAMSNodeActionTest() {
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
     * Test of getName method, of class CMDIAMSNodeAction.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        CMDIAMSNodeAction instance = new CMDIAMSNodeAction(nodeActionsConfiguration);
        String expResult = "ams";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of execute method, of class CMDIAMSNodeAction.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        final TypedCorpusNode node = context.mock(TypedCorpusNode.class, "parent");
        Collection<TypedCorpusNode> nodes = new ArrayList<TypedCorpusNode>();
        nodes.add(node);
        nodeActionsConfiguration.setAmsURL("http://lux16.mpi.nl/am/ams2/index.face");
        URL url = new URL(nodeActionsConfiguration.getAmsURL() + "?nodeid=" + NODE_ID + "&jsessionID=" + new URI("session_id"));

        context.checking(new Expectations() {
            {
                allowing(node).getNodeURI();
                will(returnValue(NODE_ID));
            }
        });



        CMDIAMSNodeAction instance = new CMDIAMSNodeAction(nodeActionsConfiguration);
        NodeActionResult result = instance.execute(node);
        assertEquals("ams", instance.getName());

        ControllerActionRequest actionRequest = result.getControllerActionRequest();
        assertNotNull(actionRequest);
        assertThat(actionRequest, instanceOf(NavigationActionRequest.class));

        NavigationActionRequest navigationActionRequest = (NavigationActionRequest) actionRequest;
        assertNotNull(navigationActionRequest.getTargetURL());
        assertEquals(url.toString(), navigationActionRequest.getTargetURL().toString());
    }
}
