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
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.tree.services.NodeResolver;
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.CMDINodeType;
import org.jmock.Expectations;
import static org.jmock.Expectations.returnValue;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIViewNodeActionTest {

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
	final NodeResolver nodeResolver = context.mock(NodeResolver.class);
	
        context.checking(new Expectations() {
            {
                oneOf(node).getUri();
                will(returnValue(new URI("nodeUri")));
                oneOf(nodeResolver).getUrl(node);
                will(returnValue(new URL("http://nodeUri")));
                allowing(node).getNodeType();
                will(returnValue(new CMDINodeType()));
            }
        });

        CMDIViewNodeAction instance = new CMDIViewNodeAction(nodeResolver);
        NodeActionResult result = instance.execute(node);
        ControllerActionRequest actionRequest = result.getControllerActionRequest();
        assertNotNull(actionRequest);
        //assertThat(actionRequest, instanceOf(ShowComponentActionRequest.class));
    }

    /**
     * Test of getName method, of class CMDIViewNodeAction.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
	final NodeResolver nodeResolver = context.mock(NodeResolver.class);
	CMDIViewNodeAction instance = new CMDIViewNodeAction(nodeResolver);
        String expResult = "view Node";
        String result = instance.getName();
        assertEquals(expResult, result);
    }
}