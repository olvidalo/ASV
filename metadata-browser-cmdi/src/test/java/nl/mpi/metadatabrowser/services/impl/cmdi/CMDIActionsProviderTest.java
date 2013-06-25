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
package nl.mpi.metadatabrowser.services.impl.cmdi;

import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDIDownloadNodeActionTest;
import nl.mpi.metadatabrowser.services.cmdi.impl.CMDIActionsProvider;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.tree.GenericTreeNode;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.*;
import nl.mpi.metadatabrowser.services.cmdi.ZipService;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIActionsProviderTest {

    private final Mockery context = new JUnit4Mockery();
    private TypedCorpusNode corpType = new TypedCorpusNode() {

        @Override
        public URI getNodeId() {
            return URI.create("node:1");
        }

        @Override
        public String getName() {
            return "1";
        }

        @Override
        public URI getUri() {
            try {
                URI uri = new URI("http://lux16.mpi.nl/corpora/lams_demo/Corpusstructure/1.imdi");
                return uri;
            } catch (URISyntaxException ex) {
                Logger.getLogger(CMDIDownloadNodeActionTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        @Override
        public GenericTreeNode getChild(int index) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getChildCount() {
            return 0;
        }

        @Override
        public int getIndexOfChild(GenericTreeNode child) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public GenericTreeNode getParent() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public NodeType getNodeType() {
            return new CMDICollectionType();
        }
    };

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
    public void testGetNodeActions() {
        System.out.println("getNodeActions");

        Collection<TypedCorpusNode> collectionCorpus = Arrays.<TypedCorpusNode>asList(corpType);

        CMDIActionsProvider instance = new CMDIActionsProvider(context.mock(CorpusStructureProvider.class), context.mock(ZipService.class));
        List expResult = instance.collectionNodeActionList;
        List result = instance.getNodeActions(collectionCorpus);
        System.out.println(expResult.size());
        assertEquals(expResult.size(), result.size());
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }
}
