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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
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
        add(new Label("viewTitle", "Viewing " + node.getName()));

        final List<MediaSource> html5media = findHtml5alternative(node);

        // add view for case where no HTML5 resources available
        add(new ExternalFramePanel("altView", resolver.getUrl(node).toString()) {

            @Override
            public boolean isVisible() {
                return html5media == null;
            }

        });

        // add view for case where HTML5 resources is available
        Html5Video video = (new Html5Video("displayVideo", createMediaModel(html5media)) {
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

        add(video);
        add(new ExternalLink("viewVideo", getNodeUrl(node)));
    }

    private List<MediaSource> findHtml5alternative(TypedCorpusNode node) {
        final URI nodeParent = csdb.getCanonicalParent(node.getNodeURI());
        final String parentName = getFilename(node);
        final String parentBaseName = FilenameUtils.removeExtension(parentName);
        final List<CorpusNode> childrenNodes = csdb.getChildNodes(nodeParent);

        final List<MediaSource> mm = new ArrayList<>();
        for (CorpusNode childNode : childrenNodes) { // parent has children
            final String childNodeName = getFilename(childNode);
            if (childNodeName.endsWith(".mp4")) { // look up for children with mp4 extension
                // name comparison. Should be same node if only extension differs within the same session
                final String childBaseName = FilenameUtils.removeExtension(childNodeName);
                if (parentBaseName.equals(childBaseName)) {
                    logger.debug("Found MP4 alternative for {}: {}", node, childNode);
                    String childNodeURL = getNodeUrl(childNode);
                    mm.add(new MediaSource(childNodeURL, "video/mp4"));
//                  mm.add(new MediaSource(url in ogg format, "video/ogg.")); // ideally supported but not for now
                }
            }
        }
        if (mm.isEmpty()) {
            return null;
        } else {
            return mm;
        }
    }

    private String getNodeUrl(CorpusNode node) {
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

    private String getFilename(CorpusNode node) {
        final File localFile = resolver.getLocalFile(node);
        if (localFile != null) {
            return localFile.getName();
        } else {
            //fallback, best effort
            return node.getName();
        }
    }

    private IModel<List<MediaSource>> createMediaModel(final List<MediaSource> html5media) {
        IModel<List<MediaSource>> mediaSourceList = new AbstractReadOnlyModel<List<MediaSource>>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<MediaSource> getObject() {
                return html5media;
            }
        };
        return mediaSourceList;
    }
}
