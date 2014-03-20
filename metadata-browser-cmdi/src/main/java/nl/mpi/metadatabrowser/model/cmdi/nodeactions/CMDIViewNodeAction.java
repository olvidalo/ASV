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
package nl.mpi.metadatabrowser.model.cmdi.nodeactions;

import java.net.MalformedURLException;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.ControllerActionRequestException;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.model.SingleNodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.NavigationActionRequest;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDIResourceAudioType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDIResourceVideoType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDIResourceWrittenType;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.AudioFilePanel;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.MediaFilePanel;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.ViewInfoFile;
import nl.mpi.metadatabrowser.services.FilterNodeIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
@Component
public class CMDIViewNodeAction extends SingleNodeAction implements NodeAction {

    private NodeResolver resolver;
    private final CorpusStructureProvider csdb;
    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private final NodeActionsConfiguration nodeActionsConfiguration;
    @Autowired
    private FilterNodeIds filterNodeId;

    @Autowired
    public CMDIViewNodeAction(NodeActionsConfiguration nodeActionsConfiguration, NodeResolver nodeResolver, CorpusStructureProvider csdb) {
        this.nodeActionsConfiguration = nodeActionsConfiguration;
        this.resolver = nodeResolver;
        this.csdb = csdb;
    }

    @Override
    protected NodeActionResult execute(final TypedCorpusNode node) throws NodeActionException {
        //Buil redirect to Annex here
        logger.debug("Action [{}] invoked on {}", getName(), node);
        URI targetURI = null;
        UriBuilder uriBuilder = UriBuilder.fromPath(nodeActionsConfiguration.getAnnexURL());
//        String[] formats = SearchClient.getSearchableFormats();
//        List<String> formatslist = new ArrayList<String>(formats.length);
//        formatslist.addAll(Arrays.asList(formats));
        boolean navType = false;
        if (node.getNodeType() instanceof CMDIResourceTxtType || node.getNodeType() instanceof IMDIResourceWrittenType && node.getName().endsWith(".srt") || node.getName().endsWith(".eaf") || node.getName().endsWith("tbt") || node.getName().endsWith(".cha")) {
            //TODO get session id
            URI nodeid = node.getNodeURI();// should be handle
            String nodeId = filterNodeId.getURIParam(nodeid);
            navType = true;
            if ((nodeId.toString().startsWith("hdl")) || nodeId.toString().startsWith("1839")) {
                targetURI = uriBuilder.queryParam("handle", nodeId).queryParam("jsessionID", "session_id").build();
            } else {
                targetURI = uriBuilder.queryParam("nodeid", nodeId).queryParam("jsessionID", "session_id").build();
            }
        }

        if (navType == true) {
            try {
                final NavigationActionRequest request = new NavigationActionRequest(targetURI.toURL());
                return new SimpleNodeActionResult(request);
            } catch (MalformedURLException ex) {
                logger.error("URL syntax exception:" + ex);
            }
        } else if (node.getNodeType() instanceof IMDIResourceVideoType) {
            final ShowComponentRequest componentRequest = new ShowComponentRequest() {
                @Override
                public org.apache.wicket.Component getComponent(String id) throws ControllerActionRequestException {
                    return new MediaFilePanel(id, resolver, node, nodeActionsConfiguration, csdb);
                }
            };
            return new SimpleNodeActionResult(componentRequest);

        } else if (node.getNodeType() instanceof IMDIResourceAudioType) {
            final ShowComponentRequest componentRequest = new ShowComponentRequest() {
                @Override
                public org.apache.wicket.Component getComponent(String id) throws ControllerActionRequestException {
                    return new AudioFilePanel(id, resolver, node, nodeActionsConfiguration, csdb);
                }
            };
            return new SimpleNodeActionResult(componentRequest);

        } else {
            final ShowComponentRequest componentRequest = new ShowComponentRequest() {
                @Override
                public org.apache.wicket.Component getComponent(String id) throws ControllerActionRequestException {
                    return new ViewInfoFile(id, resolver, node);
                }
            };
            return new SimpleNodeActionResult(componentRequest);
        }
        return null;
    }

    @Override
    public String getName() {
        return "View";
    }

    @Override
    public String getTitle() {
        return "View this resource";
    }
}
