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

import java.util.List;
import nl.mpi.archiving.corpusstructure.provider.AccessInfo;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
class MockAccessInfo implements AccessInfo {

    @Override
    public int getAccessLevel() {
	return AccessInfo.ACCESS_LEVEL_OPEN_EVERYBODY;
    }

    @Override
    public String getReadRights() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getWriteRights() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasReadAccess(String username) {
	return true;
    }

    @Override
    public boolean hasWriteAccess(String username) {
	return true;
    }

    @Override
    public void setAccessLevel(int accessLevel) {
    }

    @Override
    public void setReadRights(String privs) {
    }

    @Override
    public void setReadRule(String privs) {
    }

    @Override
    public void setReadUsers(List users) {
    }

    @Override
    public void setWriteRights(String privs) {
    }

    @Override
    public void setWriteRule(String privs) {
    }

    @Override
    public void setWriteUsers(List users) {
    }
}
