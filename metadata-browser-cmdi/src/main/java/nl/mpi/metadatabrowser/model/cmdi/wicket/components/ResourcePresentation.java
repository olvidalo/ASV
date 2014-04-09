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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ws.rs.core.UriBuilder;
import nl.mpi.archiving.corpusstructure.core.AccessLevel;
import nl.mpi.archiving.corpusstructure.core.FileInfo;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.AccessInfoProvider;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.nodeactions.NodeActionsConfiguration;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceType;
import nl.mpi.metadatabrowser.services.AmsService;
import nl.mpi.metadatabrowser.services.AuthenticationHolder;
import nl.mpi.metadatabrowser.services.FilterNodeIds;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public final class ResourcePresentation extends Panel {

    @SpringBean
    private FilterNodeIds filterNodeId;
    @SpringBean
    private AccessInfoProvider accessInfoProvider;
    @SpringBean
    private NodeResolver resolver;
    @SpringBean
    private AmsService amsService;
    @SpringBean
    protected AuthenticationHolder auth;
    @SpringBean
    private NodeActionsConfiguration nodeActionsConfiguration;
    private final ResourceReference openIcon = new PackageResourceReference(ResourcePresentation.class, "al_circle_green.png");
    private final ResourceReference licensedIcon = new PackageResourceReference(ResourcePresentation.class, "al_circle_yellow.png");
    private final ResourceReference restrictedIcon = new PackageResourceReference(ResourcePresentation.class, "al_circle_orange.png");
    private final ResourceReference closedIcon = new PackageResourceReference(ResourcePresentation.class, "al_circle_red.png");
    private final ResourceReference externalIcon = new PackageResourceReference(ResourcePresentation.class, "al_circle_black.png");
    private final static Logger logger = LoggerFactory.getLogger(ResourcePresentation.class);

    public ResourcePresentation(String id, TypedCorpusNode node) {
        super(id);
        //String nodeId = Integer.toString(node.getNodeURI());
        final String userid = auth.getPrincipalName();
        String nodeid = filterNodeId.getURIParam(node.getNodeURI());
        final String nodeURL = nodeActionsConfiguration.processLinkProtocol(resolver.getUrl(node).toString(), nodeActionsConfiguration.getForceHttpOrHttps().equals("https"));
        if (nodeURL != null) {
            Boolean hasaccess;
            if (userid == null || userid.equals("") || userid.equals("anonymous")) {
                hasaccess = accessInfoProvider.hasReadAccess(node.getNodeURI(), AccessInfoProvider.EVERYBODY);
            } else {
                hasaccess = accessInfoProvider.hasReadAccess(node.getNodeURI(), userid);
            }

            String wrapHandle = "";
            URI handle = node.getPID();
            if (handle != null) {
                wrapHandle = handle.toString();
                if (wrapHandle.contains(":")) {
                    wrapHandle = wrapHandle.split(":")[1];
                    wrapHandle = "http://hdl.handle.net/" + wrapHandle;
                }
            }
            String nodetype = "unknown";
            String format = node.getFormat();

            final FileInfo fileInfo = node.getFileInfo();
            String checksum = fileInfo.getChecksum();
            String size = "unknown";
            String lastmodified = "unknown";
            long isize = fileInfo.getSize();

            if (isize > 0) {
                if (isize < (10 * 1024)) {
                    size = String.valueOf(isize) + " bytes";
                } else if (isize < (10 * 1024 * 1024)) {
                    size = String.valueOf(isize / 1024) + " KB";
                } else {
                    size = String.valueOf(isize / (1024 * 1024)) + " MB";
                }
                final Date filetime = fileInfo.getFileTime();
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


//	    AccessLevel nodeAccessLevel = AccessLevel.ACCESS_LEVEL_UNKNOWN;
//	    if (nAccessInfo.getAccessLevel() > AccessLevel.ACCESS_LEVEL_UNKNOWN) {
            AccessLevel nodeAccessLevel = accessInfoProvider.getAccessLevel(node.getNodeURI());
//	    }

            final MarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
            ExternalLink requestLink = null;

            if (nodeAccessLevel == AccessLevel.ACCESS_LEVEL_OPEN_EVERYBODY) {
                tableContainer.add(new Image("access_icon", openIcon));
                tableContainer.add(new Label("accesslevel", "This resource is openly available"));

            } else if (nodeAccessLevel == AccessLevel.ACCESS_LEVEL_OPEN_REGISTERED_USERS) {
                tableContainer.add(new Image("access_icon", licensedIcon));
                tableContainer.add(new Label("accesslevel", "This resource is accessible to registered users of the archive"));

            } else if (nodeAccessLevel == AccessLevel.ACCESS_LEVEL_PERMISSION_NEEDED) {
                UriBuilder rrsurl = UriBuilder.fromUri(nodeActionsConfiguration.getRrsURL() + nodeActionsConfiguration.getRrsIndexURL());
                URI requestedUri = rrsurl.queryParam("nodeid", nodeid).build();
                requestLink = new ExternalLink("requestLink", requestedUri.toString(), "requested") {
                    @Override
                    protected void onComponentTag(ComponentTag tag) {
                        super.onComponentTag(tag);
                        tag.put("target", "_blank");
                    }
                };
                tableContainer.add(new Image("access_icon", restrictedIcon));
                tableContainer.add(new Label("accesslevel", "Access to this resource can be "));
            } else if (nodeAccessLevel == AccessLevel.ACCESS_LEVEL_CLOSED) {
                tableContainer.add(new Image("access_icon", closedIcon));
                tableContainer.add(new Label("accesslevel", "Access to this resource is prohibited"));

            } else if (nodeAccessLevel == AccessLevel.ACCESS_LEVEL_EXTERNAL) {
                tableContainer.add(new Image("access_icon", externalIcon));
                tableContainer.add(new Label("accesslevel", "This resource is external"));
            } else {
                Image icon  = new Image("access_icon", "");
                icon.setVisible(false);
                tableContainer.add(icon);
                tableContainer.add(new Label("accesslevel", "No access level has been calculated yet"));
            }
            if (requestLink == null) {
                requestLink = new ExternalLink("requestLink", "");
                requestLink.setVisible(false);
            }

            // get the licenses for a nodeId
            List<String[]> licenseViews = amsService.getLicense(node.getNodeURI());

            StringBuilder sb = new StringBuilder();
            Label licensesLabel;

            // create label for licenses
            if (licenseViews.isEmpty()) {
                licensesLabel = new Label("licenses", "No licenses required for this resource.");
            } else {
                Iterator<String[]> it = licenseViews.iterator();
                while (it.hasNext()) {
                    String lic[] = it.next();
                    sb.append("<a href=\"");
                    sb.append(lic[1]);
                    sb.append("\">");
                    sb.append(lic[0]);
                    sb.append("</a><br/> ");
                }
                licensesLabel = new Label("licenses", sb.toString());

            }
            licensesLabel.setEscapeModelStrings(false);



            // Add all labels to table container to be displayed in html
            tableContainer.add(licensesLabel);
            if (hasaccess) {
                tableContainer.add(new Label("hasaccess", "yes"));
            } else {
                tableContainer.add(new Label("hasaccess", "no"));
            }
            tableContainer.add(new Label("nodeId", nodeid));
            if (handle != null) {
                tableContainer.add(new ExternalLink("handle", wrapHandle, wrapHandle));
            } else {
                tableContainer.add(new ExternalLink("handle", "", "no handle was found for this resource"));
            }
            tableContainer.add(new ExternalLink("url", nodeURL, nodeURL));
            tableContainer.add(new Label("nodetype", nodetype));
            tableContainer.add(new Label("format", format));
            tableContainer.add(new Label("checksum", checksum));
            tableContainer.add(new Label("size", size));
            tableContainer.add(new Label("last_modified", lastmodified));

            tableContainer.add(new Label("userid", userid));

            tableContainer.add(new ExternalLink("link", nodeURL, "link"));
            tableContainer.add(requestLink);
            // Add container to page
            add(tableContainer);

        }
    }
}
