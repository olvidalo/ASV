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
package nl.mpi.metadatabrowser.model.cmdi.nodeactions;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collection;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.NavigationActionRequest;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public abstract class RedirectingNodeAction implements NodeAction {

    private final static Logger logger = LoggerFactory.getLogger(RedirectingNodeAction.class);

    @Override
    public NodeActionResult execute(Collection<TypedCorpusNode> nodes) throws NodeActionException {
        logger.debug("Action [{}] invoked on {}", getName(), nodes);
        final URI targetURI = getTarget(nodes);
        if (targetURI != null) {
            try {
                final NavigationActionRequest navRequest = new NavigationActionRequest(targetURI.toURL());
                return new SimpleNodeActionResult(navRequest);
            } catch (MalformedURLException ex) {
                throw new NodeActionException(this, "Not a valid URL: " + targetURI, ex);
            }
        } else {
            throw new NodeActionException(this, "target uri could not be build. This is likely to happen when no node was found. If this is not the case please check configuration paramters.");
        }
    }

    protected abstract URI getTarget(Collection<TypedCorpusNode> nodes) throws NodeActionException;

}
