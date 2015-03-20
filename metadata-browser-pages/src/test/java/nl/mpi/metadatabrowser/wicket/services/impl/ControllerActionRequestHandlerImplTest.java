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

import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.DownloadRequest;
import nl.mpi.metadatabrowser.model.NavigationRequest;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.wicket.services.ControllerActionRequestHandler;
import nl.mpi.metadatabrowser.wicket.services.RequestHandlerException;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.mock.MockHomePage;
import org.apache.wicket.request.cycle.RequestCycle;
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
    private Page page;
    private RequestCycle requestCycle;
    private ControllerActionRequestHandlerImpl instance;
    private ControllerActionRequestHandler<NavigationRequest> navigationRequestHandler;
    private ControllerActionRequestHandler<DownloadRequest> downloadRequestHandler;
    private ControllerActionRequestHandler<ShowComponentRequest> showComponentRequestHandler;
    private ControllerActionRequestHandler selectActionRequestHandler;
    private AjaxRequestTarget ajaxRequestTarget;
    
    @Before
    public void setUp() {
        tester = new WicketTester();
        requestCycle = tester.getRequestCycle();
        page = new MockHomePage();
        navigationRequestHandler = context.mock(ControllerActionRequestHandler.class, "NavigationRequest");
        downloadRequestHandler = context.mock(ControllerActionRequestHandler.class, "DownloadRequest");
        showComponentRequestHandler = context.mock(ControllerActionRequestHandler.class, "ShowComponentRequest");
        selectActionRequestHandler = context.mock(ControllerActionRequestHandler.class, "SelectActionRequest");
        ajaxRequestTarget = context.mock(AjaxRequestTarget.class);
        instance = new ControllerActionRequestHandlerImpl(navigationRequestHandler, downloadRequestHandler, showComponentRequestHandler, selectActionRequestHandler);
    }

    @Test
    public void testHandleNavigationRequest() throws Exception {
        final NavigationRequest request = context.mock(NavigationRequest.class);
        context.checking(new Expectations() {
            {
                oneOf(navigationRequestHandler).handleActionRequest(requestCycle, request, page, ajaxRequestTarget);
            }
        });
        instance.handleActionRequest(requestCycle, request, page, ajaxRequestTarget);
    }

    @Test
    public void testHandleDownloadRequest() throws Exception {
        final DownloadRequest request = context.mock(DownloadRequest.class);
        context.checking(new Expectations() {
            {
                oneOf(downloadRequestHandler).handleActionRequest(requestCycle, request, page, ajaxRequestTarget);
            }
        });
        instance.handleActionRequest(requestCycle, request, page, ajaxRequestTarget);
    }

    @Test
    public void testHandleShowComponentRequest() throws Exception {
        final ShowComponentRequest request = context.mock(ShowComponentRequest.class);
        context.checking(new Expectations() {
            {
                oneOf(showComponentRequestHandler).handleActionRequest(requestCycle, request, page, ajaxRequestTarget);
            }
        });
        instance.handleActionRequest(requestCycle, request, page, ajaxRequestTarget);
    }

    @Test(expected = RequestHandlerException.class)
    public void handleUnknown() throws Exception {
        final ControllerActionRequest request = context.mock(ControllerActionRequest.class);
        instance.handleActionRequest(requestCycle, request, page, ajaxRequestTarget);
    }
}
