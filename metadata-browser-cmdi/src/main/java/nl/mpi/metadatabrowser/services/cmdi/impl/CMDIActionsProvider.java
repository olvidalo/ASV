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
package nl.mpi.metadatabrowser.services.cmdi.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.tree.services.NodeResolver;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.CMDICollectionType;
import nl.mpi.metadatabrowser.model.cmdi.CMDIMetadata;
import nl.mpi.metadatabrowser.model.cmdi.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.CMDIResourceType;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDIAMSNodeAction;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDIBookmarkNodeAction;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDIDownloadNodeAction;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDIMultipleDownloadNodeAction;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDIRrsNodeAction;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDISearchNodeAction;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDIStatsNodeAction;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDITrovaNodeAction;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDIVersionNodeAction;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDIViewNodeAction;
import nl.mpi.metadatabrowser.services.NodeActionsProvider;
import nl.mpi.metadatabrowser.services.cmdi.ZipService;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIActionsProvider implements NodeActionsProvider {

    private final List<NodeAction> resourcetxtNodeActionList;
    private final List<NodeAction> resourceAudioVideoNodeActionList;
    public final List<NodeAction> metadataNodeActionList;
    public final List<NodeAction> collectionNodeActionList;
    public final List<NodeAction> multipleNodeActionList;

    public CMDIActionsProvider(CorpusStructureProvider csdb, NodeResolver nodeResolver, ZipService zipService) {
	metadataNodeActionList = Arrays.<NodeAction>asList(
		new CMDISearchNodeAction(),
		new CMDITrovaNodeAction(),
		new CMDIAMSNodeAction(),
		new CMDIRrsNodeAction(),
		new CMDIStatsNodeAction(),
		new CMDIBookmarkNodeAction(csdb),
		new CMDIDownloadNodeAction(csdb, nodeResolver),
		new CMDIMultipleDownloadNodeAction(csdb, zipService),
		new CMDIVersionNodeAction(csdb, nodeResolver));

	collectionNodeActionList = Arrays.<NodeAction>asList(
		new CMDISearchNodeAction(),
		new CMDITrovaNodeAction(),
		new CMDIAMSNodeAction(),
		new CMDIRrsNodeAction(),
		new CMDIBookmarkNodeAction(csdb),
		new CMDIDownloadNodeAction(csdb, nodeResolver));

	resourceAudioVideoNodeActionList = Arrays.<NodeAction>asList(
		new CMDIAMSNodeAction(),
		new CMDIRrsNodeAction(),
		new CMDIStatsNodeAction(),
		new CMDIViewNodeAction(nodeResolver),
		new CMDIBookmarkNodeAction(csdb),
		new CMDIDownloadNodeAction(csdb, nodeResolver),
		new CMDIVersionNodeAction(csdb, nodeResolver));

	resourcetxtNodeActionList = Arrays.<NodeAction>asList(
		new CMDITrovaNodeAction(),
		new CMDIAMSNodeAction(),
		new CMDIRrsNodeAction(),
		new CMDIStatsNodeAction(),
		new CMDIViewNodeAction(nodeResolver),
		new CMDIBookmarkNodeAction(csdb),
		new CMDIDownloadNodeAction(csdb, nodeResolver),
		new CMDIVersionNodeAction(csdb, nodeResolver));

	multipleNodeActionList = Arrays.<NodeAction>asList(
		new CMDISearchNodeAction(),
		new CMDITrovaNodeAction(),
		new CMDIAMSNodeAction(),
		new CMDIRrsNodeAction());


    }

    @Override
    public List<NodeAction> getNodeActions(Collection<TypedCorpusNode> nodes) {
	if (nodes.size() > 0 && nodes.size() == 1) {
	    for (TypedCorpusNode node : nodes) {
		if (node.getNodeType() instanceof CMDICollectionType) {
		    return collectionNodeActionList;
		}
		if (node.getNodeType() instanceof CMDIMetadata) {
		    return metadataNodeActionList;
		}
		if (node.getNodeType() instanceof CMDIResourceType) {
		    return resourceAudioVideoNodeActionList;
		}
		if (node.getNodeType() instanceof CMDIResourceTxtType) {
		    return resourcetxtNodeActionList;
		}
	    }
	} else if (nodes.size() > 1) {
	    return multipleNodeActionList;
	}
	return null;
    }
}
