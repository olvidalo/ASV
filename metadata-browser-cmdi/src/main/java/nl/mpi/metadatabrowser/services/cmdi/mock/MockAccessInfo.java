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

import java.util.Collection;
import nl.mpi.archiving.corpusstructure.core.AccessInfo;
import nl.mpi.archiving.corpusstructure.core.AccessLevel;
import nl.mpi.archiving.corpusstructure.core.ArchiveUser;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class MockAccessInfo implements AccessInfo {

    @Override
    public AccessLevel getAccessLevel() {
	return AccessLevel.ACCESS_LEVEL_OPEN_EVERYBODY;
    }

    @Override
    public Collection<ArchiveUser> getReadRights() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<ArchiveUser> getWriteRights() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasReadAccess(String username) {
	return true;
    }

    @Override
    public boolean hasWriteAccess(String username) {
	return true;
    }
}
