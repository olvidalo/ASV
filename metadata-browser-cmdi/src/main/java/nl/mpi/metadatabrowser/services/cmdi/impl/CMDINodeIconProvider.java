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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.Icon;
import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreeNodeIconProvider;
import nl.mpi.corpusstructure.AccessInfo;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.cmdi.CmdiCorpusStructureDB;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.ResourcePresentation;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import javax.swing.ImageIcon;
import nl.mpi.metadatabrowser.services.cmdi.CompoundIcon;

;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDINodeIconProvider<T extends CorpusNode> implements ArchiveTreeNodeIconProvider<T> {
    
    private final ResourceReference sessionIcon = new PackageResourceReference(CMDINodeIconProvider.class, "session_color.gif");
    private final ResourceReference corpusIcon = new PackageResourceReference(CMDINodeIconProvider.class, "corpusnode_color.gif");
    private final ResourceReference fileIcon = new PackageResourceReference(CMDINodeIconProvider.class, "mediafile.gif");
    private final ResourceReference fileIconTxt = new PackageResourceReference(CMDINodeIconProvider.class, "file.gif");
    private final ResourceReference openIcon = new PackageResourceReference(ResourcePresentation.class, "al_circle_green.png");
    private final ResourceReference licensedIcon = new PackageResourceReference(ResourcePresentation.class, "al_circle_yellow.png");
    private final ResourceReference restrictedIcon = new PackageResourceReference(ResourcePresentation.class, "al_circle_orange.png");
    private final ResourceReference closedIcon = new PackageResourceReference(ResourcePresentation.class, "al_circle_red.png");
    private final ResourceReference externalIcon = new PackageResourceReference(ResourcePresentation.class, "al_circle_black.png");
    private final NodeTypeIdentifier nodeTypeIdentifier;
    private final CmdiCorpusStructureDB csdb;
    float[] scales = {1f, 1f, 1f, 0.5f};
    float[] offsets = new float[4];
    RescaleOp rop;
    
    public CMDINodeIconProvider(NodeTypeIdentifier nodeTypeIdentifier, CmdiCorpusStructureDB csdb) {
        this.nodeTypeIdentifier = nodeTypeIdentifier;
        this.csdb = csdb;
    }
    
    @Override
    public ResourceReference getNodeIcon(T contentNode) {
        
        final ImageIcon se = new ImageIcon(CMDINodeIconProvider.class.getResource("session_color.gif"));
        final ImageIcon red = new ImageIcon(ResourcePresentation.class.getResource("al_circle_red.png"));
        Icon[] icons = new Icon[2];
        icons[0] = se;
        icons[1] = red;
        
        

        final nl.mpi.metadatabrowser.services.cmdi.CompoundIcon ic = new CompoundIcon(icons);
        
        // try {
        // load source images
//            File session = new File(getClass().getResource("/icons/session_color.gif").toURI());
//File green = new File(getClass().getResource("/icons/al_circle_green.png").toURI());
//
//    BufferedImage image = ImageIO.read(session);
//    BufferedImage overlay = ImageIO.read(green);
//    
//    // create the new image, canvas size is the max. of both image sizes
//int w = Math.max(image.getWidth(), overlay.getWidth());
//int h = Math.max(image.getHeight(), overlay.getHeight());
//int type= image.getType();
//BufferedImage combined = new BufferedImage(w, h, type);
//
//// paint both images, preserving the alpha channels
//Graphics2D g = combined.createGraphics();
//g.drawImage(image, 0, 0, null);
//g.drawImage(overlay, image.getWidth(), 0, null);
//g.dispose();

        /*
         * Create a rescale filter op that makes the image 50% opaque.
         */
//float[] scales = { 1f, 1f, 1f, 0.5f };
//float[] offsets = new float[4];
//RescaleOp rop = new RescaleOp(scales, offsets, null);
//setOpacity(0.5f);
///* Draw the image, applying the filter */
//Graphics2D g2d = (Graphics2D)g;
//g2d.drawImage(combined, rop, 0, 0);


        //   ImageIO.write(combined, "PNG", new File("combined.png"));
        ResourceReference test = new ResourceReference("combined.png") {
            
            @Override
            public IResource getResource() {
                try {
                   // File session = new File(getClass().getResource("/icons/session_color.gif").toURI());
                   // File green = new File(getClass().getResource("/icons/al_circle_red.png").toURI());
                    InputStream session = CMDINodeIconProvider.class.getResourceAsStream("session_color.gif");
                    InputStream green = ResourcePresentation.class.getResourceAsStream("al_circle_red.png");

                    
                    
                    final BufferedImage image = ImageIO.read(session);
                    final BufferedImage overlay = ImageIO.read(green);
                    
                    int type = image.getType();
                    
                    final BufferedDynamicImageResource resource = new BufferedDynamicImageResource();
                    final BufferedImage images = new BufferedImage(image.getWidth()* 10, image.getHeight(), type);
                    Graphics2D g = images.createGraphics();
                    g.drawImage(image, null, 0, 0);
                    g.drawImage(overlay, null, image.getWidth(), 0);
                    //images.createGraphics().drawImage(image, 1, 0, null);
                    //images.createGraphics().drawImage(overlay, image.getWidth(),  0, null);
                    //g.drawImage(image, image.getWidth(), image.getHeight(), null);
                    //g.drawImage(overlay, image.getWidth() * 2, image.getHeight() * 2, null);
                    //g.setBackground(Color.WHITE);
                    //g.drawImage(image, 0, 0, null);
                    //g.drawImage(overlay, image.getWidth(), 0, null);
                    //g.dispose();
                    //drawCircle((Graphics2D)image.getGraphics());
                    // ImageIO.write(images, "jpeg", new File("combined.jpg")); 
                   //g.setBackground(Color.WHITE);
                    //resource.setImage(images);
                    
                    int w = (int) (se.getImage().getWidth(null) + (float)red.getImage().getWidth(null));
                    int h = Math.max(se.getImage().getHeight(null), red.getImage().getHeight(null));
                    
                    final BufferedImage testing = new BufferedImage(w , h, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = testing.createGraphics();
                    //g2.drawImage(se.getImage(), 0, 0, null);
                    //g2.drawImage(red.getImage(), se.getImage().getWidth(null), 0, null);
                    se.paintIcon(null, g2, 1, 0);
                    red.paintIcon(null, g2, se.getImage().getWidth(null), 0);
                    g2.dispose();
                    resource.setImage(testing);
                    resource.setFormat("PNG");
                    return resource;
                } catch (IOException ex) {
                    Logger.getLogger(CMDINodeIconProvider.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (URISyntaxException ex) {
//                    Logger.getLogger(CMDINodeIconProvider.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;                
            }
        };
        
        final NodeType nodeType = nodeTypeIdentifier.getNodeType(contentNode);
        if (nodeType.getName().equalsIgnoreCase("Collection")) {
            // return corpusIcon;
            return test;
        } else if (nodeType.getName().equalsIgnoreCase("Root")) {
            return corpusIcon;
        } else if (nodeType.getName().equalsIgnoreCase("ResourceAudioVideo")) {
            return fileIcon;
        } else if (nodeType.getName().equalsIgnoreCase("ResourceTxt")) {
            return fileIconTxt;
        } else {
            return test;
        }
//        } catch (IOException ex) {
//            Logger.getLogger(CMDINodeIconProvider.class.getName()).log(Level.SEVERE, null, ex);
//        }catch (URISyntaxException ex) {
//                Logger.getLogger(CMDINodeIconProvider.class.getName()).log(Level.SEVERE, null, ex);
//            }
        //return sessionIcon;
    }

//    @Override
//    public List<ResourceReference>  getNodeIcon(T contentNode) {
//        ResourceReference accessIcon;
//        List<ResourceReference> iconList = new ArrayList<ResourceReference>();
//        final NodeType nodeType = nodeTypeIdentifier.getNodeType(contentNode);
//        if (nodeType.getName().equalsIgnoreCase("Collection")) {
//            iconList.add(corpusIcon);
//            accessIcon = checkNodeAccess(contentNode, csdb);
//            iconList.add(accessIcon);
//        } else if (nodeType.getName().equalsIgnoreCase("Root")) {
//            iconList.add(corpusIcon);
//            accessIcon = checkNodeAccess(contentNode, csdb);
//            iconList.add(accessIcon);
//            return iconList;
//        } else if (nodeType.getName().equalsIgnoreCase("ResourceAudioVideo")) {
//            iconList.add(fileIcon);
//            accessIcon = checkNodeAccess(contentNode, csdb);
//            iconList.add(accessIcon);
//            return iconList;
//        } else if (nodeType.getName().equalsIgnoreCase("ResourceTxt")) {
//            iconList.add(fileIconTxt);
//            accessIcon = checkNodeAccess(contentNode, csdb);
//            iconList.add(accessIcon);
//            return iconList;
//        } else {
//            iconList.add(sessionIcon);
//            accessIcon = checkNodeAccess(contentNode, csdb);
//            iconList.add(accessIcon);
//            return iconList;
//        }
//    }
    private ResourceReference checkNodeAccess(T contentNode, CmdiCorpusStructureDB csdb) {
        String nodeId = Integer.toString(contentNode.getNodeId());
        AccessInfo nAccessInfo = csdb.getObjectAccessInfo(nodeId);
        
        ResourceReference refIcon = null;
        int nodeAccessLevel = AccessInfo.ACCESS_LEVEL_NONE;
        if (nAccessInfo.getAccessLevel() > AccessInfo.ACCESS_LEVEL_NONE) {
            nodeAccessLevel = nAccessInfo.getAccessLevel();
        }
        
        if (nodeAccessLevel == 1) {
            
            refIcon = openIcon;
            
            
        } else if (nodeAccessLevel == 2) {
            
            refIcon = licensedIcon;
            
        } else if (nodeAccessLevel == 3) {
            refIcon = restrictedIcon;
            
        } else if (nodeAccessLevel == 4) {
            
            refIcon = closedIcon;
        } else if (nodeAccessLevel == 5) {
            refIcon = externalIcon;
            
        }
        return refIcon;
    }
}
