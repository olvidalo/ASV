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
package nl.mpi.metadatabrowser.model.cmdi.nodeactions;

import java.net.URI;
import java.util.Collection;
import javax.ws.rs.core.UriBuilder;
import nl.mpi.metadatabrowser.model.StyleSpecifier;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.services.NodeIdFilter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * Class that call redirect to RRS
 */
@Component
public class CMDIRrsNodeAction extends EmbeddedPageNodeAction implements StyleSpecifier {

    private final NodeActionsConfiguration nodeActionsConfiguration;
    private final NodeIdFilter nodeIdFilter;

    @Autowired
    public CMDIRrsNodeAction(NodeActionsConfiguration nodeActionsConfiguration, NodeIdFilter nodeIdFilter) {
        this.nodeActionsConfiguration = nodeActionsConfiguration;
        this.nodeIdFilter = nodeIdFilter;
    }

    @Override
    public String getName() {
        return "Request Access";
    }

    @Override
    public String getTitle() {
        return "Request access to the selected branch or resource";
    }

    @Override
    public String getStyleClass() {
        return "RequestAccess";
    }

    @Override
    protected URI getTarget(Collection<TypedCorpusNode> nodes) {
        //Buil redirect to RRS
        final UriBuilder uriBuilder = UriBuilder.fromUri(nodeActionsConfiguration.getRrsURL() + nodeActionsConfiguration.getRrsIndexURL());
        for (TypedCorpusNode node : nodes) {
            final URI nodeId = node.getNodeURI();
            String nodeid = nodeIdFilter.getURIParam(nodeId);
            uriBuilder.queryParam("nodeid", nodeid).build();
        }
        return uriBuilder.build();
    }

    @Override
    protected IModel<String> getOpenInNewLabelModel() {
        return Model.of("Click here to request access to this node");
    }
}
