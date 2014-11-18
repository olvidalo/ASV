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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.NodeActionsConfiguration;
import nl.mpi.metadatabrowser.services.URIFilter;
import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.html5.media.MediaSource;
import org.wicketstuff.html5.media.video.Html5Video;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * Class that is responsible for video display in the browser
 */
public final class MediaFilePanel extends Panel {

    @SpringBean
    private NodeResolver resolver;
    @SpringBean
    private CorpusStructureProvider csdb;
    @SpringBean
    private URIFilter nodeUriFilter;

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);

    /**
     * Method that handle the html5 tag generation for video and build the
     * wicket front-end of the panel.
     *
     * @param id
     * @param node TypedCorpusNode, the node to be displayed as video
     */
    public MediaFilePanel(String id, TypedCorpusNode node) {
        super(id);
        final List<MediaSource> mm = new ArrayList<>();

        String nodeURL;
        try {
            // allow filter to rewrite, e.g. http->https
            nodeURL = nodeUriFilter.filterURI(resolver.getUrl(node).toURI()).toString();
        } catch (URISyntaxException ex) {
            // highly unlikely
            logger.warn("Node resolver URL was not a valid URI!" + ex.getMessage());
            nodeURL = resolver.getUrl(node).toString();
        }

        Label resourcelabel;
        add(new Label("viewTitle", "Viewing " + node.getName()));
        boolean hasmp4 = false; // use to display video without mp4 double. Alternative display to html5
        URI nodeParent = csdb.getCanonicalParent(node.getNodeURI());
        List<CorpusNode> childrenNodes = csdb.getChildNodes(nodeParent);
        for (CorpusNode childNode : childrenNodes) { // parent has children
            if (childNode.getName().endsWith(".mp4")) { // look up for children with mp4 extension
                String name = FilenameUtils.removeExtension(node.getName());
                String childNodeName = FilenameUtils.removeExtension(childNode.getName());
                if (name.equals(childNodeName)) { // name comparison. Should be same node if only extension differs within the same session

                    String childNodeURL;
                    try {
                        // allow filter to rewrite, e.g. http->https
                        childNodeURL = nodeUriFilter.filterURI(resolver.getUrl(childNode).toURI()).toString();
                    } catch (URISyntaxException ex) {
                        // highly unlikely
                        logger.warn("Node resolver URL was not a valid URI!" + ex.getMessage());
                        childNodeURL = resolver.getUrl(node).toString();
                    }

                    // allow filter to rewrite, e.g. http->https
                    mm.add(new MediaSource(childNodeURL, "video/mp4"));
//                  mm.add(new MediaSource(url in ogg format, "video/ogg.")); // ideally supported but not for now
                    hasmp4 = true;
                    resourcelabel = new Label("altView", ""); // used for wicket id
                    resourcelabel.setVisibilityAllowed(false);
                    add(resourcelabel);
                } else {
                    logger.error("video could not be found in mp4 format");
                }
            }
        }
        if (!hasmp4) { // backup to display video in iframe. No html5            
            StringBuilder sb = new StringBuilder();
            // create label for resource
            sb.append("<iframe id=\"viewFrame\" src=\"");
            sb.append(nodeURL);
            sb.append("\">");
            sb.append("</iframe>");
            resourcelabel = new Label("altView", sb.toString());
            resourcelabel.setEscapeModelStrings(false);
            add(resourcelabel);
        }

        IModel<List<MediaSource>> mediaSourceList = new AbstractReadOnlyModel<List<MediaSource>>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<MediaSource> getObject() {
                return mm;
            }
        };

        Html5Video video = (new Html5Video("displayVideo", mediaSourceList) {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean isControls() {
                return true;
            }

            @Override
            protected boolean isAutoPlay() {
                return true;
            }
        });

        if (!hasmp4) {
            video.setVisible(false);
        }

        add(video);

        add(new ExternalLink("viewVideo", nodeURL));
    }
}
