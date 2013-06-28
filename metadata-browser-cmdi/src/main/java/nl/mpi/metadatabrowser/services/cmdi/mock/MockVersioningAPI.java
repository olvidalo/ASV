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
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class MockVersioningAPI {
    public MockVersioningAPI(String jdbcurl) {
        
    }

    public boolean getStatus(String versioningTableInfo) {
        return true;
    }

    public List getAllVersions(URI nodeId, boolean showRetired) throws URISyntaxException {
                List result = new ArrayList();
        if (!getStatus("getAllVersions")) return result;
        URI where = getOldestVersion(nodeId);
        if (showRetired ) result.add(where);
       // while ( (where=getNewerVersion(where)) != null) {
        URI where2 = getNewerVersion(nodeId);
            if (showRetired) result.add(where2);
       // }
        return result;
    }

    public Date getDateOfVersion(URI currentNodeId) {
        //if (badNodeId(nodeId, "getDateOfVersion")) return null;
        if (!getStatus("getDateOfVersion")) return null;
        ResultSet rs = null;
        return new Timestamp(1368520487);
    }
    
    /* the remaining methods do not invoke SQL queries directly */
    
    /** @param nodeId the nodeId of any version of the node in question.
     * @return the newest version of the given node - returns the node itself if none
     */
    public URI getNewestVersion(URI nodeId) throws URISyntaxException {
        if (!getStatus("getNewestVersion")) return nodeId;
        URI node = nodeId;

            URI newNode = getNewerVersion(node);
            if (newNode==null) return node;
            node = newNode;
 return node;
        // not reached
    }

    /** @param nodeId the nodeId of any version of the node in question.
     * @return the oldest version of the given node - returns the node itself if none
     */
    public URI getOldestVersion(URI nodeId) throws URISyntaxException {
        if (!getStatus("getOldestVersion")) return nodeId;
        URI node = nodeId;
     
            URI oldNode = getOlderVersion(node);
            if (oldNode==null) return node;
            node = oldNode;
       return node;
        // not reached
    }
    
    /** @param nodeId the nodeId of a particular version of a node
     * @return the next older version of the given node, null if none exists
     */
    public URI getOlderVersion(URI node) throws URISyntaxException {
return new URI("olderVerison");
    }

    /** @param nodeId the nodeId of a particular version of a node
     * @return the next newer version of the given node, null if none exists
     */
    public URI getNewerVersion(URI where) throws URISyntaxException {
        return new URI("newerVersion");
    }
}
