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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * Class that calls redirect to CMDI Search
 */
@Component
public class CMDISearchNodeAction extends RedirectingNodeAction implements NodeActionSingletonBean {

    private final NodeActionsConfiguration nodeActionsConfiguration;
    private final NodeIdFilter nodeIdFilter;

    /**
     *
     * @param nodeActionsConfiguration
     * @param nodeIdFilter filter that rewrites node IDs when passed as query
     * parameters
     * @param nodeResolver
     * @param nodeUriFilter filter that rewrites a node URL (e.g. to intercept
     * non-HTTPS URIs)
     */
    @Autowired
    CMDISearchNodeAction(NodeActionsConfiguration nodeActionsConfiguration, NodeIdFilter nodeIdFilter) {
        this.nodeActionsConfiguration = nodeActionsConfiguration;
        this.nodeIdFilter = nodeIdFilter;
    }

    @Override
    public String getName() {
        return "Metadata Search";
    }

    @Override
    public String getTitle() {
        return "Search the metadata descriptions of the archived material below the selected branch";
    }

    @Override
    protected URI getTarget(Collection<TypedCorpusNode> nodes) throws NodeActionException {
        final UriBuilder uriBuilder = UriBuilder.fromPath(nodeActionsConfiguration.getMdSearchURL());
        for (TypedCorpusNode node : nodes) {
            final URI nodeId = node.getNodeURI();
            final String nodeid = nodeIdFilter.getURIParam(nodeId);
            uriBuilder.queryParam("nodeid", nodeid);
        }
        return uriBuilder.build();
    }
}
