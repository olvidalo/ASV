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
package nl.mpi.metadatabrowser.model.cmdi.wicket.components;

import java.net.URI;
import java.util.*;
import nl.mpi.archiving.corpusstructure.provider.AccessInfo;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.corpusstructure.provider.UnknownNodeException;
import nl.mpi.lat.ams.model.License;
import nl.mpi.lat.ams.model.NodeLicense;
import nl.mpi.lat.ams.service.LicenseService;
import nl.mpi.lat.auth.authorization.AdvAuthorizationService;
import nl.mpi.lat.dao.DataSourceException;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.CMDIResourceType;
import nl.mpi.metadatabrowser.services.cmdi.mock.MockAuthorizationService;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public final class ResourcePresentation extends Panel {

    private final ResourceReference openIcon = new PackageResourceReference(ResourcePresentation.class, "al_circle_green.png");
    private final ResourceReference licensedIcon = new PackageResourceReference(ResourcePresentation.class, "al_circle_yellow.png");
    private final ResourceReference restrictedIcon = new PackageResourceReference(ResourcePresentation.class, "al_circle_orange.png");
    private final ResourceReference closedIcon = new PackageResourceReference(ResourcePresentation.class, "al_circle_red.png");
    private final ResourceReference externalIcon = new PackageResourceReference(ResourcePresentation.class, "al_circle_black.png");
    private final static Logger logger = LoggerFactory.getLogger(ResourcePresentation.class);

    public ResourcePresentation(String id, TypedCorpusNode node, CorpusStructureProvider csdb, String userid, LicenseService licenseService, AdvAuthorizationService aSrv) throws UnknownNodeException {
	super(id);
	//String nodeId = Integer.toString(node.getNodeId());
	URI nodeId = node.getNodeId();

	URI nodeURL = csdb.getObjectURI(nodeId);
	if ((nodeURL != null) && (node != null)) {
	    Boolean hasaccess;
	    if (userid == null || userid.equals("") || userid.equals("anonymous")) {
		hasaccess = Boolean.valueOf(csdb.hasReadAccess(nodeId, AccessInfo.EVERYBODY));
	    } else {
		hasaccess = Boolean.valueOf(csdb.hasReadAccess(nodeId, userid));
	    }

	    String handle = csdb.getHandle(node.getNodeId());
	    String nodetype = "unknown";
	    String format = node.getNodeType().getName();
	    String checksum = csdb.getObjectChecksum(node.getNodeId());
	    String size = "unknown";
	    String lastmodified = "unknown";
	    long isize = csdb.getObjectSize(node.getNodeId());

	    if (isize > 0) {
		if (isize < (10 * 1024)) {
		    size = String.valueOf(isize) + " bytes";
		} else if (isize < (10 * 1024 * 1024)) {
		    size = String.valueOf(isize / 1024) + " KB";
		} else {
		    size = String.valueOf(isize / (1024 * 1024)) + " MB";
		}
		Date filetime = csdb.getObjectFileTime(nodeId);
		if (filetime != null) {
		    lastmodified = new Date(filetime.getTime()).toString();
		}
	    }

	    if (node.getNodeType() instanceof CMDIResourceTxtType) {
		nodetype = "Written Resource";
	    } else if (node.getNodeType() instanceof CMDIResourceType) {
		nodetype = "Media Resource";
	    }

	    if (checksum == null) {
		checksum = "unknown";
	    }

	    AccessInfo nAccessInfo = csdb.getObjectAccessInfo(nodeId);

	    int nodeAccessLevel = AccessInfo.ACCESS_LEVEL_NONE;
	    if (nAccessInfo.getAccessLevel() > AccessInfo.ACCESS_LEVEL_NONE) {
		nodeAccessLevel = nAccessInfo.getAccessLevel();
	    }

	    // TODO : get rrsurl
	    String rrsurl = null;
	    final MarkupContainer tableContainer = new WebMarkupContainer("tableContainer");

	    if (nodeAccessLevel == 1) {
		tableContainer.add(new Image("access_icon", openIcon));
		tableContainer.add(new Label("accesslevel", "This resource is openly available"));

	    } else if (nodeAccessLevel == 2) {
		tableContainer.add(new Image("access_icon", licensedIcon));
		tableContainer.add(new Label("accesslevel", "This resource is accessible to registered users of the archive"));

	    } else if (nodeAccessLevel == 3) {
		tableContainer.add(new Image("access_icon", restrictedIcon));
		tableContainer.add(new Label("accesslevel", "Access to this resource can be <a href='" + rrsurl + ">requested</a>"));

	    } else if (nodeAccessLevel == 4) {
		tableContainer.add(new Image("access_icon", closedIcon));
		tableContainer.add(new Label("accesslevel", "Access to this resource is prohibited"));

	    } else if (nodeAccessLevel == 5) {
		tableContainer.add(new Image("access_icon", externalIcon));
		tableContainer.add(new Label("accesslevel", "This resource is external"));
	    } else {
		tableContainer.add(new Image("access_icon", new ContextRelativeResource("")));
		tableContainer.add(new Label("accesslevel", "No access level has been calculated yet"));
	    }


	    // get the licenses for a nodeId
	    List<NodeLicense> licenses = getLicenses(node, aSrv);
	    List<String[]> licenseViews = new ArrayList<String[]>();
	    for (int i = 0; i < licenses.size(); i++) {
		License license = licenses.get(i).getLicense();
		String url = "";
		//TODO replace URL by String url=licenseService.getLicenseLink(license);
		String[] licenseData = new String[2];
		licenseData[0] = license.getName();
		licenseData[1] = url;
		licenseViews.add(licenseData);
	    }

	    StringBuilder sb = new StringBuilder();
	    Label licensesLabel;

	    // create label for licenses
	    if (licenseViews.isEmpty()) {
		licensesLabel = new Label("licenses", "No licenses required for this resource.");
	    } else {
		Iterator<String[]> it = licenseViews.iterator();
		while (it.hasNext()) {
		    String lic[] = it.next();
		    sb.append(lic[0]);
		    sb.append("<a href=\"");
		    sb.append(lic[1]);
		    sb.append("\">");
		    sb.append("</a><br/> ");
		}
		licensesLabel = new Label("licenses", sb.toString());

	    }
	    licensesLabel.setEscapeModelStrings(false);



	    // Add all labels to table container to be displayed in html
	    tableContainer.add(licensesLabel);

	    tableContainer.add(new Label("hasaccess", hasaccess.toString()));
	    tableContainer.add(new Label("nodeId", nodeId.toString()));
	    tableContainer.add(new Label("handle", handle));
//            tableContainer.add(new Label("urid", urid));
	    tableContainer.add(new Label("url", nodeURL.toString()));
	    tableContainer.add(new Label("nodetype", nodetype));
	    tableContainer.add(new Label("format", format));
	    tableContainer.add(new Label("checksum", checksum));
	    tableContainer.add(new Label("size", size));
	    tableContainer.add(new Label("last_modified", lastmodified));

	    tableContainer.add(new Label("userid", userid));

	    tableContainer.add(new ExternalLink("link", nodeURL.toString(), "link"));

	    // Add container to page
	    add(tableContainer);

	}
    }

    private List<NodeLicense> getLicenses(TypedCorpusNode node, AdvAuthorizationService aSrv) {
	List<NodeLicense> result = Collections.EMPTY_LIST;
        
        MockAuthorizationService asrv = (MockAuthorizationService) aSrv;// TODO : remove mock once next TODO is done
	try {
	    //result = aSrv.getLicenseAcceptance(new NodeIDImpl(node.getNodeId().toString()), null); //TODO: Accept URI nodeId's in AMS libraries
            result = asrv.getLicenseAcceptance(node.getNodeId(), null); //TODO: remove line and uncomment previous line when previous TODO is fixed
	} catch (DataSourceException e) {
	    logger.error("Cannot get licenses from AMS for node: " + node + " (This can happen when AMS is not deployed or not running).");
	}
	return result;
    }
}