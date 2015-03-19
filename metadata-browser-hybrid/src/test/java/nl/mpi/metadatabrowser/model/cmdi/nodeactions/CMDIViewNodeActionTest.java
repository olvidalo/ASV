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
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.metadatabrowser.model.ActionSelectionRequest;
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.NavigationActionRequest;
import nl.mpi.metadatabrowser.services.AuthenticationHolder;
import nl.mpi.metadatabrowser.services.NodeIdFilter;
import nl.mpi.metadatabrowser.services.authentication.AccessChecker;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
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

    private AuthenticationHolder auth;
    private NodeResolver nodeResolver;
    private AccessChecker accessChecker;
    private NodeIdFilter filter;
    private TypedCorpusNode node;
    private CMDIViewNodeAction instance;

    @Before
    public void setUp() {
        node = context.mock(TypedCorpusNode.class, "parent");
        filter = context.mock(NodeIdFilter.class);
        accessChecker = context.mock(AccessChecker.class);
        nodeResolver = context.mock(NodeResolver.class);
        auth = context.mock(AuthenticationHolder.class);
        nodeActionsConfiguration.setAnnexMimeTypes("text/x-eaf+xml text/x-chat");
        nodeActionsConfiguration.setOptionalAnnexMimeTypes("text/plain");
        nodeActionsConfiguration.setAnnexURL("http://lux16.mpi.nl/ds/annex/search.jsp");
        
        final ViewResourceAction viewResourceAction = new ViewResourceAction(accessChecker, nodeResolver);
        viewResourceAction.setAuthenticationHolder(auth);

        final ViewInAnnexAction viewInAnnexAction = new ViewInAnnexAction(nodeActionsConfiguration, filter, nodeResolver);
        viewInAnnexAction.setAuthenticationHolder(auth);
        
        instance = new CMDIViewNodeAction(nodeActionsConfiguration, viewResourceAction, viewInAnnexAction);
        instance.init();
        instance.setAuthenticationHolder(auth);
    }

    /**
     * Test of execute method, of class CMDIViewNodeAction.
     */
    @Test
    public void testExecuteView() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(node).getNodeType();
                will(returnValue(context.mock(NodeType.class)));

                allowing(node).getFormat();
                will(returnValue("image/jpeg")); //not an annex format

                oneOf(auth).getPrincipalName();
                will(returnValue("user"));

                oneOf(node).getNodeURI();
                will(returnValue(URI.create("node:123")));

                oneOf(accessChecker).hasAccess("user", URI.create("node:123"));
                will(returnValue(true));
            }
        });

        final NodeActionResult result = instance.execute(node);
        final ControllerActionRequest actionRequest = result.getControllerActionRequest();
        assertTrue(actionRequest instanceof ShowComponentRequest);
    }

    /**
     * Test of execute method, of class CMDIViewNodeAction.
     */
    @Test
    public void testExecuteAnnex() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(node).getNodeType();
                will(returnValue(context.mock(NodeType.class)));

                allowing(node).getFormat();
                will(returnValue("text/x-eaf+xml"));

                oneOf(nodeResolver).getPID(node);
                will(returnValue(URI.create("hdl:1234/5678-abcd")));
            }
        });

        final NodeActionResult result = instance.execute(node);
        final ControllerActionRequest actionRequest = result.getControllerActionRequest();
        assertTrue(actionRequest instanceof NavigationActionRequest);
        NavigationActionRequest navRequest = (NavigationActionRequest) actionRequest;
        assertTrue(navRequest.getTargetURL().toString().contains("http://lux16.mpi.nl/ds/annex/search.jsp?handle=hdl:1234/5678-abcd"));
    }

    /**
     * Test of execute method, of class CMDIViewNodeAction.
     */
    @Test
    public void testExecuteAnnexNoPid() throws Exception {
        final URI nodeId = URI.create("node:123");
        context.checking(new Expectations() {
            {
                allowing(node).getNodeType();
                will(returnValue(context.mock(NodeType.class)));

                allowing(node).getFormat();
                will(returnValue("text/x-chat"));

                oneOf(nodeResolver).getPID(node);
                will(returnValue(null));

                oneOf(node).getNodeURI();
                will(returnValue(nodeId));

                oneOf(filter).getURIParam(nodeId);
                will(returnValue("NODE-ID-PARAM"));
            }
        });

        final NodeActionResult result = instance.execute(node);
        final ControllerActionRequest actionRequest = result.getControllerActionRequest();
        assertTrue(actionRequest instanceof NavigationActionRequest);
        NavigationActionRequest navRequest = (NavigationActionRequest) actionRequest;
        assertTrue(navRequest.getTargetURL().toString().equals("http://lux16.mpi.nl/ds/annex/search.jsp?nodeid=NODE-ID-PARAM"));
    }

    /**
     * Test of execute method, of class CMDIViewNodeAction.
     */
    @Test
    public void testExecuteSelect() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(node).getNodeType();
                will(returnValue(context.mock(NodeType.class)));

                allowing(node).getFormat();
                will(returnValue("text/plain"));
            }
        });

        final NodeActionResult result = instance.execute(node);
        final ControllerActionRequest actionRequest = result.getControllerActionRequest();
        assertTrue(actionRequest instanceof ActionSelectionRequest);
        //ActionSelectionRequest selectionRequest = (ActionSelectionRequest) actionRequest;
        //test actions in list
    }
}
