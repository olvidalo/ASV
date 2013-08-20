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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import nl.mpi.archiving.corpusstructure.core.AccessInfo;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.UnknownNodeException;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreeNodeIconProvider;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.ResourcePresentation;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifierException;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

;

/**
 *
 * @param <T>
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDINodeIconProvider<T extends CorpusNode> implements ArchiveTreeNodeIconProvider<T> {

    private final ImageIcon sessionIcon = new ImageIcon(CMDINodeIconProvider.class.getResource("session_color.gif"));
    private final ImageIcon corpusIcon = new ImageIcon(CMDINodeIconProvider.class.getResource("corpusnode_color.gif"));
    private final ImageIcon fileIcon = new ImageIcon(CMDINodeIconProvider.class.getResource("mediafile.gif"));
    private final ImageIcon fileIconTxt = new ImageIcon(CMDINodeIconProvider.class.getResource("file.gif"));
    private final ImageIcon openIcon = new ImageIcon(ResourcePresentation.class.getResource("al_circle_green.png"));
    private final ImageIcon licensedIcon = new ImageIcon(ResourcePresentation.class.getResource("al_circle_yellow.png"));
    private final ImageIcon restrictedIcon = new ImageIcon(ResourcePresentation.class.getResource("al_circle_orange.png"));
    private final ImageIcon closedIcon = new ImageIcon(ResourcePresentation.class.getResource("al_circle_red.png"));
    private final ImageIcon externalIcon = new ImageIcon(ResourcePresentation.class.getResource("al_circle_black.png"));
    private final NodeTypeIdentifier nodeTypeIdentifier;
    private final CorpusStructureProvider csdb;
    private HashMap<Map<ImageIcon, ImageIcon>, ResourceReference> IconMap = new HashMap<Map<ImageIcon, ImageIcon>, ResourceReference>();

    /**
     * Constructor
     *
     * @param nodeTypeIdentifier
     * @param csdb
     */
    public CMDINodeIconProvider(NodeTypeIdentifier nodeTypeIdentifier, CorpusStructureProvider csdb) {
        this.nodeTypeIdentifier = nodeTypeIdentifier;
        this.csdb = csdb;
        populateIconMap();
    }

    /**
     * Method that manage the type of resourceReference returned dpeending on
     * type of node and accesslevel
     *
     * @param contentNode, node for which type and accesslevel have to be grabed
     * @return ResourceReference
     */
    @Override
    public ResourceReference getNodeIcon(T contentNode) {
        ResourceReference combinedIcon = null;
        try {
            final NodeType nodeType = nodeTypeIdentifier.getNodeType(contentNode);
            if (nodeType.getName().equalsIgnoreCase("Collection")) {
                combinedIcon = checkNodeAccess(contentNode, csdb, corpusIcon);
            } else if (nodeType.getName().equalsIgnoreCase("Root")) {
                combinedIcon = checkNodeAccess(contentNode, csdb, corpusIcon);
            } else if (nodeType.getName().equalsIgnoreCase("ResourceAudioVideo")) {
                combinedIcon = checkNodeAccess(contentNode, csdb, fileIcon);
            } else if (nodeType.getName().equalsIgnoreCase("ResourceTxt")) {
                combinedIcon = checkNodeAccess(contentNode, csdb, fileIconTxt);
            } else {
                combinedIcon = checkNodeAccess(contentNode, csdb, sessionIcon);
            }
        } catch (UnknownNodeException ex) {
            Logger.getLogger(CMDINodeIconProvider.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NodeTypeIdentifierException ex) {
            Logger.getLogger(CMDINodeIconProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return combinedIcon;
    }

    /**
     * Method that create a combinedIcon reflecting the type of Node and the
     * access level.
     *
     * @param ImageIcon,typeIcon : icon that give the type of node
     * @param ImageIcon, accesslevel : icon that give the level of access for
     * the node
     * @return ResourceReference, combination of both icon given as parameters.
     */
    private ResourceReference createCombinedIcon(final ImageIcon typeIcon, final ImageIcon accessLevel, String name) {
        ResourceReference combinedIcon = new ResourceReference(name) {

            @Override
            public IResource getResource() {
                final BufferedDynamicImageResource resource = new BufferedDynamicImageResource();
                int w = (int) (typeIcon.getImage().getWidth(null) + (float) accessLevel.getImage().getWidth(null));
                int h = Math.max(typeIcon.getImage().getHeight(null), accessLevel.getImage().getHeight(null));

                final BufferedImage testing = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = testing.createGraphics();
                typeIcon.paintIcon(null, g2, 1, 0);
                accessLevel.paintIcon(null, g2, typeIcon.getImage().getWidth(null), 0);
                g2.dispose();
                resource.setImage(testing);
                resource.setFormat("PNG");
                return resource;
            }
        };
        return combinedIcon;
    }

    /**
     * Method return an ImageIcon reflecting the access level for a particular
     * node. Access is requested to corpusStructureDB
     *
     * @param contentNode, node for which access level have to be requested
     * @param csdb , instance of corpusStructureDb
     * @return ImageIcon, corresponding to access level
     */
    private ResourceReference checkNodeAccess(T contentNode, CorpusStructureProvider csdb, ImageIcon typeNode) throws UnknownNodeException {
        final AccessInfo nAccessInfo = csdb.getNode(contentNode.getNodeURI()).getAuthorization();
        HashMap<ImageIcon, ImageIcon> valuesMap = new HashMap<ImageIcon, ImageIcon>();
        ImageIcon accessIcon = null;
        ResourceReference combinedIcon = null;

        int nodeAccessLevel = AccessInfo.ACCESS_LEVEL_NONE;
        if (nAccessInfo.getAccessLevel() > AccessInfo.ACCESS_LEVEL_NONE) {
            nodeAccessLevel = nAccessInfo.getAccessLevel();
        }

        if (nodeAccessLevel == 1) {
            accessIcon = openIcon;
        } else if (nodeAccessLevel == 2) {
            accessIcon = licensedIcon;
        } else if (nodeAccessLevel == 3) {
            accessIcon = restrictedIcon;
        } else if (nodeAccessLevel == 4) {
            accessIcon = closedIcon;
        } else if (nodeAccessLevel == 5) {
            accessIcon = externalIcon;
        }

        valuesMap.put(typeNode, accessIcon);
        // retrieve the corresponding combined icon based on nodetype and accesslevel
        for (Entry<Map<ImageIcon, ImageIcon>, ResourceReference> entry : IconMap.entrySet()) {
            if (entry.getKey().equals(valuesMap)) {
                combinedIcon = entry.getValue();
            }
        }
        return combinedIcon;
    }

    private void populateIconMap() {
        List<ImageIcon> nodeIcon = new ArrayList<ImageIcon>() {
            {
                add(sessionIcon);
                add(corpusIcon);
                add(fileIcon);
                add(fileIconTxt);
            }
        };

        List<ImageIcon> accessIcon = new ArrayList<ImageIcon>() {
            {
                add(openIcon);
                add(licensedIcon);
                add(restrictedIcon);
                add(closedIcon);
                add(externalIcon);
            }
        };

        
        for(ImageIcon nodetypeIcon: nodeIcon){
            for(ImageIcon accesslevelIcon: accessIcon){
                String name = StringRandomGenerator.generateRandomString();
                HashMap<ImageIcon, ImageIcon> iconsMap = new HashMap<ImageIcon, ImageIcon>();
                iconsMap.put(nodetypeIcon, accesslevelIcon);
                IconMap.put(iconsMap, createCombinedIcon(nodetypeIcon, accesslevelIcon, name));
            }
        }
    }
}
