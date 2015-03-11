/*
 * Copyright (C) 2014 Max Planck Institute for Psycholinguistics
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
package nl.mpi.metadatabrowser.wicket;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import org.apache.wicket.Application;
import static org.apache.wicket.ThreadContext.getRequestCycle;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Model that generates a 'view' link, that is a link to the provided node
 * displayed in the metadata browser. If available, it uses the node handle
 * followed by the '@view' identifier; otherwise it falls back to the openpath
 * parameter
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class NodeViewLinkModel extends AbstractReadOnlyModel<String> {

    private final IModel<? extends Collection<? extends CorpusNode>> model;

    public NodeViewLinkModel(IModel<? extends Collection<? extends CorpusNode>> model) {
        this.model = model;
    }

    @Override
    public String getObject() {
        final Collection<? extends CorpusNode> nodes = model.getObject();
        if (nodes.size() == 1) {
            final CorpusNode node = nodes.iterator().next();
            return getViewNodeLink(node).toString();
        } else {
            // build multi-node openpath request; processing of this not implemented yet!
            return createOpenPathLink(nodes);
        }
    }

    private CharSequence getViewNodeLink(final CorpusNode node) {
        final MetadataBrowserServicesLocator services = MetadataBrowserServicesLocator.Instance.get();
        if (node.isOnSite()) {
            // on-site nodes with a handle can be bookmarked by handle (we can assume the @view identifier works for these)
            final NodeResolver nodeResolver = services.getNodeResolver();
            final URI pid = nodeResolver.getPID(node);
            if (pid != null) {
                final URI handleResolverURI = services.getCorpusStructureProvider().getHandleResolverURI();
                return new StringBuilder(handleResolverURI.toString())
                        .append(pid.getSchemeSpecificPart())
                        .append("@view");
            }
        }
        return createOpenPathLink(Collections.singleton(node));
    }

    private String createOpenPathLink(final Collection<? extends CorpusNode> nodes) {
        final PageParameters params = new PageParameters();
        for (CorpusNode node : nodes) {
            params.add("openpath", node.getNodeURI());
        }
        final String url = getRequestCycle().urlFor(Application.get().getHomePage(), params).toString();
        return RequestCycle.get().getUrlRenderer().renderFullUrl(Url.parse(url));
    }

}
