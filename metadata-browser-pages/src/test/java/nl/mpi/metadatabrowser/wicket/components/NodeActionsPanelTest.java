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
package nl.mpi.metadatabrowser.wicket.components;

import java.net.URI;
import java.util.Arrays;
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.wicket.AbstractWicketTest;
import nl.mpi.metadatabrowser.wicket.model.NodeActionsStructure;
import nl.mpi.metadatabrowser.wicket.services.ControllerActionRequestHandler;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class NodeActionsPanelTest extends AbstractWicketTest {

    private WicketTester tester;
    private Mockery context = new JUnit4Mockery();
    private ControllerActionRequestHandler actionRequestHandler;

    @Override
    public void setUp() {
	this.tester = getTester();
	actionRequestHandler = context.mock(ControllerActionRequestHandler.class);
	putBean("actionRequestHandler", actionRequestHandler);
    }

    @Test
    public void testRender() throws Exception {
	final NodeActionsStructure modelObject = new NodeActionsStructure();
	modelObject.setNodeUri(new URI("nodeUri"));

	// Add two actions to the panel
	final NodeAction action1 = context.mock(NodeAction.class, "action1");
	final NodeAction action2 = context.mock(NodeAction.class, "action2");
	context.checking(new Expectations() {
	    {
		oneOf(action1).getName();
		will(returnValue("action 1"));
		oneOf(action2).getName();
		will(returnValue("action 2"));
	    }
	});
	modelObject.setNodeActions(Arrays.asList(action1, action2));

	// Check the rendering
	final NodeActionsPanel panel = new NodeActionsPanel("panelId", modelObject);
	tester.startComponentInPage(panel);
	Component actionsForm = panel.get("nodeActionsForm");
	assertTrue(actionsForm instanceof Form);
	Component listView = panel.get("nodeActionsForm:nodeActions");
	assertTrue(listView instanceof ListView);
	assertEquals(2, ((ListView) listView).size());
	Component actionButton = panel.get("nodeActionsForm:nodeActions:0:nodeActionButton");
	assertTrue(actionButton instanceof NodeActionButton);
    }

    @Test
    public void testSubmitAction() throws Exception {
	// Prepare an action
	final NodeActionsStructure modelObject = new NodeActionsStructure();
	modelObject.setNodeUri(new URI("nodeUri"));

	final NodeAction action = context.mock(NodeAction.class);
	context.checking(new Expectations() {
	    {
		allowing(action).getName();
		will(returnValue("action name"));
	    }
	});
	modelObject.setNodeActions(Arrays.asList(action));

	final NodeActionsPanel panel = new NodeActionsPanel("panelId", modelObject);
	tester.startComponentInPage(panel);

	// Prepare for submitting form through action button
	final NodeActionResult actionResult = context.mock(NodeActionResult.class);
	final ControllerActionRequest actionRequest = context.mock(ControllerActionRequest.class);
	context.checking(new Expectations() {
	    {
		// Submission should execute the method
		oneOf(action).execute(new URI("nodeUri"));
		// which returns a result
		will(returnValue(actionResult));

		// Feedback message of result gets polled
		oneOf(actionResult).getFeedbackMessage();
		will(returnValue("feedback message"));

		// Controller action requests gets polled...
		oneOf(actionResult).getControllerActionRequest();
		will(returnValue(actionRequest));

		// ..and passed on to the action request handler
		oneOf(actionRequestHandler).handleActionRequest(tester.getRequestCycle(), actionRequest);
	    }
	});
	// Submit form...
	final FormTester formTester = tester.newFormTester(panel.getPageRelativePath() + ":nodeActionsForm");
	// ...using the action button
	formTester.submit(formTester.getForm().get("nodeActions:0:nodeActionButton"));
	// test whether feedback message matches
	tester.assertInfoMessages("feedback message");
    }
}
