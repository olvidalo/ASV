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
import java.util.Arrays;
import java.util.List;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.cmdi.*;
import nl.mpi.metadatabrowser.services.NodeActionsProvider;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIActionsProvider implements NodeActionsProvider {

    private final List<NodeAction> resourcetxtNodeActionList = Arrays.asList(
            new CMDIAMSNodeAction("Annotation Content Search"),
            new CMDIAMSNodeAction("Manage access Rights"),
            new CMDIAMSNodeAction("Request Resource Access"),
            new CMDIAMSNodeAction("Access statistics"),
            new CMDIAMSNodeAction("View Node"),
            new CMDIAMSNodeAction("Create Bookmark"),
            new CMDIDonwloadNodeAction("Download"));
    
    //private final List<NodeAction> resourcetxtNodeActionList;
    private final List<NodeAction> resourceAudioVideoNodeActionList = Arrays.asList(
            new CMDIAMSNodeAction("Manage access Rights"),
            new CMDIAMSNodeAction("Request Resource Access"),
            new CMDIAMSNodeAction("Access statistics"),
            new CMDIAMSNodeAction("View Node"),
            new CMDIAMSNodeAction("Create Bookmark"),
            new CMDIDonwloadNodeAction("Download"));
    
    private final List<NodeAction> metadataNodeActionList = Arrays.asList(
            new CMDIAMSNodeAction("Metadata Search"),
            new CMDIAMSNodeAction("Annotation Content Search"),
            new CMDIAMSNodeAction("Manage access Rights"),
            new CMDIAMSNodeAction("Request Resource Access"),
            new CMDIAMSNodeAction("Access statistics"),
            new CMDIAMSNodeAction("View Node"),
            new CMDIAMSNodeAction("Create Bookmark"),
            new CMDIDonwloadNodeAction("Download"),
            new CMDIDonwloadNodeAction("Download all resources"),
            new CMDIDonwloadNodeAction("Version info"),
            new CMDIAMSNodeAction("View Images")
            );
    
    public final List<NodeAction> collectionNodeActionList = Arrays.asList(
            new CMDIAMSNodeAction("Metadata Search"),
            new CMDIAMSNodeAction("Annotation Content Search"),
            new CMDIAMSNodeAction("Manage access Rights"),
            new CMDIAMSNodeAction("Request Resource Access"),
            new CMDIAMSNodeAction("View Node"),
            new CMDIAMSNodeAction("Create Bookmark"),
            new CMDIDonwloadNodeAction("Download")
            );
    // private final List<NodeAction> extraNodeActionList;

    public CMDIActionsProvider() {
        //this.uriNodeActionMap = uriNodeActionMap;
    }

    @Override
    public List<NodeAction> getNodeActions(URI nodeUri, NodeType nodeType) {
        if (nodeType instanceof CMDICollectionType) {
            return collectionNodeActionList;
        }
        if (nodeType instanceof CMDIMetadata) {
            return metadataNodeActionList;
        }
        if (nodeType instanceof CMDIResourceType) {
            return resourceAudioVideoNodeActionList;
        }
        if (nodeType instanceof CMDIResourceTxtType) {
            return resourcetxtNodeActionList;
        }
        return null;
    }

//    private List<NodeAction> createResourcetxtActions() {
//        resourcetxtNodeActionList.add(new CMDIDonwloadNodeAction("Annotation Content Search"));
//        resourcetxtNodeActionList.add(new CMDIDonwloadNodeAction("Manage access Rights"));
//        resourcetxtNodeActionList.add(new CMDIDonwloadNodeAction("Request Resource Access"));
//        resourcetxtNodeActionList.add(new CMDIDonwloadNodeAction("Access statistics"));
//        resourcetxtNodeActionList.add(new CMDIDonwloadNodeAction("View Node"));
//        resourcetxtNodeActionList.add(new CMDIDonwloadNodeAction("Create Bookmark"));
//        resourcetxtNodeActionList.add(new CMDIDonwloadNodeAction("Download"));
//        return resourcetxtNodeActionList;
//    }

//    private List<NodeAction> createResourceAudioVideoActions() {
//        resourceAudioVideoNodeActionList.add(new CMDIDonwloadNodeAction("Manage access Rights"));
//        resourceAudioVideoNodeActionList.add(new CMDIDonwloadNodeAction("Request Resource Access"));
//        resourceAudioVideoNodeActionList.add(new CMDIDonwloadNodeAction("Access statistics"));
//        resourceAudioVideoNodeActionList.add(new CMDIDonwloadNodeAction("View Node"));
//        resourceAudioVideoNodeActionList.add(new CMDIDonwloadNodeAction("Create Bookmark"));
//        resourceAudioVideoNodeActionList.add(new CMDIDonwloadNodeAction("Download"));
//        return resourceAudioVideoNodeActionList;
//    }

//    private List<NodeAction> createMetadataActions() {
//        metadataNodeActionList.add(new CMDIDonwloadNodeAction("Metadata Search"));
//        metadataNodeActionList.add(new CMDIDonwloadNodeAction("Annotation Content Search"));
//        metadataNodeActionList.add(new CMDIDonwloadNodeAction("Manage access Rights"));
//        metadataNodeActionList.add(new CMDIDonwloadNodeAction("Request Resource Access"));
//        metadataNodeActionList.add(new CMDIDonwloadNodeAction("View Node"));
//        metadataNodeActionList.add(new CMDIDonwloadNodeAction("Create Bookmark"));
//        metadataNodeActionList.add(new CMDIDonwloadNodeAction("Download"));
//        metadataNodeActionList.add(new CMDIDonwloadNodeAction("Download all resources"));
//        metadataNodeActionList.add(new CMDIDonwloadNodeAction("Version info"));
//        metadataNodeActionList.add(new CMDIDonwloadNodeAction("View Images"));
//        metadataNodeActionList.add(new CMDIDonwloadNodeAction("Access statistics"));
//        return metadataNodeActionList;
//    }
//
//    private List<NodeAction> createCollectionActions() {
//        collectionNodeActionList.add(new CMDIDonwloadNodeAction("Metadata Search"));
//        collectionNodeActionList.add(new CMDIDonwloadNodeAction("Annotation Content Search"));
//        collectionNodeActionList.add(new CMDIDonwloadNodeAction("Manage access Rights"));
//        collectionNodeActionList.add(new CMDIDonwloadNodeAction("Request Resource Access"));
//        collectionNodeActionList.add(new CMDIDonwloadNodeAction("View Node"));
//        collectionNodeActionList.add(new CMDIDonwloadNodeAction("Create Bookmark"));
//        collectionNodeActionList.add(new CMDIDonwloadNodeAction("Download"));
//        return collectionNodeActionList;
//    }
}
