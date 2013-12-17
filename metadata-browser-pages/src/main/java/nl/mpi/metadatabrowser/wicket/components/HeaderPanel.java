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

import javax.servlet.http.HttpServletRequest;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.NodeActionsConfiguration;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.servlet.XForwardedRequestWrapper;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public final class HeaderPanel extends Panel {

    @SpringBean
    private NodeActionsConfiguration nodeActionsConf; //TODO: Make separate headerConf and inject that to prevent dependency on CMDI impl

    public HeaderPanel(String id, HttpServletRequest request) {
	super(id);
	XForwardedRequestWrapper xf = new XForwardedRequestWrapper(request);
	String user = xf.getRemoteUser();

	add(new BookmarkablePageLink("aboutLink", AboutPage.class) {
	    @Override
	    protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.put("target", "_blank");
	    }
	});

	ExternalLink manualLink = new ExternalLink("manualLink", nodeActionsConf.getManualURL()) {
	    @Override
	    protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.put("target", "_blank");
	    }
	};

	ExternalLink registerLink = new ExternalLink("registerLink", nodeActionsConf.getRrsURL() + nodeActionsConf.getRrsRegister());

	ExternalLink userLoginLink;
	if (user == null || user.trim().equals("")) {
	    userLoginLink = new ExternalLink("userLoginLink", "loginPage.html");
	} else {
	    userLoginLink = new ExternalLink("userLoginLink", "loginPage.html");
	}

	Link<Void> userName = new Link<Void>("userName") {
	    @Override
	    public void onClick() {
		getBeforeDisabledLink();
	    }

	    @Override
	    public boolean isEnabled() {
		return super.isEnabled() && false;
	    }
	};
	userName.add(new Label("user", "user : " + user));
	add(userName);
	add(manualLink);
	add(registerLink);
	add(userLoginLink);
//        add(userLogoutLink);
    }
}
