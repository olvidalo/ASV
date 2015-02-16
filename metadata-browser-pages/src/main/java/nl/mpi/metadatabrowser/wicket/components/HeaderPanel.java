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
import nl.mpi.metadatabrowser.wicket.HomePage;
import nl.mpi.metadatabrowser.wicket.UserModel;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
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
     * @param userModel model that provides the principal name of the current user
     */
    public HeaderPanel(String id, IModel<String> userModel) {
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

        ExternalLink userLoginLink;
        if (UserModel.ANONYMOUS_PRINCIPAL.equals(userModel.getObject())) {
            userLoginLink = new ExternalLink("userLoginLink", "login.jsp", "login");
        } else {
            userLoginLink = new ExternalLink("userLoginLink", "logoutPage.html", "logout");
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
        userName.add(new Label("user", userModel));
        add(userName);
        add(manualLink);
        add(registerLink);
        add(userLoginLink);
        add(homeLink);
//        add(userLogoutLink);
    }
}
