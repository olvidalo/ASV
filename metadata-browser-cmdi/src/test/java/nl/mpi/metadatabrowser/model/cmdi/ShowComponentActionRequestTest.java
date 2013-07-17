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

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
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
public class ShowComponentActionRequestTest {
    
    
    public ShowComponentActionRequestTest() {
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
     * Test of setTextArea method, of class ShowComponentActionRequest.
     */
    @Test
    public void testSetTextArea() {
        System.out.println("setTextArea"); 
        WicketTester tester = new WicketTester();
        TextArea<String> content = new TextArea<String>("testSetTextArea");
        IModel<String> textModel = Model.of("this is a test textArea");
        content.setModel(textModel);
        assertEquals("this is a test textArea", content.getValue());
        ShowComponentActionRequest instance = new ShowComponentActionRequest(content, null);
        instance.setTextArea(content);
        assertNotNull(instance.content);
        assertEquals("this is a test textArea", instance.content.getValue());
        assertEquals("testSetTextArea", instance.content.getId());
    }

    /**
     * Test of getComponent method, of class ShowComponentActionRequest.
     */
    @Test
    public void testGetComponent() {
        System.out.println("getComponent");
        WicketTester tester = new WicketTester();
        String id = "test";
        TextArea<String> content = new TextArea<String>(id);
        ShowComponentActionRequest instance = new ShowComponentActionRequest(content, null);
        Component result = instance.getComponent(id);
        assertNotNull(result);
        assertEquals("test", result.getId());
    }
}