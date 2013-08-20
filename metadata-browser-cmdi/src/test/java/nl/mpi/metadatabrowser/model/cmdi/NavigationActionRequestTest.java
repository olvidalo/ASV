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
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.metadatabrowser.model.NavigationRequest;
import nl.mpi.metadatabrowser.model.NavigationRequest.NavigationTarget;
import org.jmock.Expectations;
import static org.jmock.Expectations.returnValue;
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
    public void testSetTarget() throws URISyntaxException {
        System.out.println("setTarget");
        final NavigationTarget target = NavigationTarget.AMS;
        final Map<String, URI> parameters = new HashMap<String, URI>();
        parameters.put("nodeId", new URI("1"));

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
        Map<String, URI> parameters = null;
        NavigationActionRequest instance = new NavigationActionRequest(target, parameters);
        NavigationTarget expResult = NavigationTarget.AMS;
        NavigationTarget result = instance.getTarget();
        assertEquals(expResult, result);
    }

    /**
     * Test of setParameters method, of class NavigationActionRequest.
     */
    @Test
    public void testSetParameters() throws URISyntaxException {
        final CorpusNode node = context.mock(CorpusNode.class);
        System.out.println("setParameters");
        Map<String, URI> parameters = new HashMap<String, URI>();
        
        
                context.checking(new Expectations() {
            {
                oneOf(node).getNodeURI();
                will(returnValue(new URI("nodeid")));

            }
        });
        
        parameters.put("nodeId", node.getNodeURI());
        parameters.put("jessionID", new URI("session_number"));
        NavigationActionRequest instance = new NavigationActionRequest(NavigationTarget.AMS, parameters);
        assertNotNull(instance);
        instance.setParameters(parameters);
        assertNotNull(instance.getParameters());

    }

    /**
     * Test of getParameters method, of class NavigationActionRequest.
     */
    @Test
    public void testGetParameters() {
        System.out.println("getParameters");
        final NavigationRequest request = context.mock(NavigationRequest.class);
        Map<String, URI> parameters = null;


        context.checking(new Expectations() {
            {
                oneOf(request).getTarget();
                will(returnValue(NavigationTarget.AMS));
                oneOf(request).getParameters();
                will(returnValue(null));
            }
        });
        NavigationActionRequest instance = new NavigationActionRequest(request.getTarget(), parameters);
        Map expResult = null;
        Map result = instance.getParameters();
        assertEquals(expResult, result);
    }
}
