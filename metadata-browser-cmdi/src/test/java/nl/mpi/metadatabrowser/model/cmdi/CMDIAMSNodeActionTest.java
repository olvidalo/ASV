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
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.mpi.archiving.tree.GenericTreeNode;
import nl.mpi.metadatabrowser.model.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIAMSNodeActionTest {

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
            return new CMDIMetadata();
        }
    };
    
    private Map<String, String> parameters = new HashMap<String, String>();

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
        String expResult = "ams";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of execute method, of class CMDIAMSNodeAction.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        NavigationActionRequest.setTarget(NavigationRequest.NavigationTarget.AMS);
        System.out.println(parameters);
        parameters.put("nodeId", Integer.toString(corpType.getNodeId()));
        parameters.put("jessionID", "number");
        NavigationActionRequest.setParameters(parameters);
        System.out.println(parameters);

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
        System.out.println(expResult);
        CMDIAMSNodeAction instance = new CMDIAMSNodeAction();
        NodeActionResult result = instance.execute(corpType);
        assertEquals("ams", instance.getName());
        // TODO review the generated test code and remove the default call to fail.
    }
}
