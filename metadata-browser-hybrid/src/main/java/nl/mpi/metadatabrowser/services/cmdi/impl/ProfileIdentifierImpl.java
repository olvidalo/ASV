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

import java.net.URI;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.services.cmdi.ProfileIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class ProfileIdentifierImpl implements ProfileIdentifier {

    private final static Logger logger = LoggerFactory.getLogger(ProfileIdentifierImpl.class);
    private final CorpusStructureProvider csdb;

    /**
     *
     * @param csdb
     */
    @Autowired
    public ProfileIdentifierImpl(CorpusStructureProvider csdb) {
	this.csdb = csdb;
    }

    @Override
    public URI getProfile(URI uri) {
        URI profileUri = csdb.getNode(uri).getProfile();
        if(profileUri == null) {
            logger.warn("Failed to get profile, unknown node at {}", uri);
        }
        return profileUri;
    }
}
