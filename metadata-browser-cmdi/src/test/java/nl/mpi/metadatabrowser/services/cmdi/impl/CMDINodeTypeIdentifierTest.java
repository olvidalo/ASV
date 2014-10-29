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

import java.io.File;
import java.net.URI;
import java.net.URL;
import nl.mpi.archiving.corpusstructure.core.CorpusNodeType;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDICollectionType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIMetadataType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourceAudioType;
import static org.hamcrest.Matchers.instanceOf;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.*;

import static org.jmock.Expectations.returnValue;
import static org.junit.Assert.assertThat;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDINodeTypeIdentifierTest {

    public static final URI NODE_1_ID = URI.create("node:1");
    private final Mockery context = new JUnit4Mockery();

    private CorpusStructureProvider csdb;
    private NodeResolver nodeResolver;
    private CMDINodeTypeIdentifier instance;

    @Before
    public void setUp() {
        csdb = context.mock(CorpusStructureProvider.class);
        nodeResolver = context.mock(NodeResolver.class);
        instance = new CMDINodeTypeIdentifier(csdb, nodeResolver);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getNodeType method, of class CMDINodeTypeIdentifier.
     */
    @Test
    public void testGetNodeTypeAudio() throws Exception {
        final TypedCorpusNode node = context.mock(TypedCorpusNode.class);
        context.checking(new Expectations() {
            {
                oneOf(nodeResolver).getLocalFile(node);
                will(returnValue(new File("audio.wav")));

                allowing(node).getType();
                will(returnValue(CorpusNodeType.RESOURCE_AUDIO));
            }
        });
        NodeType result = instance.getNodeType(node);
        assertThat(result, instanceOf(ResourceAudioType.class));
    }    
    
    @Test
    public void testGetNodeTypeTransformedCmdiRecord() throws Exception {
        final TypedCorpusNode node = context.mock(TypedCorpusNode.class);

        context.checking(new Expectations() {
            {
                oneOf(nodeResolver).getLocalFile(node);
                will(returnValue(new File("record.cmdi")));

                allowing(node).getType();
                will(returnValue(CorpusNodeType.METADATA));
                
                allowing(node).getFormat();
                will(returnValue("application/imdi+xml"));
            }
        });
        NodeType result = instance.getNodeType(node);
        assertThat(result, instanceOf(CMDIMetadataType.class));
    } 
    
    @Test
    public void testGetNodeTypeTransformedCmdiCollection() throws Exception {
        final TypedCorpusNode node = context.mock(TypedCorpusNode.class);

        context.checking(new Expectations() {
            {
                oneOf(nodeResolver).getLocalFile(node);
                will(returnValue(new File("collection.cmdi")));

                allowing(node).getType();
                will(returnValue(CorpusNodeType.COLLECTION));
                
                allowing(node).getFormat();
                will(returnValue("application/imdi+xml"));
            }
        });
        NodeType result = instance.getNodeType(node);
        assertThat(result, instanceOf(CMDICollectionType.class));
    }
}
