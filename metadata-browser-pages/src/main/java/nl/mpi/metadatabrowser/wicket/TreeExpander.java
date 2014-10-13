/*
 * Copyright (C) 2014 Max Planck Institute for Psycholinguistics
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
package nl.mpi.metadatabrowser.wicket;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.tree.GenericTreeNode;
import nl.mpi.archiving.tree.swingtree.GenericTreeSwingTreeNodeWrapper;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreePanel;
import nl.mpi.corpusstructure.UnknownNodeException;
import org.apache.wicket.extensions.markup.html.tree.Tree;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Utility that can expand a {@link ArchiveTreePanel} tree to a node identified
 * through its node ID or handle
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class TreeExpander implements Serializable {

    @Autowired
    private CorpusStructureProvider csprovider;

    public TreeExpander() {
    }

    public TreeExpander(CorpusStructureProvider csprovider) {
        this.csprovider = csprovider;
    }

    /**
     * expands the provided tree to the specified (by URI) node
     *
     * @param rootObj root object of the tree
     * @param tree tree to expand
     * @param nodeUri URI (with either "hdl:" or "node:" schema)
     * @return whether the tree was expanded
     */
    public boolean openPath(Tree tree, Object rootObj, URI nodeUri) {
        // retrieve actual node to:
        // 1. check whether it exists
        // 2. obtain the NodeUri in case it was requested by means of its handle
        final CorpusNode node = csprovider.getNode(nodeUri);
        if (node != null) {
            final List<URI> parentNodes = new ArrayList<>();
            parentNodes.add(node.getNodeURI());
            return getParentNode(nodeUri, tree, parentNodes, rootObj);
        }
        return false;
    }

    /**
     * Recursive method that collects all the parents of a given node up to the
     * root node.
     *
     * @param node URI, the uri of the child node for which we lookup the parent
     * @param treePanel ArchiveTreePanel, treePanel from which the parentNode
     * will be expanded
     * @param parentNodes List<URI>, stored list of parents URI
     * @param rootObj Object, the root node
     * @throws URISyntaxException
     * @throws UnknownNodeException
     */
    private boolean getParentNode(URI node, Tree tree, List<URI> parentNodes, Object rootObj) {
        URI parentNodeURI = csprovider.getCanonicalParent(node);// get parent URI
        if (parentNodeURI != null) {
            parentNodes.add(parentNodeURI); //add parent to the list
            return getParentNode(parentNodeURI, tree, parentNodes, rootObj);// find next parent
        } else {// no more parents
            return expandTreeToSelectedNode(parentNodes, tree, rootObj);
        }
    }

    /**
     * Method that expand the nodes
     *
     * @param parentNodes List<URI>, list of node URI that need to be expanded
     * @param treePanel ArchivePanel, treePanel in which node will be expanded
     * @param rootObj Object, the root node which is already expanded
     */
    private boolean expandTreeToSelectedNode(List<URI> parentNodes, Tree tree, Object rootObj) {
        // Generate an iterator. Start just after the last element.(reverse reading)
        ListIterator<URI> li = parentNodes.listIterator(parentNodes.size());
        Object currentTreeNode = rootObj;// this is root node (already expanded)
        li.previous();
        while (li.hasPrevious()) {
            URI nodeUri = li.previous();
            currentTreeNode = findTreeNodeObject((GenericTreeSwingTreeNodeWrapper) currentTreeNode, nodeUri);
            if (currentTreeNode == null) {
                return false;
            } else {
                tree.getTreeState().expandNode(currentTreeNode);
                tree.getTreeState().selectNode(currentTreeNode, true);
            }
        }
        return true;
    }

    /**
     * Method that will find the object of a given node. Goes through all the
     * children of a node to find the perfect match
     *
     * @param currentTreeNode GenericTreeSwingTreeNodeWrapper, object to be
     * matched with nopdeURI
     * @param nodeUri URI, uri of the node that has to be expanded
     * @return treeNodeObject GenericTreeSwingTreeNodeWrapper, object of a node
     * to be expanded
     */
    private Object findTreeNodeObject(GenericTreeSwingTreeNodeWrapper currentTreeNode, URI nodeUri) {
        for (int i = 0; i < currentTreeNode.getChildCount(); i++) {
            final GenericTreeSwingTreeNodeWrapper treeNodeObject = (GenericTreeSwingTreeNodeWrapper) currentTreeNode.getChildAt(i);
            GenericTreeNode contentNode = treeNodeObject.getContentNode();
            if (contentNode instanceof CorpusNode) {
                if (nodeUri.equals(((CorpusNode) contentNode).getNodeURI())) {
                    return treeNodeObject;
                }
            }
        }
        return null;
    }
}
