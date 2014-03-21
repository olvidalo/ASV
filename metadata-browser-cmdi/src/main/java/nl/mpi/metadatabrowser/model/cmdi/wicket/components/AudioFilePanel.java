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

import java.util.ArrayList;
import java.util.List;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.NodeActionsConfiguration;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.html5.media.MediaSource;
import org.wicketstuff.html5.media.audio.Html5Audio;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public final class AudioFilePanel extends Panel {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);

    /**
     * Method that handle the html5 tag generation for audio and build the
     * wicket front-end of the panel.
     *
     * @param id
     * @param resolver NodeResolver, used to retrieve the node URL
     * @param node TypedCorpusNode, the node to be displayed as video
     * @param nodeActionsConfiguration NodeActionsConfiguration, configurations
     * to convert Http to Https
     * @param csdb CorpusStructureProvider, access corpusStructure to get
     * parents and children.
     */
    public AudioFilePanel(String id, NodeResolver resolver, TypedCorpusNode node, NodeActionsConfiguration nodeActionsConfiguration, CorpusStructureProvider csdb) {
        super(id);
        final List<MediaSource> mm = new ArrayList<MediaSource>();
        final String nodeURL = nodeActionsConfiguration.processLinkProtocol(resolver.getUrl(node).toString(), nodeActionsConfiguration.getForceHttpOrHttps().equals("https"));
        Label resourcelabel;
        boolean haswav = true;
        add(new Label("viewTitle", node.getName()));
        if (node.getName().endsWith(".wav")) {
            mm.add(new MediaSource(nodeURL.toString()));
//                  mm.add(new MediaSource(url in ogg format)); // ideally supported but not for now
            resourcelabel = new Label("altView", ""); // used for wicket id
            resourcelabel.setVisibilityAllowed(false);
            add(resourcelabel);
        } else { // backup to display audio in iframe. No html5
            StringBuilder sb = new StringBuilder();
            // create label for resource
            sb.append("<iframe id=\"viewFrame\" src=\"");
            sb.append(nodeURL.toString());
            sb.append("\">");
            sb.append("</iframe>");
            resourcelabel = new Label("altView", sb.toString());
            resourcelabel.setEscapeModelStrings(false);
            add(resourcelabel);
            haswav = false;
        }

        IModel<List<MediaSource>> mediaSourceList = new AbstractReadOnlyModel<List<MediaSource>>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<MediaSource> getObject() {
                return mm;
            }
        };

        Html5Audio audio = (new Html5Audio("displayAudio", mediaSourceList) {
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
        if (!haswav) {
            audio.setVisible(false);
        }
        add(audio);

        add(new ExternalLink("viewAudio", nodeURL.toString())); // let the browser handle the display if html5 is not supported.
    }
}
