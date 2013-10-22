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

import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class SimpleNodeActionResultTest {
    private final Mockery context = new JUnit4Mockery();
    
    public SimpleNodeActionResultTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getControllerActionRequest method, of class SimpleNodeActionResult.
     */
    @Test
    public void testGetControllerActionRequest() {
        System.out.println("getControllerActionRequest");
        ControllerActionRequest crequest = context.mock(ControllerActionRequest.class);
        SimpleNodeActionResult instance = new SimpleNodeActionResult(crequest);

        assertNotNull(instance.getControllerActionRequest());
        assertEquals(crequest, instance.getControllerActionRequest());

 
   }

    /**
     * Test of getFeedbackMessage method, of class SimpleNodeActionResult.
     */
    @Test
    public void testGetFeedbackMessage() {
        System.out.println("getFeedbackMessage");
        SimpleNodeActionResult instance = new SimpleNodeActionResult("this is a feedback message");
        assertNotNull(instance.getFeedbackMessage());
        
        String expResult = "this is a feedback message";
        String result = instance.getFeedbackMessage();
        assertEquals(expResult, result);

    }
}