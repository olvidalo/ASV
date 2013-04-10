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

import java.util.Map;
import nl.mpi.metadatabrowser.model.NavigationRequest.NavigationTarget;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class NavigationActionRequestTest {
    
    public NavigationActionRequestTest() {
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
     * Test of setTarget method, of class NavigationActionRequest.
     */
    @Test
    public void testSetTarget() {
        System.out.println("setTarget");
        NavigationTarget target = null;
        NavigationActionRequest instance = new NavigationActionRequest();
        instance.setTarget(target);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getTarget method, of class NavigationActionRequest.
     */
    @Test
    public void testGetTarget() {
        System.out.println("getTarget");
        NavigationActionRequest instance = new NavigationActionRequest();
        NavigationTarget expResult = null;
        NavigationTarget result = instance.getTarget();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of setParameters method, of class NavigationActionRequest.
     */
    @Test
    public void testSetParameters() {
        System.out.println("setParameters");
        Map<String, String> parameters = null;
        NavigationActionRequest instance = new NavigationActionRequest();
        instance.setParameters(parameters);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getParameters method, of class NavigationActionRequest.
     */
    @Test
    public void testGetParameters() {
        System.out.println("getParameters");
        NavigationActionRequest instance = new NavigationActionRequest();
        Map expResult = null;
        Map result = instance.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }
}
