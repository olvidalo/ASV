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
import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.cmdi.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDINodeTypeIdentifier implements nl.mpi.metadatabrowser.services.NodeTypeIdentifier {

    private final CmdiCorpusStructureDB csdb;
    private String collectionProfileId = "profile";

    public CMDINodeTypeIdentifier(CmdiCorpusStructureDB csdb) {
        this.csdb = csdb;
    }

    @Override
    public NodeType getNodeType(CorpusNode node) {
        URI nodeUri = node.getUri();
        if (nodeUri == null) {
            return null;
        }
        ProfileIdentifierImpl profileid = new ProfileIdentifierImpl(csdb);
        final CorpusNodeType corpusNodeType = csdb.getCorpusNodeType(node.getNodeId());
        
        if (corpusNodeType == CorpusNodeType.RESOURCE_VIDEO || corpusNodeType == CorpusNodeType.RESOURCE_AUDIO
                || corpusNodeType == CorpusNodeType.RESOURCE_OTHER) {
            return new CMDIResourceType();
        } else if (corpusNodeType == CorpusNodeType.RESOURCE_ANNOTATION || corpusNodeType == CorpusNodeType.RESOURCE_LEXICAL) {
            return new CMDIResourceTxtType();
        } else if (corpusNodeType == CorpusNodeType.METADATA){
            return new CMDIMetadata();
        }
        else if (profileid.getProfile(nodeUri).equals(collectionProfileId)) {
            return new CMDICollectionType();
        } //todo extend for special profile support (configurable probably)
        else {
            return new CMDIMetadata();
        }
    }
}
