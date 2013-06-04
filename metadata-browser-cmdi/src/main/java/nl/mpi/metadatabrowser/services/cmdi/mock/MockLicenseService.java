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
import nl.mpi.lat.ams.model.License;
import nl.mpi.lat.ams.model.NodeLicense;
import nl.mpi.lat.ams.service.LicenseService;
import nl.mpi.lat.auth.principal.LatUser;
import nl.mpi.lat.fabric.Node;
import nl.mpi.lat.fabric.NodeID;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class MockLicenseService implements LicenseService {

    public MockLicenseService() {
    }

    @Override
    public License newLicense() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public NodeLicense newNodeLicense() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public NodeLicense newNodeLicense(Node node, License lics) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<License> getLicenses() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<License> getEnabledLicenses() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void save(License lics) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveLicenses(List<License> licenses) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(License lics) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<NodeLicense> getNodeLicenses() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<NodeLicense> getNodeLicenses(NodeID nodeID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<License> getLicenses(NodeID nodeID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveNodeLicenses(List<NodeLicense> nolicss) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(List<NodeLicense> nolicss) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public License getLicense(Integer id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void staleNode(NodeID nodeID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getLicenseRootDir() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getLicenseLink(License license) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resetLicenseListByCreatorOrModifier(LatUser user, LatUser resetUser) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resetNodeLicenseListByCreatorOrModifier(LatUser user, LatUser resetUser) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resetNodePcplLicenseListByCreatorOrModifier(LatUser user, LatUser resetUser) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
