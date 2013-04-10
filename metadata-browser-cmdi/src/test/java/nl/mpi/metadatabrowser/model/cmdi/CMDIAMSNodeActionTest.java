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
import java.util.Map;
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NavigationRequest;
import nl.mpi.metadatabrowser.model.NavigationRequest.NavigationTarget;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIAMSNodeActionTest {
    
    public CMDIAMSNodeActionTest() {
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
     * Test of getName method, of class CMDIAMSNodeAction.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        CMDIAMSNodeAction instance = new CMDIAMSNodeAction();
        String expResult = "navigate";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    
        /**
     * Test of setName method, of class CMDIAMSNodeAction.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        String name = "navigate";
        CMDIAMSNodeAction instance = new CMDIAMSNodeAction();
        instance.setName(name);
    } 
    
    
    /**
     * Test of execute method, of class CMDIAMSNodeAction.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        URI nodeUri = new URI("http://lux16.mpi.nl/corpora/lams_demo/Corpusstructure/1.imdi");
        CMDIAMSNodeAction instance = new CMDIAMSNodeAction();
        NodeActionResult expResult = new NodeActionResult() {

            @Override
            public String getFeedbackMessage() {
                return "no message";
            }

            @Override
            public ControllerActionRequest getControllerActionRequest() {
                return new NavigationActionRequest();
            }
        };
        NodeActionResult result = instance.execute(nodeUri);
        assertEquals("navigate", instance.getName());
        // TODO review the generated test code and remove the default call to fail.
    }
}
