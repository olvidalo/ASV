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

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.tree.GenericTreeModelProvider;
import nl.mpi.archiving.tree.GenericTreeModelProviderFactory;
import nl.mpi.archiving.tree.GenericTreeNode;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreePanel;
import nl.mpi.corpusstructure.UnknownNodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Utility that can expand a {@link ArchiveTreePanel} tree to a node identified
 * through its node ID or handle
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class TreeExpander implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(TreeExpander.class);
    @Autowired
    private CorpusStructureProvider csprovider;
    @Autowired
    private GenericTreeModelProviderFactory treeModelProviderFactory;

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
     * @return whether the tree could be expanded
     */
    public boolean openPath(ArchiveTreePanel tree, GenericTreeNode rootObj, URI nodeUri) {
        logger.debug("Trying to expand archive tree to [{}]", nodeUri);
        // retrieve actual node to:
        // 1. check whether it exists and has a parent (i.e. is linked in)
        // 2. obtain the NodeUri in case it was requested by means of its handle
        //    (node URIs will be assumed when expanding the tree later on)
        final CorpusNode node = csprovider.getNode(nodeUri);
        if (node != null) {
            if (csprovider.getCanonicalParent(node.getNodeURI()) != null) {
                final List<URI> parentNodes = new ArrayList<>();
                parentNodes.add(node.getNodeURI());
                return getParentNode(nodeUri, tree, parentNodes, rootObj);
            } else if(node.getNodeURI().equals(csprovider.getRootNodeURI())) {
                //target node is the root node, select and expand
                tree.expand(node);
                tree.toggleSelection(node);
                return true;
            }
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
    private boolean getParentNode(URI node, ArchiveTreePanel tree, List<URI> parentNodes, GenericTreeNode rootObj) {
        URI parentNodeURI = csprovider.getCanonicalParent(node);// get parent URI
        logger.trace("Expanding tree: found parent node [{}]", parentNodeURI);
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
     * @param parentNodes list of node URIs (not handles!) that need to be
     * expanded
     * @param treePanel ArchivePanel, treePanel in which node will be expanded
     * @param rootObj Object, the root node which is already expanded
     */
    private boolean expandTreeToSelectedNode(List<URI> parentNodes, ArchiveTreePanel tree, GenericTreeNode rootObj) {
        final GenericTreeModelProvider treeModelProvider = treeModelProviderFactory.createTreeModelProvider(rootObj);

        logger.debug("Found parents, expanding node path {}", parentNodes);
        // Generate an iterator. Start just after the last element.(reverse reading)
        ListIterator<URI> li = parentNodes.listIterator(parentNodes.size());
        GenericTreeNode currentTreeNode = rootObj;// this is root node (already expanded)
        li.previous();//TODO: use guava to iterate over a reverse list (and skip root)
        while (li.hasPrevious()) {
            URI nodeUri = li.previous();
            currentTreeNode = findTreeNodeObject(currentTreeNode, nodeUri, treeModelProvider);
            if (currentTreeNode == null) {
                logger.error("Node not found while expanding trees!", nodeUri);
                return false;
            } else {
                tree.expand(currentTreeNode);
                tree.toggleSelection(currentTreeNode);
            }
        }
        return true;
    }

    /**
     * Method that will find the object of a given node. Goes through all the
     * children of a node to find a match
     *
     * @param currentTreeNode GenericTreeSwingTreeNodeWrapper, object to be
     * matched with nopdeURI
     * @param targetUri URI, uri of the node that has to be expanded
     * @return treeNodeObject GenericTreeSwingTreeNodeWrapper, object of a node
     * to be expanded
     */
    private GenericTreeNode findTreeNodeObject(GenericTreeNode currentTreeNode, final URI targetUri, GenericTreeModelProvider treeModelProvider) {
        final Iterator<? extends GenericTreeNode> childIterator = treeModelProvider.getChildren(currentTreeNode);

        // get the first child that matches the target URI
        final GenericTreeNode node = Iterators.find(childIterator, new Predicate<GenericTreeNode>() {

            @Override
            public boolean apply(GenericTreeNode contentNode) {
                return (contentNode instanceof CorpusNode
                        && targetUri.equals(((CorpusNode) contentNode).getNodeURI()));
            }

        });
        return node;
    }
}
