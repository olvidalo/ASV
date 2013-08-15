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
import nl.mpi.archiving.corpusstructure.core.UnknownNodeException;
import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.CMDIMetadata;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifierException;
import nl.mpi.metadatabrowser.services.cmdi.mock.MockAccessInfo;
import org.apache.wicket.request.resource.ResourceReference;
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
public class CMDINodeIconProviderTest {

    private final Mockery context = new JUnit4Mockery();

    public CMDINodeIconProviderTest() {
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
     * Test of getNodeIcon method, of class CMDINodeIconProvider.
     */
    @Test
    public void testGetNodeIcon() throws NodeTypeIdentifierException, URISyntaxException, UnknownNodeException {
        System.out.println("getNodeIcon");
        final URI nodeid = new URI("nodeid1");
        final CorpusNode contentNode = context.mock(CorpusNode.class);
        final NodeTypeIdentifier nodeTypeId = context.mock(NodeTypeIdentifier.class);
        final CorpusStructureProvider csdb = context.mock(CorpusStructureProvider.class);
        CMDINodeIconProvider instance = new CMDINodeIconProvider(nodeTypeId, csdb);

        context.checking(new Expectations() {
            {
                oneOf(nodeTypeId).getNodeType(contentNode);
                will(returnValue(new CMDIMetadata()));
                allowing(contentNode).getNodeId();
                will(returnValue(nodeid));
                oneOf(csdb).getObjectAccessInfo(nodeid);
                will(returnValue(new MockAccessInfo()));
            }
        });

        ResourceReference result = instance.getNodeIcon(contentNode);
        assertNotNull(result);
    }
}