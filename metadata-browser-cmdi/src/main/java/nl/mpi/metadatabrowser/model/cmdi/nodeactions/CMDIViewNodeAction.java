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
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.core.UriBuilder;
import nl.mpi.annot.search.lib.SearchClient;
import nl.mpi.archiving.corpusstructure.adapter.AdapterUtils;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
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
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.ViewInfoFile;
import nl.mpi.metadatabrowser.services.NodePresentationException;
import nl.mpi.metadatabrowser.services.NodePresentationProvider;
import org.apache.wicket.spring.injection.annot.SpringBean;
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
    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private final static String name = "View Node";
    private final NodeActionsConfiguration nodeActionsConfiguration;

    @Autowired
    public CMDIViewNodeAction(NodeActionsConfiguration nodeActionsConfiguration, NodeResolver nodeResolver) {
        this.nodeActionsConfiguration = nodeActionsConfiguration;
        this.resolver = nodeResolver;
    }

    @Override
    protected NodeActionResult execute(final TypedCorpusNode node) throws NodeActionException {
        //Buil redirect to Annex here
        logger.debug("Action [{}] invoked on {}", getName(), node);
        URI targetURI = null;
        UriBuilder uriBuilder = UriBuilder.fromPath(nodeActionsConfiguration.getAnnexURL());
        String[] formats = SearchClient.getSearchableFormats();
        List<String> formatslist = new ArrayList<String>(formats.length);
        formatslist.addAll(Arrays.asList(formats));
        boolean navType = false;
        if (node.getNodeType() instanceof CMDIResourceTxtType && formatslist.contains(node.getFormat())) {
            //TODO get session id
            URI nodeid = node.getNodeURI();// should be handle
            String nodeId = AdapterUtils.toNodeIdString(nodeid);
            navType = true;
            if (("".equals(nodeId.toString()) || !nodeId.toString().startsWith("hdl")) || !nodeId.toString().startsWith("1839")) {
                targetURI = uriBuilder.queryParam("nodeid", nodeId).queryParam("jsessionID", "session_id").build();
            } else {
                targetURI = uriBuilder.queryParam("handle", nodeId).queryParam("jsessionID", "session_id").build();
            }
        }
//        } else{
//            URL nodeURL = resolver.getUrl(node);
//            if(nodeURL.getProtocol().equals("file")){
//
//            }
//        }


        if (navType == true) {
            try {
                final NavigationActionRequest request = new NavigationActionRequest(targetURI.toURL());
                return new SimpleNodeActionResult(request);
            } catch (MalformedURLException ex) {
                logger.error("URL syntax exception:" + ex);
            }
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
        return name;
    }
}
