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

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import nl.mpi.metadatabrowser.model.NavigationRequest;
import nl.mpi.metadatabrowser.model.NavigationRequest.NavigationTarget;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class NavigationActionRequestTest {

    public NavigationActionRequestTest() {
    }
    private final Mockery context = new JUnit4Mockery();

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
        final NavigationTarget target = NavigationTarget.AMS;
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("nodeId", "1");

        NavigationActionRequest instance = new NavigationActionRequest(target, parameters);
        instance.setTarget(target);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getTarget method, of class NavigationActionRequest.
     */
    @Test
    public void testGetTarget() {
        System.out.println("getTarget");
final NavigationTarget target = NavigationTarget.AMS;
        Map<String, String> parameters = null;
        NavigationActionRequest instance = new NavigationActionRequest(target, parameters);
        NavigationTarget expResult = NavigationTarget.AMS;
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
        NavigationActionRequest instance = new NavigationActionRequest(NavigationTarget.AMS, parameters);
    }

    /**
     * Test of getParameters method, of class NavigationActionRequest.
     */
    @Test
    public void testGetParameters() {
        System.out.println("getParameters");
        final NavigationRequest request = context.mock(NavigationRequest.class);
        Map<String, String> parameters = null;
//        NavigationActionRequest instance = new NavigationActionRequest(request.getTarget(), parameters);
//
//        context.checking(new Expectations() {
//
//            {
//                oneOf(request).getTarget();
//                will(returnValue(NavigationTarget.AMS));
//                oneOf(request).getParameters();
//                will(returnValue(null));
//            }
//        });
//        Map expResult = null;
//        Map result = instance.getParameters();
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }
}
