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
package nl.mpi.metadatabrowser.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class SingleNodeActionTest {

    
    private Mockery context = new JUnit4Mockery();

    /**
     * Test execute with single node. Should succeed!
     */
    @Test
    public void testExecuteOne() throws Exception {
	final TypedCorpusNode inputNode = context.mock(TypedCorpusNode.class);
	final NodeActionResult expectedResult = context.mock(NodeActionResult.class);

	final NodeAction instance = new SingleNodeActionTestImpl() {
	    @Override
	    protected NodeActionResult execute(TypedCorpusNode node) throws NodeActionException {
		assertEquals(inputNode, node);
		return expectedResult;
	    }
	};

	// execute action with singleton collection and check whether the node matches
	final Set<TypedCorpusNode> nodeCollection = Collections.singleton(inputNode);
	final NodeActionResult result = instance.execute(nodeCollection);
	assertEquals(expectedResult, result);
    }

    /**
     * Test execute with no nodes. This violates pre-condition, should throw exception.
     */
    @Test(expected = NodeActionException.class)
    public void testExecuteZeroInputNodes() throws NodeActionException {
	// implementation with execute(node) method that should not get called because we are going to call it with an empty list
	final NodeAction instance = new SingleNodeActionTestImpl() {
	    @Override
	    protected NodeActionResult execute(TypedCorpusNode node) throws NodeActionException {
		fail("execute(node) should not get called");
		throw new RuntimeException();
	    }
	};
	// Execute with empty list
	instance.execute(Collections.<TypedCorpusNode>emptyList());
	// exception should have been thrown at this point
    }

    /**
     * Test execute with multiple nodes. This violates pre-condition, should throw exception.
     */
    @Test(expected = NodeActionException.class)
    public void testExecuteMultipleInputNodes() throws NodeActionException {
	// implementation with execute(node) method that should not get called because we are going to call it with an empty list
	final NodeAction instance = new SingleNodeActionTestImpl() {
	    @Override
	    protected NodeActionResult execute(TypedCorpusNode node) throws NodeActionException {
		fail("execute(node) should not get called");
		throw new RuntimeException();
	    }
	};

	// execute with two nodes
	final TypedCorpusNode inputNode1 = context.mock(TypedCorpusNode.class, "node1");
	final TypedCorpusNode inputNode2 = context.mock(TypedCorpusNode.class, "node2");
	instance.execute(Arrays.asList(inputNode1, inputNode2));
	// exception should have been thrown at this point
    }

    private static abstract class SingleNodeActionTestImpl extends SingleNodeAction {

	@Override
	public final String getName() {
	    return "name";
	}
    }
}
