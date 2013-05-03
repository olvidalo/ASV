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

import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import nl.mpi.metadatabrowser.model.*;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIBookmarkNodeAction extends SingleNodeAction implements NodeAction {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private final String name = "bookmark";
    Form form;
    private final CmdiCorpusStructureDB csdb;

    public CMDIBookmarkNodeAction(CmdiCorpusStructureDB csdb) {
        this.csdb = csdb;
    }

    @Override
    protected NodeActionResult execute(TypedCorpusNode node) throws NodeActionException {
        URI nodeUri = node.getUri();
        logger.info("Action [{}] invoked on {}", getName(), nodeUri);
        String nodeName = node.getName();
        String nodeId = Integer.toString(node.getNodeId());
        String resolver = csdb.getArchiveRoots().getHandleProxy();
        if (resolver == null) {
            resolver = "";
        }
        if (!resolver.endsWith("/")) {
            resolver = resolver + "/";
        }

        String archive_name = csdb.getArchiveRoots().getArchiveName();
        if (archive_name == null) {
            archive_name = "unknown";
        }

        String url = null;
        try {
            url = nodeUri.toURL().toString();
        } catch (MalformedURLException ex) {
            logger.error("url error while getting URL", ex);
        }


        // HANDLE bookmark action here
        form.add(new Label("nodeId", nodeId));
        form.add(new Label("name", nodeName));
        form.add(new Label("uri", nodeUri.toString()));
        form.add(new Label("url", url));
        form.add(new Label("last_modified", "lastmodified"));
        form.add(new Label("title", "title"));
        form.add(new Label("format", "format"));
        form.add(new Label("archive_name", archive_name));
        form.add(new Label("resolver", resolver));

        final ShowComponentActionRequest request = new ShowComponentActionRequest(form);
        return new SimpleNodeActionResult(request);
    }

    @Override
    public String getName() {
        return name;
    }
}
