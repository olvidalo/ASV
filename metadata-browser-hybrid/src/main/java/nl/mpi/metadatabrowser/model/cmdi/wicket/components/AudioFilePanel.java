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
package nl.mpi.metadatabrowser.model.cmdi.wicket.components;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.services.URIFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.html5.media.MediaSource;
import org.wicketstuff.html5.media.audio.Html5Audio;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public final class AudioFilePanel extends Panel {

    @SpringBean
    private NodeResolver resolver;
    @SpringBean
    private URIFilter nodeUriFilter;

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);

    /**
     * Method that handle the html5 tag generation for audio and build the
     * wicket front-end of the panel.
     *
     * @param id
     * @param node TypedCorpusNode, the node to be displayed as video
     */
    public AudioFilePanel(String id, TypedCorpusNode node) {
        super(id);
        final String nodeURL = getNodeUrl(node);
        add(new Label("viewTitle", "Listening to " + node.getName()));

        final List<MediaSource> html5media = findHtml5Media(nodeURL);

        // backup to display audio in iframe. No html5
        add(new ExternalFramePanel("altView", resolver.getUrl(node).toString()) {

            @Override
            public boolean isVisible() {
                return html5media == null;
            }

        });

        // add view for case where HTML5 resources is available
        final Html5Audio audio = (new Html5Audio("displayAudio", createMediaModel(html5media)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean isControls() {
                return true;
            }

            @Override
            protected boolean isAutoPlay() {
                return true;
            }

            @Override
            public boolean isVisible() {
                return html5media != null;
            }
        });
        add(audio);

        add(new ExternalLink("viewAudio", nodeURL.toString())); // let the browser handle the display if html5 is not supported.
    }

    private List<MediaSource> findHtml5Media(final String nodeURL) {
        final List<MediaSource> mm = new ArrayList<MediaSource>();
        if (nodeURL.endsWith(".wav")) {
            mm.add(new MediaSource(nodeURL));
            //mm.add(new MediaSource(url in ogg format)); // ideally supported but not for now
        }
        if (mm.isEmpty()) {
            return null;
        } else {
            return mm;
        }
    }

    private String getNodeUrl(TypedCorpusNode node) {
        String nodeURL;
        try {
            // allow filter to rewrite, e.g. http->https
            nodeURL = nodeUriFilter.filterURI(resolver.getUrl(node).toURI()).toString();
        } catch (URISyntaxException ex) {
            // highly unlikely
            logger.warn("Node resolver URL was not a valid URI!" + ex.getMessage());
            nodeURL = resolver.getUrl(node).toString();
        }
        return nodeURL;
    }

    private IModel<List<MediaSource>> createMediaModel(final List<MediaSource> html5media) {
        final IModel<List<MediaSource>> mediaSourceList = new AbstractReadOnlyModel<List<MediaSource>>() {
            private static final long serialVersionUID = 1L;
            
            @Override
            public List<MediaSource> getObject() {
                return html5media;
            }
        };
        return mediaSourceList;
    }
}
