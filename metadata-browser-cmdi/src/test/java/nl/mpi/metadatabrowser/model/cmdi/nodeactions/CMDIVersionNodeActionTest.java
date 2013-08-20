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

import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import static org.hamcrest.Matchers.instanceOf;
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
public class CMDIVersionNodeActionTest {

    private final Mockery context = new JUnit4Mockery();

    public CMDIVersionNodeActionTest() {
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
     * Test of execute method, of class CMDIVersionNodeAction.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        final TypedCorpusNode node = context.mock(TypedCorpusNode.class, "parent");
        final CorpusStructureProvider csdb = context.mock(CorpusStructureProvider.class);
	final NodeResolver nodeResolver = context.mock(NodeResolver.class);

        CMDIVersionNodeAction instance = new CMDIVersionNodeAction(csdb, nodeResolver);
        NodeActionResult result = instance.execute(node);

        ControllerActionRequest actionRequest = result.getControllerActionRequest();
        assertNotNull(actionRequest);
        assertThat(actionRequest, instanceOf(ShowComponentRequest.class));
    }

    /**
     * Test of getName method, of class CMDIVersionNodeAction.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        final CorpusStructureProvider csdb = context.mock(CorpusStructureProvider.class);
	final NodeResolver nodeResolver = context.mock(NodeResolver.class);
	CMDIVersionNodeAction instance = new CMDIVersionNodeAction(csdb, nodeResolver);
        String expResult = "version";
        String result = instance.getName();
        assertEquals(expResult, result);
    }
}