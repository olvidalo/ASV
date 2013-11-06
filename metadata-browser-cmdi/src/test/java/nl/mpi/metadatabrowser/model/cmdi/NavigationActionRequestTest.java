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
package nl.mpi.metadatabrowser.model.cmdi;

import java.net.MalformedURLException;
import java.net.URL;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class NavigationActionRequestTest {
    private final Mockery context = new JUnit4Mockery();
    private final URL url;

    public NavigationActionRequestTest() throws MalformedURLException {
        this.url = new URL("http://lux16.mpi.nl/am/ams2/index.face");
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
     * Test of setTargetUrl method, of class NavigationActionRequest.
     */
    @Test
    public void testSetTargetURL() {
        System.out.println("setTarget");

        NavigationActionRequest instance = new NavigationActionRequest(url);
        instance.setTargetURL(url);
        assertNotNull(instance.getTargetURL());

    }

    /**
     * Test of getTargetURL method, of class NavigationActionRequest.
     */
    @Test
    public void testGetTargetURL() throws MalformedURLException {
        System.out.println("getTarget");

        NavigationActionRequest instance = new NavigationActionRequest(url);
        URL expResult = new URL("http://lux16.mpi.nl/am/ams2/index.face");
        URL result = instance.getTargetURL();
        assertEquals(expResult.toString(), result.toString());

    }
}
