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
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.services.AuthenticationHolder;
import nl.mpi.metadatabrowser.services.FilterNodeIds;
import nl.mpi.metadatabrowser.services.authentication.AccessChecker;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.jmock.Expectations.returnValue;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIViewNodeActionTest {
    
    private final NodeActionsConfiguration nodeActionsConfiguration = new NodeActionsConfiguration();
    private final Mockery context = new JUnit4Mockery();
    private final static URI NODE_ID = URI.create("node:1");
    
    public CMDIViewNodeActionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of execute method, of class CMDIViewNodeAction.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        final TypedCorpusNode node = context.mock(TypedCorpusNode.class, "parent");
        final FilterNodeIds filter = context.mock(FilterNodeIds.class);
        final AccessChecker accessChecker = context.mock(AccessChecker.class);
        final AuthenticationHolder auth = context.mock(AuthenticationHolder.class);
        nodeActionsConfiguration.setAnnexURL("http://lux16.mpi.nl/ds/annex/search.jsp");
        context.checking(new Expectations() {
            {
                allowing(node).getNodeType();
                will(returnValue(context.mock(NodeType.class)));
                
                allowing(node).getName();
                will(returnValue("parent.jpg"));
                
                oneOf(auth).getPrincipalName();
                will(returnValue("user"));
                
                oneOf(node).getNodeURI();
                will(returnValue(URI.create("node:123")));
                
                oneOf(accessChecker).hasAccess("user", URI.create("node:123"));
                will(returnValue(true));
            }
        });
        
        final CMDIViewNodeAction instance = new CMDIViewNodeAction(nodeActionsConfiguration, filter, accessChecker);
        instance.setAuthenticationHolder(auth);
        
        NodeActionResult result = instance.execute(node);
        ControllerActionRequest actionRequest = result.getControllerActionRequest();
        assertNotNull(actionRequest);
    }
    //TODO: Add test for view node action cases
}
