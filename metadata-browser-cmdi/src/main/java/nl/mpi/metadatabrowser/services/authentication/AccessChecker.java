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
package nl.mpi.metadatabrowser.services.authentication;

import java.net.URI;
import nl.mpi.archiving.corpusstructure.core.NodeNotFoundException;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public interface AccessChecker {

    Boolean hasAccess(final String userid, TypedCorpusNode node) throws NodeNotFoundException;

    /**
     * method to check accessibility to a node for user
     *
     * @param userid user that request the download
     * @param nodeId URI from the node that needs to grant access to the user
     * @return true iff user has access to the specificed node
     * @throws nl.mpi.archiving.corpusstructure.core.NodeNotFoundException
     */
    Boolean hasAccess(String userid, URI nodeId) throws NodeNotFoundException;
    
}
