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
package nl.mpi.metadatabrowser.services.impl.cmdi;

import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreeNodeIconProvider;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.services.NodeIconProvider;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDINodeIconProvider implements NodeIconProvider{
    private final ResourceReference sessionIcon = new PackageResourceReference(CMDINodeIconProvider.class, "session_color.gif");
    private final ResourceReference corpusIcon = new PackageResourceReference(CMDINodeIconProvider.class, "corpusnode_color.gif");
    private final ResourceReference fileIcon = new PackageResourceReference(CMDINodeIconProvider.class, "mediafile.gif");
    private final NodeTypeIdentifier nodeTypeIdentifier;

    public CMDINodeIconProvider(NodeTypeIdentifier nodeTypeIdentifier) {
	this.nodeTypeIdentifier = nodeTypeIdentifier;
    }

//    @Override
//    public ResourceReference getNodeIcon(T contentNode) {
//	final NodeType nodeType = nodeTypeIdentifier.getNodeType(contentNode);
//	if (nodeType.getName().equalsIgnoreCase("Collection")) {
//	    return corpusIcon;
//	} else if (nodeType.getName().equalsIgnoreCase("Root")) {
//	    return corpusIcon;
//	} else if (nodeType.getName().equalsIgnoreCase("Resource")) {
//	    return fileIcon;
//	} else {
//	    return sessionIcon;
//	}
//    }

    @Override
    public ResourceReference getNodeIcon(CorpusNode contentNode) {
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
    }

    
}
