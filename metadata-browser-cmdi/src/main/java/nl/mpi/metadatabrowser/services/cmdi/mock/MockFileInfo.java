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

import java.util.Date;
import nl.mpi.archiving.corpusstructure.core.FileInfo;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
class MockFileInfo implements FileInfo {

    @Override
    public long getSize() {
	return 2640;
    }

    @Override
    public String getChecksum() {
	return "adab88ba00910f9591df887f45a05419";
    }

    @Override
    public Date getFileTime() {
	return new Date();
    }

    @Override
    public String getCreator() {
	return "creator";
    }

    @Override
    public String getOwner() {
	return "owner";
    }
}
