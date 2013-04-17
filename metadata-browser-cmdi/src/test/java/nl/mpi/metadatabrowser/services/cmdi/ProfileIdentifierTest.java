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
import nl.mpi.metadatabrowser.model.cmdi.CmdiCorpusStructureDB;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class ProfileIdentifierTest {
        private final Mockery context = new JUnit4Mockery();
    
    public ProfileIdentifierTest() {
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
     * Test of getProfile method, of class ProfileIdentifier.
     */
    @Test
    public void testGetProfile() {
        final CmdiCorpusStructureDB csdb = context.mock(CmdiCorpusStructureDB.class);
        System.out.println("getProfile");
        URI uri = null;
        ProfileIdentifier instance = new ProfileIdentifier(csdb);
        String expResult = "profile2";
        instance.setProfile(expResult);
        String result = instance.getProfile(uri);
        assertNotEquals(expResult, result);
    }

    /**
     * Test of setProfile method, of class ProfileIdentifier.
     */
    @Test
    public void testSetProfile() {
        final CmdiCorpusStructureDB csdb = context.mock(CmdiCorpusStructureDB.class);
        System.out.println("setProfile");
        String expResult_2 = "profile2";
        ProfileIdentifier instance = new ProfileIdentifier(csdb);
        instance.setProfile(expResult_2);
        assertNotEquals(expResult_2, instance.getProfile(null));
        assertEquals("profile1", instance.getProfile(null));
    }
}
