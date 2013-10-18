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

import java.net.URI;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.CorpusNodeType;
import nl.mpi.archiving.corpusstructure.core.UnknownNodeException;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDICollectionType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIMetadataType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDICorpusType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDISessionType;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifierException;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDINodeTypeIdentifier implements NodeTypeIdentifier {

    public static final String IMDI_MIME_TYPE = "application/imdi+xml";
    public static final URI COLLECTION_PROFILE_ID = URI.create("profile"); //TODO: have a list of profileID or add correct profileID
    private final ProfileIdentifierImpl profileid;

    public CMDINodeTypeIdentifier(CorpusStructureProvider csProvider) {
	this.profileid = new ProfileIdentifierImpl(csProvider);
    }

    @Override
    public NodeType getNodeType(CorpusNode node) throws NodeTypeIdentifierException {
	final CorpusNodeType corpusNodeType = node.getType();

	if (corpusNodeType == CorpusNodeType.RESOURCE_VIDEO
		|| corpusNodeType == CorpusNodeType.RESOURCE_AUDIO
		|| corpusNodeType == CorpusNodeType.RESOURCE_OTHER) {
	    return new CMDIResourceType();
	} else if (corpusNodeType == CorpusNodeType.RESOURCE_ANNOTATION
		|| corpusNodeType == CorpusNodeType.RESOURCE_LEXICAL) {
	    return new CMDIResourceTxtType();
	} else if (corpusNodeType == CorpusNodeType.METADATA) {
	    //TODO: extend for special profile support (configurable probably)
	    if (node.getFormat().equals(IMDI_MIME_TYPE)) {
		return new IMDISessionType();
	    } else {
		return new CMDIMetadataType();
	    }
	} else if (node.getType() == CorpusNodeType.COLLECTION) {
	    if (node.getFormat().equals(IMDI_MIME_TYPE)) {
		return new IMDICorpusType();
	    } else {
		return new CMDICollectionType();
	    }
	} else if (COLLECTION_PROFILE_ID.equals(profileid.getProfile(node.getNodeURI()))) {
	    return new CMDICollectionType();
	} else {
	    return UNKNOWN_NODE_TYPE;
	}
    }
}
