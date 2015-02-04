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
package nl.mpi.metadatabrowser.services.cmdi.mock;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.OutputFormat;
import nl.mpi.archiving.corpusstructure.core.service.BaseNodeResolver;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.tree.corpusstructure.CorpusStructureDBNodeResolver;

/**
 * Node resolver that resolves URI's to resource file locations using a static
 * map while wrapping another resolver (base resolver) to act as a fallback.
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class MockNodeResolver extends BaseNodeResolver {

    private final NodeResolver baseResolver;
    private final Map<URI, String> nodeResourcesMap;

    /**
     *
     * @param nodeResourcesMap mapping node URI's to resource locations (e.g.
     * node:123 -> /nl/mpi/metadatabrowser/my/resource.xml)
     */
    public MockNodeResolver(Map<URI, String> nodeResourcesMap) {
        this(new CorpusStructureDBNodeResolver(), nodeResourcesMap);
    }

    public MockNodeResolver(NodeResolver baseResolver, Map<URI, String> nodeResourcesMap) {
        this.baseResolver = baseResolver;
        this.nodeResourcesMap = nodeResourcesMap;
    }

    @Override
    public URL getUrl(CorpusNode node) {
        final URI nodeId = node.getNodeURI();
        if (nodeResourcesMap.containsKey(nodeId)) {
            String resourceLocation = nodeResourcesMap.get(nodeId);
            return getClass().getResource(resourceLocation);
        } else {
            return baseResolver.getUrl(node);
        }
    }

    /**
     * Get the url for this corpus node with the specified output format. This
     * might result in a translation service url.
     *
     * @param node
     * @param format
     * @return
     */
    public URI getUrl(CorpusNode node, OutputFormat format) {
        try {
            return getUrl(node).toURI();
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public URI getPID(CorpusNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getId(CorpusNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
