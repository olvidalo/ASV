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

import java.net.URI;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.services.cmdi.impl.ProfileIdentifierImpl;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.*;

import static org.junit.Assert.assertEquals;

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
    public void testGetProfile() throws Exception {
        final CorpusStructureProvider csdb = context.mock(CorpusStructureProvider.class);
        System.out.println("getProfile");

        context.checking(new Expectations() {

            {
                oneOf(csdb).getProfileSchemaLocation(new URI("nodeUri"));
                will(returnValue(URI.create("profile1")));
            }
        });
        ProfileIdentifierImpl instance = new ProfileIdentifierImpl(csdb);
        URI expResult = URI.create("profile1");
        URI result = instance.getProfile(new URI("nodeUri"));
        assertEquals(expResult, result);
    }
}
