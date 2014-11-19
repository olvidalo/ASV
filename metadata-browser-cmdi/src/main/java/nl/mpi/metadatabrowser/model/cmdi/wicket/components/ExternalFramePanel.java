/*
 * Copyright (C) 2014 Max Planck Institute for Psycholinguistics
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
/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
package nl.mpi.metadatabrowser.model.cmdi.wicket.components;

import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class ExternalFramePanel extends Panel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    final boolean safeForFrame;
    final IModel<String> redirectURL;

    public ExternalFramePanel(String id, String redirectURL) {
        this(id, Model.of(redirectURL));
    }

    public ExternalFramePanel(String id, IModel<String> redirectURL) {
        super(id);
        this.redirectURL = redirectURL;
        this.safeForFrame = true; //TOOD: determine dynamically

        // create external link (will be hidden of safe for frames)
        add(createExternalLink("linkContainer"));
        // create frame (will be hidden of not safe for frames)
        add(createFrameLabel("iframe"));
    }

    /**
     * Creates label to render iframe
     *
     * Note twagoo 2014/11/18: using a WebMarkupContainer would be much nicer
     * but this somehow causes the AJAX updating of the tree to break :(
     *
     * @param redirectURL
     * @return
     */
    private Label createFrameLabel(String id) {
        final IModel<String> labelModel = new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                return String.format("<iframe id=\"iframe\" width=\"100%%\" height=\"100%%\" align=\"center\" src=\"%1$s\"></iframe>", redirectURL.getObject());
            }
        };

        final Label resourcelabel = new Label(id, labelModel) {

            @Override
            public boolean isVisible() {
                return safeForFrame;
            }

        };
        // render HTML as is
        resourcelabel.setEscapeModelStrings(false);
        return resourcelabel;
    }

    private Component createExternalLink(String link) {
        final WebMarkupContainer linkContainer = new WebMarkupContainer(link) {

            @Override
            public boolean isVisible() {
                return !safeForFrame;
            }

        };
        linkContainer.add(new ExternalLink("link", redirectURL));
        return linkContainer;
    }

}
