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

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

/**
 * Resource stream to corpus node contents. Uses a node resolver to open a
 * stream to the actual content.
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class CorpusNodeResourceStream extends AbstractResourceStream {

    private final CorpusNode corpusNode;
    private final NodeResolver nodeResolver;
    private transient InputStream inputStream;

    /**
     * Creates a resource stream object, does not open the contained stream yet
     * (this only happens when {@link #getInputStream() } gets called)
     *
     * @param nodeResolver resolver to request input stream from
     * @param corpusNode node to stream content of
     */
    public CorpusNodeResourceStream(NodeResolver nodeResolver, CorpusNode corpusNode) {
        this.nodeResolver = nodeResolver;
        this.corpusNode = corpusNode;
    }

    /**
     *
     * @return the input stream provided by {@link NodeResolver#getInputStream(nl.mpi.archiving.corpusstructure.core.CorpusNode)
     * }
     * @
     * throws ResourceStreamNotFoundException if requesting the input stream
     * leads to an {@link IOException}
     */
    @Override
    public InputStream getInputStream() throws ResourceStreamNotFoundException {
        try {
            return inputStream = nodeResolver.getInputStream(corpusNode);
        } catch (IOException ex) {
            throw new ResourceStreamNotFoundException(String.format("Error reading contents of node %s using node resolver %s", corpusNode, nodeResolver), ex);
        }
    }

    @Override
    public void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Override
    public String getContentType() {
        return corpusNode.getFormat();
    }
}
