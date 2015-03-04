/*
 * Copyright (C) 2015 Max Planck Institute for Psycholinguistics
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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collection;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.StyleSpecifier;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.services.NodeIdFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
@Component
public class ViewImagesAction extends RedirectingNodeAction implements StyleSpecifier {
    
    private final static String IMEX_URL_FORMAT = "%s?nodeid=%s";
    
    private final NodeIdFilter nodeIdFilter;
    private final NodeActionsConfiguration nodeActionsConfiguration;
    
    @Autowired
    public ViewImagesAction(NodeIdFilter nodeIdFilter, NodeActionsConfiguration nodeActionsConfiguration) {
        this.nodeIdFilter = nodeIdFilter;
        this.nodeActionsConfiguration = nodeActionsConfiguration;
    }
    
    @Override
    protected URI getTarget(Collection<TypedCorpusNode> nodes) throws NodeActionException {
        if (nodes.size() == 1) {
            final TypedCorpusNode node = nodes.iterator().next();
            
            final String imexBaseUrl = nodeActionsConfiguration.getImexUrl();
            final String nodeIdParam = getNodeIdParam(node);
            final String uri = String.format(IMEX_URL_FORMAT, imexBaseUrl, nodeIdParam);
            
            return URI.create(uri);
        } else {
            throw new NodeActionException(this, "action not available for multiple selections");
        }
    }
    
    private String getNodeIdParam(final TypedCorpusNode node) throws NodeActionException {
        try {
            return URLEncoder.encode(nodeIdFilter.getURIParam(node.getNodeURI()), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new NodeActionException(this, "Could not create node ID parameter for Imex", ex);
        }
    }
    
    @Override
    public String getName() {
        return "View images";
    }
    
    @Override
    public String getTitle() {
        return "View all images in this session";
    }

    @Override
    public String getStyleClass() {
        return "ViewImages";
    }
    
}
