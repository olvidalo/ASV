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
package nl.mpi.metadatabrowser.services.cmdi;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.mpi.archiving.tree.GenericTreeNode;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.CMDIDownloadNodeActionTest;
import nl.mpi.metadatabrowser.model.cmdi.CMDIMetadata;
import nl.mpi.metadatabrowser.model.cmdi.CMDIResourceTxtType;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDINodeTypeIdentifierTest {
    
        private TypedCorpusNode corpType = new TypedCorpusNode() {

        @Override
        public int getNodeId() {
            return 1;
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
            return new CMDIResourceTxtType();
        }
    };
    
    
    public CMDINodeTypeIdentifierTest() {
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
     * Test of getNodeType method, of class CMDINodeTypeIdentifier.
     */
    @Test
    public void testGetNodeType() {
        System.out.println("getNodeType");
        CMDINodeTypeIdentifier instance = new CMDINodeTypeIdentifier();
        NodeType expResult = new CMDIResourceTxtType();
        NodeType result = instance.getNodeType(corpType);
        System.out.println(expResult);
        System.out.println(result);
        assertEquals(expResult.getName(), result.getName());
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getProfileId method, of class CMDINodeTypeIdentifier.
     */
    @Test
    public void testGetProfileId() {
        System.out.println("getProfileId");
        URI nodeURI = null;
        CMDINodeTypeIdentifier instance = new CMDINodeTypeIdentifier();
        ProfileIdentifier expResult = new ProfileIdentifier();
        ProfileIdentifier result = instance.getProfileId(nodeURI);
        assertSame(expResult.getProfile(null), result.getProfile(nodeURI));
        // TODO review the generated test code and remove the default call to fail.
    }
}
