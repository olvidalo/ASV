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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import nl.mpi.metadatabrowser.model.DownloadRequest;
import nl.mpi.metadatabrowser.model.NavigationRequest;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.tester.WicketTester;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class ControllerActionRequestHandlerImplTest {

    public final String rrsUrl = "RRSURL";
    private final Mockery context = new JUnit4Mockery();
    private WicketTester tester;
    private RequestCycle requestCycle;
    private ControllerActionRequestHandlerImpl instance;

    @Before
    public void setUp() {
	tester = new WicketTester();
	requestCycle = tester.getRequestCycle();
	instance = new ControllerActionRequestHandlerImpl();
	instance.setRrsUrl(rrsUrl);
    }

    /**
     * Test of handleActionRequest method, of class ControllerActionRequestHandlerImpl.
     */
    @Test
    public void testHandleNavigationRequest() {

	final NavigationRequest actionRequest = context.mock(NavigationRequest.class);
	context.checking(new Expectations() {
	    {
		oneOf(actionRequest).getTarget();
		will(returnValue(NavigationRequest.NavigationTarget.RRS));
		allowing(actionRequest).getParameters();
	    }
	});
	instance.handleActionRequest(requestCycle, actionRequest);
	//TODO: Assert redirect
	//TODO: Test for other targets
    }

    @Test
    public void testDownloadRequest() throws Exception {
	final String fileName = "filename";

	final DownloadRequest actionRequest = context.mock(DownloadRequest.class);
	final IResourceStream resourceStream = context.mock(IResourceStream.class);
	final InputStream inputStream = new ByteArrayInputStream("stream content".getBytes());
	context.checking(new Expectations() {
	    {
		oneOf(actionRequest).getFileName();
		will(returnValue(fileName));
		oneOf(actionRequest).getDownloadStream();
		will(returnValue(resourceStream));

		oneOf(resourceStream).getInputStream();
		will(returnValue(inputStream));
		allowing(resourceStream).getContentType();
		will(returnValue("test/content-type"));
	    }
	});
	instance.handleActionRequest(requestCycle, actionRequest);
	//TODO: Assert download
    }
}
