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
import nl.mpi.metadatabrowser.model.NodeActionSingletonBean;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.services.NodeIdFilter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * @author Twan Goosen <twan.goosen@mpi.nl>
 * Class Action to call AMS (Manage Access Rights).
 */
@Component
public class CMDIAMSNodeAction extends EmbeddedPageNodeAction implements NodeActionSingletonBean {

    private final NodeActionsConfiguration nodeActionsConfiguration;
    private final NodeIdFilter filterIdProvider;

    @Autowired
    public CMDIAMSNodeAction(NodeActionsConfiguration nodeActionsCongiguration, NodeIdFilter filterIdProvider) {
        this.nodeActionsConfiguration = nodeActionsCongiguration;
        this.filterIdProvider = filterIdProvider;
    }

    @Override
    public String getName() {
        return "Manage Access";
    }

    @Override
    public String getTitle() {
        return "Manage access permissions for the selected branch (requires management permissions)";
    }

    @Override
    protected URI getTarget(Collection<TypedCorpusNode> nodes) throws NodeActionException {
        // assuming a single node
        if (nodes.size() != 1) {
            throw new NodeActionException(this, "Action only suitable one single node selections");
        }
        final TypedCorpusNode node = nodes.iterator().next();

        // Build redirect to AMS
        final URI nodeId = node.getNodeURI();
        final String nodeid = filterIdProvider.getURIParam(nodeId);
        return UriBuilder.fromUri(nodeActionsConfiguration.getAmsURL()).queryParam("nodeid", nodeid).build();
    }

    @Override
    protected IModel<String> getOpenInNewLabelModel() {
        return Model.of("Click here to manage node access");
    }

}
