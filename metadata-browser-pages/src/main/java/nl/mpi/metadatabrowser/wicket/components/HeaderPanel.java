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

import nl.mpi.metadatabrowser.model.cmdi.nodeactions.NodeActionsConfiguration;
import nl.mpi.metadatabrowser.services.AuthenticationHolder;
import nl.mpi.metadatabrowser.wicket.HomePage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public final class HeaderPanel extends Panel {

    @SpringBean
    private NodeActionsConfiguration nodeActionsConf; //TODO: Make separate headerConf and inject that to prevent dependency on CMDI impl

    /**
     *
     * @param id component id
     * @param userModel model that provides the principal name of the current
     * user
     */
    public HeaderPanel(String id, final IModel<String> userModel) {
        super(id);

        add(new BookmarkablePageLink("aboutLink", AboutPage.class) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("target", "_blank");
            }
        });

        BookmarkablePageLink homeLink = new BookmarkablePageLink("homeLink", HomePage.class);
//        ExternalLink homeLink = new ExternalLink("homeLink", "");
//        Image image = new Image("homeImg", new ContextRelativeResource("home.png"));
//        homeLink.add(image);

        ExternalLink manualLink = new ExternalLink("manualLink", nodeActionsConf.getManualURL()) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("target", "_blank");
            }
        };

        ExternalLink registerLink = new ExternalLink("registerLink", nodeActionsConf.getRrsURL() + nodeActionsConf.getRrsRegister());

        final Component userLoginLink = new Link("userLoginLink") {
            @Override
            public void onClick() {
                final String loginUrl;
                if (AuthenticationHolder.ANONYMOUS_PRINCIPAL.equals(userModel.getObject())) {
                    loginUrl = "login.jsp?login=1"; //TODO: add app state parameters
                } else {
                    loginUrl = "logoutPage.html?logout=1";
                }
                getRequestCycle().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler(loginUrl));
            }

        }.add(new Label("loginLabel", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                if (AuthenticationHolder.ANONYMOUS_PRINCIPAL.equals(userModel.getObject())) {
                    return "Log in";
                } else {
                    return "Log out";
                }
            }
        }));

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
        userName.add(new Label("user", userModel));
        add(userName);
        add(manualLink);
        add(registerLink);
        add(userLoginLink);
        add(homeLink);
//        add(userLogoutLink);
    }
}
