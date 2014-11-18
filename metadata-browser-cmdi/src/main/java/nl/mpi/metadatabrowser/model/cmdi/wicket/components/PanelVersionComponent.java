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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import nl.mpi.archiving.corpusstructure.core.NodeNotFoundException;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.services.authentication.AccessChecker;
import nl.mpi.metadatabrowser.services.FilterNodeIds;
import nl.mpi.metadatabrowser.services.URIFilter;
import nl.mpi.metadatabrowser.services.cmdi.mock.MockVersioningAPI;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class PanelVersionComponent extends Panel {

    private final Logger logger = LoggerFactory.getLogger(PanelVersionComponent.class);
    @SpringBean
    private FilterNodeIds filterNodeId;
    @SpringBean
    private AccessChecker accessChecker;
    @SpringBean
    private CorpusStructureProvider csdb;
    @SpringBean
    private NodeResolver resolver;
    @SpringBean
    private URIFilter nodeUriFilter;

    public PanelVersionComponent(String id, TypedCorpusNode node, String userid, MockVersioningAPI versions) {
        super(id);
        try {
            List versionsNodeIds = null;

            // create marker for html wicket table
            RepeatingView repeating = new RepeatingView("rowItems");
            add(repeating);

            boolean showRetired = true;

            final URI nodeURI = node.getNodeURI();
            //versions = new VersioningAPI(Configuration.getInstance().versDBConnectionURL);
            if (versions.getStatus("versioningTableInfo")) {
                versionsNodeIds = versions.getAllVersions(nodeURI, showRetired);
            }
            URL nodeURL = resolver.getUrl(node);
            if ((nodeURL != null)) {
                try {
                    final Boolean hasaccess = accessChecker.hasAccess(userid, nodeURI);

                    // loop through the list of versions for a node to write them in the table.
                    if (versionsNodeIds != null && versionsNodeIds.size() > 0) {
                        final String handleResolver = csdb.getHandleResolverURI().toString();
                        addVersionInfo(versionsNodeIds, repeating, node, versions, hasaccess, handleResolver);
                    } else { // list is empty
                        //TODO decide if it is revelant to display table with no value or simply return a message.
                        repeating.add(new Label("hasaccess", hasaccess.toString()));
                        repeating.add(new Label("currentNodeDate", "unknown"));
                        repeating.add(new ExternalLink("linktoNode", "no version found", "link to the node"));
                        repeating.add(new ExternalLink("linktoPID", "no version found", "link to the PID of the node"));
                        add(repeating);
                    }
                } catch (NodeNotFoundException ex) {
                    Session.get().error(ex.getMessage());
                    logger.warn("Node not found: {}", nodeURL, ex);
                }
            }
        } catch (URISyntaxException ex) {
            Session.get().error(ex.getMessage());
            logger.error("Illegal URI", ex);
        }
    }

    private void addVersionInfo(List versionsNodeIds, RepeatingView repeating, TypedCorpusNode node, MockVersioningAPI versions, final Boolean hasaccess, String handleResolver) throws URISyntaxException, IllegalArgumentException, UriBuilderException {
        for (int v = 0; v < versionsNodeIds.size(); v++) {
            // for each loop add a row
            AbstractItem item = new AbstractItem(repeating.newChildId());
            repeating.add(item);
            final URI currentNodeId = new URI(versionsNodeIds.get(v).toString());

            String secureCurrentNodeUrlStr;
            try {
                // allow filter to rewrite, e.g. http->https
                secureCurrentNodeUrlStr = nodeUriFilter.filterURI(resolver.getUrl(node).toURI()).toString();
            } catch (URISyntaxException ex) {
                // highly unlikely
                logger.warn("Node resolver URL was not a valid URI!" + ex.getMessage());
                secureCurrentNodeUrlStr = resolver.getUrl(node).toString();
            }

            // add fields for each row
            Date currentNodeDate = versions.getDateOfVersion(currentNodeId);
            item.add(new Label("hasaccess", hasaccess.toString()));
            item.add(new Label("currentNodeDate", currentNodeDate.toString()));
            item.add(new ExternalLink("linktoNode", secureCurrentNodeUrlStr, filterNodeId.getURIParam(node.getNodeURI())));

            URI nodePID = resolver.getPID(node);
            if (nodePID != null) {
                item.add(new ExternalLink("linktoPID", UriBuilder.fromUri(handleResolver + nodePID.toString()).build().toString(), nodePID.toString()));
            } else {
                item.add(new ExternalLink("linktoPID", "", "no link to the specified node were found"));
            }

            final int idx = v;
            item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
                private static final long serialVersionUID = 1L;

                @Override
                public String getObject() {
                    return (idx % 2 == 1) ? "even" : "odd";
                }
            }));
        }
    }
}
