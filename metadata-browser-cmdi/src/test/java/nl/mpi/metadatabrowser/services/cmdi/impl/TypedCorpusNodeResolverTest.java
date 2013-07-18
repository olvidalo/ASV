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

import java.net.URI;
import java.net.URISyntaxException;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.corpusstructure.provider.UnknownNodeException;
import nl.mpi.archiving.tree.CorpusNode;
import org.jmock.Expectations;
import static org.jmock.Expectations.returnValue;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class TypedCorpusNodeResolverTest {
    private final Mockery context = new JUnit4Mockery();
    
    public TypedCorpusNodeResolverTest() {
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

    /**
     * Test of getUri method, of class TypedCorpusNodeResolver.
     */
    @Test
    public void testGetUri() throws URISyntaxException, UnknownNodeException {
        System.out.println("getUri");
        final CorpusStructureProvider csdb = context.mock(CorpusStructureProvider.class);
        final CorpusNode node = context.mock(CorpusNode.class);
        TypedCorpusNodeResolver instance = new TypedCorpusNodeResolver(csdb);
        
        context.checking(new Expectations() {

            {
                oneOf(node).getNodeId();
                will(returnValue(new URI("1")));
                oneOf(csdb).getObjectURI(new URI("1"));
                will(returnValue(new URI("nodeUri")));
            }
        });
        
        URI expResult = new URI("nodeUri");
        URI result = instance.getUri(node);
        assertNotNull(result);
        assertEquals(expResult, result);

    }
}