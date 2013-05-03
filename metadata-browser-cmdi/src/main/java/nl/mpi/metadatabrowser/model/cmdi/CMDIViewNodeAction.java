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
package nl.mpi.metadatabrowser.model.cmdi;

import org.apache.wicket.markup.html.form.TextArea;
import java.io.PrintWriter;
import java.net.URI;
import java.util.*;
import nl.mpi.corpusstructure.ArchiveAccessContext;
import nl.mpi.metadatabrowser.model.*;
import nl.mpi.util.OurURL;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.resource.IResourceStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIViewNodeAction extends SingleNodeAction implements NodeAction {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private final String name = "view Node";
    private String feedbackMessage;
    private String exceptionMessage;
    private Map<String, String> parameters = new HashMap<String, String>();
    private final CmdiCorpusStructureDB csdb;
    private boolean navType = false;

    public CMDIViewNodeAction(CmdiCorpusStructureDB csdb) {
        this.csdb = csdb;
    }

    @Override
    protected NodeActionResult execute(TypedCorpusNode node) throws NodeActionException {

        URI nodeUri = node.getUri();
        String nodeId = Integer.toString(node.getNodeId());
        OurURL nodeURL = csdb.getObjectURL(nodeId, ArchiveAccessContext.HTTP_URL); // Get the XML file
        String xmlContent = null;
        PrintWriter out = null;
        Map<String, String> parameters = new HashMap<String, String>();
        TextArea content;
        logger.info("Action [{}] invoked on {}", getName(), nodeUri);

//        String[] formats = SearchClient.getSearchableFormats();
//        List<String> formatslist = new ArrayList<String>(formats.length);
//        formatslist.addAll(Arrays.asList(formats));

        if ((nodeURL != null) && (node != null)) {
            if (node.getNodeType() instanceof CMDIMetadata
                    || node.getNodeType() instanceof CMDICollectionType) {
                xmlContent = nodeURL.toString();
            }//all formats that should be handled by annex
            else if (node.getNodeType() instanceof CMDIResourceTxtType || node.getNodeType() instanceof CMDIResourceType) {
                parameters.put("nodeId", nodeId);
                parameters.put("jsessionID", "jsessioID");
                navType = true;
            } else {
                xmlContent = nodeURL.toString();
            }
        }

//        String nodeName = node.getName();

//        String resolver = csdb.getArchiveRoots().getHandleProxy();
//        if (resolver == null) {
//            resolver = "";
//        }
//        if (!resolver.endsWith("/")) {
//            resolver = resolver + "/";
//        }
//
//        String archive_name = csdb.getArchiveRoots().getArchiveName();
//        if (archive_name == null) {
//            archive_name = "unknown";
//        }
//
//        String url = null;
//        try {
//            url = nodeUri.toURL().toString();
//        } catch (MalformedURLException ex) {
//            logger.error("url error while getting URL", ex);
//        }

        content = new TextArea(xmlContent);


        if (navType == true) {
            final NavigationActionRequest request = new NavigationActionRequest(NavigationRequest.NavigationTarget.Annex, parameters);
            return new SimpleNodeActionResult(request);
        } else {
            final ShowComponentActionRequest request = new ShowComponentActionRequest(content);
            return new SimpleNodeActionResult(request);
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
