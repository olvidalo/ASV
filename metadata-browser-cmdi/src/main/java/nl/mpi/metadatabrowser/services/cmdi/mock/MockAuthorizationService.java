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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.mpi.lat.ams.model.*;
import nl.mpi.lat.auth.authorization.AdvAuthorizationService;
import nl.mpi.lat.auth.principal.LatGroup;
import nl.mpi.lat.auth.principal.LatPrincipal;
import nl.mpi.lat.auth.principal.LatUser;
import nl.mpi.lat.fabric.Node;
import nl.mpi.lat.fabric.NodeID;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class MockAuthorizationService implements AdvAuthorizationService {

    public MockAuthorizationService() {
    }

    @Override
    public NodeAuth getNodeAuth(NodeID nodeID, LatPrincipal pcpl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<NodeAuth> getNodeAuths(NodeID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<NodeAuth> getNodeAuths(List<LatPrincipal> pcpls) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<NodeAuth> getNodeAuths(LatPrincipal pcpl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<NodeAuth> getNodeAuths() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<NodeAuth> getAuthHeredity(NodeID nodeID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public NodePcplRule getEffectiveDomainEditorRule(NodeID nodeID, LatPrincipal pcpl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void save(NodeAuth noau) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void save(List<NodeAuth> noaus) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(NodeAuth noau) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(List<NodeAuth> noaus) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public NodeAuth newNodeAuth() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public NodeAuth newNodeAuth(Node node, LatPrincipal pcpl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public NodePcplRule newNodeRule() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void staleNode(NodeID nodeID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public NodePcplLicense saveLicenseAcceptance(NodeLicense nolics, LatUser user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public NodePcplLicense saveLicenseAcceptance(NodeID nodeID, License lics, LatUser user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<NodeID> filterNodeIDsForAuthorizationRecalculation(List<NodeID> nodeIDs, Set<License> concerendLicenses) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<NodeID> collectNodeIdForAuthorizationRecalculation(LatUser user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<LatGroup> getEnabledGroups(LatUser principal, List<LatGroup> groups) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<DisplayablePrincipal> getDisplayablePrincipals(LatUser latUser) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isAdminOfGroup(LatGroup group, LatUser user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resetNodeAuthListByCreatorOrModifier(LatUser user, LatUser resetUser) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resetNodePcplRuleListByCreatorOrModifier(LatUser user, LatUser resetUser) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isArchiveManager(LatPrincipal user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Node> getHeredity(NodeID nodeID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Node getNode(NodeID nodeID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void equipNode(Node node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LatUser getDomainCurator(Node node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<LatPrincipal> checkDomainCurator(Node node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<LatPrincipal> getDomainCurators() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDomainCurator(LatPrincipal user, Node node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDomainCurator(LatPrincipal user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<LatPrincipal> getDomainManagers(Node node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<LatPrincipal> getDomainManagers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDomainManager(LatPrincipal pcpl, Node node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDomainManager(LatPrincipal pcpl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<LatPrincipal> getDomainEditors(Node node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<NodePcplLicense> getLicenseAcceptance(LatUser usr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<NodeLicense> getLicenseAcceptance(NodeID nid, LatUser usr) {
        return new ArrayList<NodeLicense>();
//        License l = new License();
//       // try {
//        //URI url = new URI("");
//           // url = getClass().getClassLoader().getResource("dobes_coc_v2.html").toURI();
//
//
//        //l.setFile(url.toString());
//        NodeLicense lic = new NodeLicense();
//        lic.setLicense(l);
//        result.add(lic);
////                } catch (URISyntaxException ex) {
////            Logger.getLogger(MockAuthorizationService.class.getName()).log(Level.SEVERE, null, ex);
////        }
//        return result;
    }
    public List<NodeLicense> getLicenseAcceptance(URI nid, LatUser usr) {
        return new ArrayList<NodeLicense>();
//        License l = new License();
//       // try {
//        //URI url = new URI("");
//           // url = getClass().getClassLoader().getResource("dobes_coc_v2.html").toURI();
//
//
//        //l.setFile(url.toString());
//        NodeLicense lic = new NodeLicense();
//        lic.setLicense(l);
//        result.add(lic);
////                } catch (URISyntaxException ex) {
////            Logger.getLogger(MockAuthorizationService.class.getName()).log(Level.SEVERE, null, ex);
////        }
//        return result;
    }

    @Override
    public Set<NodePcplLicense> getLicenseAcceptance(License lics) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isReadable(LatPrincipal user, Node target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isReadable(LatPrincipal user, NodeID targetID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<LatPrincipal> getReaders(Node target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<LatPrincipal> getReaders(NodeID targetID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isWriteable(LatPrincipal user, Node target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isWriteable(LatPrincipal user, NodeID targetID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<LatPrincipal> getWriters(Node target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<LatPrincipal> getWriters(NodeID targetID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getAccessLevel(Node target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
