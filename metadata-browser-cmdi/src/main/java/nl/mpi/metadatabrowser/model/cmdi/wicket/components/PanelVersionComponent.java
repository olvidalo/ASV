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
import nl.mpi.archiving.corpusstructure.core.UnknownNodeException;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.AccessInfoProvider;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
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
    private AccessInfoProvider accessInfoProvider;

    public PanelVersionComponent(String id, TypedCorpusNode node, CorpusStructureProvider csdb, NodeResolver resolver, String userid, MockVersioningAPI versions) {
	super(id);
	try {
	    List versionsNodeIds = null;

	    // create marker for html wicket table
	    RepeatingView repeating = new RepeatingView("rowItems");
	    add(repeating);

	    boolean showRetired = true;

	    //versions = new VersioningAPI(Configuration.getInstance().versDBConnectionURL);
	    if (versions.getStatus("versioningTableInfo")) {
		versionsNodeIds = versions.getAllVersions(node.getNodeURI(), showRetired);
	    }
	    URL nodeURL = resolver.getUrl(node);
	    if ((nodeURL != null)) {
		Boolean hasaccess; // check accessibility node for the user
		if (userid == null || userid.equals("") || userid.equals("anonymous")) {
		    hasaccess = Boolean.valueOf(accessInfoProvider.hasReadAccess(node.getNodeURI(), AccessInfoProvider.EVERYBODY));
		} else {
		    hasaccess = Boolean.valueOf(accessInfoProvider.hasReadAccess(node.getNodeURI(), userid));
		}

		// loop through the list of versions for a node to write them in the table.
		if (versionsNodeIds != null && versionsNodeIds.size() > 0) {
		    for (int v = 0; v < versionsNodeIds.size(); v++) {
			// for each loop add a row
			AbstractItem item = new AbstractItem(repeating.newChildId());
			repeating.add(item);

			URI currentNodeId = new URI(versionsNodeIds.get(v).toString());
//                        URI currentNodePid = csdb.getObjectPID(currentNodeId);
//                        URI currentNodeUrlStr = null;
//                        if (currentNodePid != null) {
//                            currentNodeUrlStr = csdb.getObjectPID(nodeId);
//                        } else {
//                            URI currentNodeUrl = csdb.getObjectURI(currentNodeId);
//                            if (currentNodeUrl != null) {
//                                currentNodeUrlStr = currentNodeUrl;
//                            }
//                        }

			URL currentNodeUrlStr = resolver.getUrl(node);

			// add fields for each row
			// TODO check wicket links when real node URI is available
			Date currentNodeDate = versions.getDateOfVersion(currentNodeId);
			item.add(new Label("hasaccess", hasaccess.toString()));
			item.add(new Label("currentNodeDate", currentNodeDate.toString()));
			item.add(new ExternalLink("linktoNode", currentNodeUrlStr.toString(), "link to the node"));
			item.add(new ExternalLink("linktoPID", currentNodeUrlStr.toString(), "link to the PID of the node"));

			final int idx = v;
			item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
			    private static final long serialVersionUID = 1L;

			    @Override
			    public String getObject() {
				return (idx % 2 == 1) ? "even" : "odd";
			    }
			}));
		    }

		} else { // list is empty
		    //TODO decide if it is revelant to display table with no value or simply return a message.
		    repeating.add(new Label("hasaccess", hasaccess.toString()));
		    repeating.add(new Label("currentNodeDate", "unknown"));
		    repeating.add(new ExternalLink("linktoNode", "no version found", "link to the node"));
		    repeating.add(new ExternalLink("linktoPID", "no version found", "link to the PID of the node"));
		    add(repeating);
		}
	    }
	} catch (URISyntaxException ex) {
	    Session.get().error(ex.getMessage());
	    logger.error("", ex);
	} catch (UnknownNodeException ex) {
	    Session.get().error(ex.getMessage());
	    logger.error("", ex);
	}
    }
}
