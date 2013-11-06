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
package nl.mpi.metadatabrowser.wicket.services.impl;

import nl.mpi.metadatabrowser.model.NavigationRequest;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.NodeActionsConfiguration;
import nl.mpi.metadatabrowser.wicket.services.RequestHandlerException;
import org.apache.wicket.mock.MockHomePage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.tester.WicketTester;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;

import static org.jmock.Expectations.returnValue;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class NavigationRequestHandlerTest {

        @Autowired
    private NodeActionsConfiguration nodeActionsConfiguration;
    private final Mockery context = new JUnit4Mockery();
    private WicketTester tester;
    private RequestCycle requestCycle;
    private NavigationRequestHandler instance;

    @Before
    public void setUp() {
	tester = new WicketTester();
	requestCycle = tester.getRequestCycle();
	instance = new NavigationRequestHandler();
    }

    /**
     * Test of handleActionRequest method, of class NavigationRequestHandler.
     */
    @Test
    public void testHandleActionRequest() throws RequestHandlerException {

	final NavigationRequest actionRequest = context.mock(NavigationRequest.class);
	context.checking(new Expectations() {
	    {
		allowing(actionRequest).getTargetURL();
		will(returnValue(nodeActionsConfiguration.getRrsURL()));
	    }
	});
	instance.handleActionRequest(requestCycle, actionRequest, new MockHomePage());
	//TODO: Assert redirect to rrs url
	//TODO: Test for other targets
    }
}