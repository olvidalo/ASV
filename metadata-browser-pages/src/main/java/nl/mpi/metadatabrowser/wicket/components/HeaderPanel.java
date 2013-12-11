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
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public final class HeaderPanel extends Panel {

    @SpringBean
    private NodeActionsConfiguration nodeActionsConf;

    public HeaderPanel(String id) {
        super(id);
        add(new BookmarkablePageLink("aboutLink", AboutPage.class){
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("target", "_blank");
            }
        });

        ExternalLink manualLink = new ExternalLink("manualLink", nodeActionsConf.getManualURL()){
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("target", "_blank");
            }
        }; 

        ExternalLink registerLink = new ExternalLink("registerLink", nodeActionsConf.getRrsURL()+nodeActionsConf.getRrsRegister());

        
        ExternalLink userLoginLink = new ExternalLink("userLoginLink", "loginPage.html");
//        ExternalLink userLogoutLink = new ExternalLink("userLogoutLink", "/protected/logout.jsp" );

        add(manualLink);
        add(registerLink);
        add(userLoginLink);
//        add(userLogoutLink);
    }
}
