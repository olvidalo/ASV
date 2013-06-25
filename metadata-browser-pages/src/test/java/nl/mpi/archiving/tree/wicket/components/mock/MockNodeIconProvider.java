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
package nl.mpi.archiving.tree.wicket.components.mock;

import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreeNodeIconProvider;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifierException;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class MockNodeIconProvider<T extends CorpusNode> implements ArchiveTreeNodeIconProvider<T> {
    
    private final static Logger logger = LoggerFactory.getLogger(MockNodeIconProvider.class);
    private final ResourceReference sessionIcon = new PackageResourceReference(MockNodeIconProvider.class, "session_color.gif");
    private final ResourceReference corpusIcon = new PackageResourceReference(MockNodeIconProvider.class, "corpusnode_color.gif");
    private final ResourceReference fileIcon = new PackageResourceReference(MockNodeIconProvider.class, "mediafile.gif");
    private final NodeTypeIdentifier nodeTypeIdentifier;
    
    public MockNodeIconProvider(NodeTypeIdentifier nodeTypeIdentifier) {
	this.nodeTypeIdentifier = nodeTypeIdentifier;
    }
    
    @Override
    public ResourceReference getNodeIcon(T contentNode) {
	try {
	    final NodeType nodeType = nodeTypeIdentifier.getNodeType(contentNode);
	    if (nodeType.getName().equalsIgnoreCase("Collection")) {
		return corpusIcon;
	    } else if (nodeType.getName().equalsIgnoreCase("Root")) {
		return corpusIcon;
	    } else if (nodeType.getName().equalsIgnoreCase("Resource")) {
		return fileIcon;
	    } else {
		return sessionIcon;
	    }
	} catch (NodeTypeIdentifierException ex) {
	    logger.error("Error determining node type for icon", ex);
	    return fileIcon;
	}
    }
}
