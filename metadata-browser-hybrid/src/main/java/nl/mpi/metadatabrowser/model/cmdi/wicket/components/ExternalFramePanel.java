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

import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletRequest;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Panel that renders an iframe for an external link if it is 'safe' (that is,
 * if it does not constitute a scheme switch, for example a http frame inside a
 * page resulting from a https request). If it is not safe, a fallback link will
 * be shown that opens the external link in a new tab.
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class ExternalFramePanel extends Panel {

    private static final long serialVersionUID = 1L;

    private final IModel<String> redirectURL;
    private final static Logger logger = LoggerFactory.getLogger(ExternalFramePanel.class);

    public ExternalFramePanel(String id, String redirectURL) {
        this(id, Model.of(redirectURL));
    }

    public ExternalFramePanel(String id, IModel<String> redirectURL) {
        super(id);
        this.redirectURL = redirectURL;

        // create external link (will be hidden of safe for frames)
        add(createExternalLink("linkContainer"));
        // create frame (will be hidden of not safe for frames)
        add(createFrameLabel("iframe"));
    }

    private boolean determineFrameSafe(String targetUrlString) {
        // check whether target and current request share scheme 
        try {
            final URI target = new URI(targetUrlString);
            // try to obtain http servlet request
            final Object containerRequest = getRequest().getContainerRequest();
            if (containerRequest instanceof HttpServletRequest) {
                final HttpServletRequest servletRequest = (HttpServletRequest) containerRequest;
                final URI current = new URI(servletRequest.getRequestURL().toString());
                return target.getScheme().equals(current.getScheme());
                //&& target.getHost().equals(current.getHost()); //also require host equality?
            } else {
                // no servlet request available, assume worst case
                return false;
            }
        } catch (URISyntaxException ex) {
            logger.warn("Invalid URI while checking whether resource is safe for iframe", ex);
            return false;
        }
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
                return String.format("<iframe id=\"iframe\" class=\"externalPage\" src=\"%1$s\"></iframe>", redirectURL.getObject());
            }
        };

        final Label resourcelabel = new Label(id, labelModel) {

            @Override
            protected void onConfigure() {
                setVisible(determineFrameSafe(redirectURL.getObject()));
            }

        };
        resourcelabel.setEscapeModelStrings(false); // render HTML as is
        return resourcelabel;
    }

    private Component createExternalLink(String link) {
        final WebMarkupContainer linkContainer = new WebMarkupContainer(link) {

            @Override
            protected void onConfigure() {
                setVisible(!determineFrameSafe(redirectURL.getObject()));
            }

        };
        linkContainer.add(new ExternalLink("link", redirectURL, getOpenInNewLabelModel()));
        return linkContainer;
    }

    /**
     * Override to provide a custom link text
     *
     * @return In this implementation, a model wrapping the constant "Click here
     * to see the resource"
     */
    protected IModel<String> getOpenInNewLabelModel() {
        return Model.of("Click here to see the resource");
    }

}
