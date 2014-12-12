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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDIAMSNodeAction;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDICitationNodeAction;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDIDownloadNodeAction;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDIMultipleDownloadNodeAction;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDIRrsNodeAction;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDISearchNodeAction;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDITrovaNodeAction;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDIVersionNodeAction;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.CMDIViewNodeAction;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceType;
import nl.mpi.metadatabrowser.model.cmdi.type.CollectionType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDICatalogueType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDIInfoType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourceAudioType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourcePictureType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourceVideoType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourceWrittenType;
import nl.mpi.metadatabrowser.model.cmdi.type.MetadataType;
import nl.mpi.metadatabrowser.services.NodeActionsProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIActionsProvider implements NodeActionsProvider {

    @Autowired(required = true)
    private CMDIAMSNodeAction amsNodeAction;
    @Autowired(required = true)
    private CMDICitationNodeAction bookmarkNodeAction;
    @Autowired(required = true)
    private CMDIDownloadNodeAction downloadNodeAction;
    @Autowired(required = true)
    private CMDIMultipleDownloadNodeAction multipleDownloadNodeAction;
    @Autowired(required = true)
    private CMDIRrsNodeAction rrsNodeAction;
    @Autowired(required = true)
    private CMDISearchNodeAction searchNodeAction;
    @Autowired(required = true)
    private CMDITrovaNodeAction trovaNodeAction;
    @Autowired(required = true)
    private CMDIVersionNodeAction versionNodeAction;
    @Autowired(required = true)
    private CMDIViewNodeAction viewNodeAction;
    @Autowired(required = true)
    private final CorpusStructureProvider csdb;
    // Type dependent lists to be initialized
    private List<NodeAction> resourcetxtNodeActionList;
    private List<NodeAction> resourceAudioVideoNodeActionList;
    private List<NodeAction> metadataNodeActionList;
    private List<NodeAction> childLessMetadataNodeActionList;
    private List<NodeAction> collectionNodeActionList;
    private List<NodeAction> multipleNodeActionList;
    private List<NodeAction> catalogueMetadataNodeActionList;
    private List<NodeAction> infoFileActionList;

    @Autowired
    public CMDIActionsProvider(CorpusStructureProvider csdb) {
        this.csdb = csdb;
    }

    @PostConstruct
    public void init() {
        metadataNodeActionList = Arrays.<NodeAction>asList(
                searchNodeAction,
                trovaNodeAction,
                amsNodeAction,
                rrsNodeAction,
                bookmarkNodeAction,
                multipleDownloadNodeAction,
                versionNodeAction);

        catalogueMetadataNodeActionList = Arrays.<NodeAction>asList(
                searchNodeAction,
                trovaNodeAction,
                amsNodeAction,
                rrsNodeAction,
                bookmarkNodeAction);

        childLessMetadataNodeActionList = Arrays.<NodeAction>asList(
                searchNodeAction,
                trovaNodeAction,
                amsNodeAction,
                rrsNodeAction,
                bookmarkNodeAction,
                versionNodeAction);

        collectionNodeActionList = Arrays.<NodeAction>asList(
                searchNodeAction,
                trovaNodeAction,
                amsNodeAction,
                rrsNodeAction,
                bookmarkNodeAction
                );

        resourceAudioVideoNodeActionList = Arrays.<NodeAction>asList(
                amsNodeAction,
                rrsNodeAction,
                viewNodeAction,
                bookmarkNodeAction,
                downloadNodeAction,
                versionNodeAction);

        resourcetxtNodeActionList = Arrays.<NodeAction>asList(
                trovaNodeAction,
                amsNodeAction,
                rrsNodeAction,
                viewNodeAction,
                bookmarkNodeAction,
                downloadNodeAction,
                versionNodeAction);
        
        infoFileActionList = Arrays.<NodeAction>asList(
                amsNodeAction,
                bookmarkNodeAction,
                downloadNodeAction
        );

        multipleNodeActionList = Arrays.<NodeAction>asList(
                trovaNodeAction,
                amsNodeAction,
                rrsNodeAction);

    }

    @Override
    public List<NodeAction> getNodeActions(Collection<TypedCorpusNode> nodes) {
        if (nodes.size() > 0 && nodes.size() == 1) {
            for (TypedCorpusNode node : nodes) {
                final NodeType nodeType = node.getNodeType();
                
                if (nodeType instanceof CollectionType) {
                    return collectionNodeActionList;
                }
                else if (nodeType instanceof MetadataType) {
                    if (nodeType instanceof IMDICatalogueType) {
                        return catalogueMetadataNodeActionList;
                    }
                    URI childNodeUri = node.getNodeURI();
                    if (childNodeUri == null) {
                        return null;
                    } else {
                        if (csdb.getChildNodes(childNodeUri).isEmpty()) {
                            return childLessMetadataNodeActionList;
                        } else {
                            return metadataNodeActionList;
                        }
                    }
                }
                else if (nodeType instanceof CMDIResourceType || nodeType instanceof ResourceVideoType || nodeType instanceof ResourcePictureType || nodeType instanceof ResourceAudioType) {
                    return resourceAudioVideoNodeActionList;
                }
                else if (nodeType instanceof CMDIResourceTxtType || nodeType instanceof ResourceWrittenType) {
                    return resourcetxtNodeActionList;
                } else if(nodeType instanceof IMDIInfoType) {
                    return infoFileActionList;
                }
            }
        } else if (nodes.size() > 1) {
            return multipleNodeActionList;
        }
        return null;
    }
}
