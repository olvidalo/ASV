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

import java.io.Serializable;
import java.net.URI;
import nl.mpi.archiving.corpusstructure.core.NodeNotFoundException;
import nl.mpi.archiving.corpusstructure.provider.AccessInfoProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class AccessCheckerImpl implements AccessChecker, Serializable {

    private final AccessInfoProvider accessInfoProvider;
    
    @Autowired
    public AccessCheckerImpl(AccessInfoProvider accessInfoProvider) {
        this.accessInfoProvider = accessInfoProvider;
    }

    /**
     * method to check accessibility to a node for user
     *
     * @param userid user that request the download
     * @param nodeId URI from the node that needs to grant access to the user
     * @return true iff user has access to the specificed node
     * @throws nl.mpi.archiving.corpusstructure.core.NodeNotFoundException
     */
    @Override
    public Boolean hasAccess(String userid, URI nodeId) throws NodeNotFoundException {
        if (userid == null || userid.equals("") || userid.equals("anonymous")) {
            return accessInfoProvider.hasReadAccess(nodeId, AccessInfoProvider.EVERYBODY);
        } else {
            return accessInfoProvider.hasReadAccess(nodeId, userid);
        }
    }

}
