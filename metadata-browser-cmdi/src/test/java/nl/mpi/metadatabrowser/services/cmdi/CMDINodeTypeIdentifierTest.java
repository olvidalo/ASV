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
import nl.mpi.metadatabrowser.model.NodeType;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDINodeTypeIdentifierTest {
    
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
        URI nodeUri = null;
        CMDINodeTypeIdentifier instance = new CMDINodeTypeIdentifier();
        NodeType expResult = null;
        NodeType result = instance.getNodeType(nodeUri);
        assertEquals(expResult, result);
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
