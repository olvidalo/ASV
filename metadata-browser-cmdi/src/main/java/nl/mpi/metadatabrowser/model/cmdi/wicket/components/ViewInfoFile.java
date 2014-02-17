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
package nl.mpi.metadatabrowser.model.cmdi.wicket.components;

import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.NodeActionsConfiguration;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public final class ViewInfoFile extends Panel {

    @SpringBean
    NodeActionsConfiguration nodeActionsConfiguration;

    public ViewInfoFile(String id, NodeResolver nodeResolver, TypedCorpusNode node) {
        super(id);
        //final MarkupContainer viewContainer = new WebMarkupContainer("viewContainer");
        final String nodeURL = nodeActionsConfiguration.processLinkProtocol(nodeResolver.getUrl(node).toString(), nodeActionsConfiguration.getForceHttpOrHttps().equals("https"));

//        if (node.getNodeType() instanceof IMDIResourcePictureType) {

        StringBuilder sb = new StringBuilder();
        Label resourcelabel;
        // create label for resource
        sb.append("<iframe id=\"viewFrame\" src=\"");
        sb.append(nodeURL.toString());
        sb.append("\">");
        sb.append("</iframe>");
        resourcelabel = new Label("infoView", sb.toString());
        resourcelabel.setEscapeModelStrings(false);
        add(resourcelabel);
//        add(viewContainer);

//        } else {
//        final IResourceStream resStream = new CorpusNodeResourceStream(nodeResolver, node);
//            add(new DocumentInlineFrame("infoView", resStream));
//        }

    }
}
