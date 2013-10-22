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
package nl.mpi.metadatabrowser.services.cmdi.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import nl.mpi.archiving.corpusstructure.core.UnknownNodeException;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.services.cmdi.ZipService;
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
public class CMDIActionsProviderTest {

    private final Mockery context = new JUnit4Mockery();
    private TypedCorpusNode corpType = new MockTypedCorpusNode("node:1", "1");

    public CMDIActionsProviderTest() {
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
     * Test of getNodeActions method, of class CMDIActionsProvider.
     */
    @Test
    public void testGetNodeActions() throws UnknownNodeException {
        System.out.println("getNodeActions");
        final Collection<TypedCorpusNode> collectionCorpus = Arrays.<TypedCorpusNode>asList(corpType);
        final CorpusStructureProvider cs = context.mock(CorpusStructureProvider.class);
        final TypedCorpusNode child1 = context.mock(TypedCorpusNode.class, "child1");
        final TypedCorpusNode child2 = context.mock(TypedCorpusNode.class, "child2");
        final List<TypedCorpusNode> childrenList = Arrays.asList(child1, child2);

        context.checking(new Expectations() {
            {
                oneOf(cs).getChildNodes(corpType.getNodeURI());
                will(returnValue(childrenList));
            }
        });

        final CMDIActionsProvider instance = new CMDIActionsProvider(cs);
	instance.init();
	
        final List result = instance.getNodeActions(collectionCorpus);
        assertNotNull(result);
    }
}
