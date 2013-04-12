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
package nl.mpi.metadatabrowser.services.cmdi;

import java.net.URI;
import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.cmdi.CMDICollectionType;
import nl.mpi.metadatabrowser.model.cmdi.CMDIMetadata;
import nl.mpi.metadatabrowser.model.cmdi.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.CMDIResourceType;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDINodeTypeIdentifier implements nl.mpi.metadatabrowser.services.NodeTypeIdentifier {

    public ProfileIdentifier getProfileId(URI nodeURI) {
        ProfileIdentifier profileId = new ProfileIdentifier();
        profileId.getProfile(nodeURI);
        return profileId;
    }

    @Override
    public NodeType getNodeType(CorpusNode node) {
        URI nodeUri = node.getUri();
        if(nodeUri == null){
            return null;
        }
        NodeType nodetype = null;
        ProfileIdentifier profileid = new ProfileIdentifier();
        if (profileid.getProfile(nodeUri).equals("profile1")) {
            nodetype = new CMDIResourceTxtType();           
        }
        if (profileid.getProfile(nodeUri).equals("profile2")) {
            nodetype = new CMDICollectionType();
        }
        if (profileid.getProfile(nodeUri).equals("profile3")) {
            nodetype = new CMDIMetadata();
        }
        return nodetype;
    }
}
