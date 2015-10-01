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
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import nl.mpi.archiving.corpusstructure.core.ExtendedCorpusNode;
import nl.mpi.archiving.corpusstructure.core.NodeNotFoundException;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.corpusstructure.provider.VersionInfoProvider;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.services.authentication.AccessChecker;
import nl.mpi.metadatabrowser.services.NodeIdFilter;
import nl.mpi.metadatabrowser.services.URIFilter;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Session;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class VersionInfoPanel extends Panel {

    private final Logger logger = LoggerFactory.getLogger(VersionInfoPanel.class);
    @SpringBean
    private NodeIdFilter nodeIdFilter;
    @SpringBean
    private AccessChecker accessChecker;
    @SpringBean
    private CorpusStructureProvider csdb;
    @SpringBean
    private NodeResolver resolver;
    @SpringBean
    private VersionInfoProvider versionInfoProvider;
    @SpringBean
    private URIFilter nodeUriFilter;

    /**
     * Date converter for the date label; usage of patterns is described at
     * {@link http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html?is-external=true}
     */
    private final static DateConverter labelDateConverter = new PatternDateConverter("d MMM yyyy HH:mm", false);
    private final static DateConverter tooltipDateConverter = new PatternDateConverter("EEE, d MMM yyyy HH:mm:ss.SSS Z", false);

    public VersionInfoPanel(String id, TypedCorpusNode node, String userid) {
        super(id);
        try {
            // create marker for html wicket table
            final RepeatingView repeating = new RepeatingView("rowItems");
            add(repeating);

            add(new Label("nodeName", node.getName()));

            final URI nodeURI = node.getNodeURI();
            final Collection<ExtendedCorpusNode> versionsNodes = versionInfoProvider.getAllVersions(nodeURI);
            final URL nodeURL = resolver.getUrl(node);
            if ((nodeURL != null)) {
                final boolean infoFound = versionsNodes != null && versionsNodes.size() > 0;
                if (infoFound) {
                    try {
                        // loop through the list of versions for a node to write them in the table.
                        final String handleResolver = csdb.getHandleResolverURI().toString();
                        addVersionInfo(versionsNodes, repeating, userid, handleResolver);
                    } catch (NodeNotFoundException ex) {
                        Session.get().error(ex.getMessage());
                        logger.warn("Node not found: {}", nodeURL, ex);
                    }
                }
                add(new WebMarkupContainer("infoNotFound").setVisible(!infoFound));
            }
        } catch (URISyntaxException ex) {
            Session.get().error(ex.getMessage());
            logger.error("Illegal URI", ex);
        }
    }

    private void addVersionInfo(Collection<ExtendedCorpusNode> versionsNodes, RepeatingView repeating, String userId, String handleResolver) throws URISyntaxException, IllegalArgumentException, UriBuilderException, NodeNotFoundException {
        for (ExtendedCorpusNode node : versionsNodes) {

            // for each loop add a row
            AbstractItem item = new AbstractItem(repeating.newChildId());
            repeating.add(item);

            final URI nodeURI = node.getNodeURI();

            final String nodeUrl = getNodeUrl(node);

            final Boolean hasaccess = accessChecker.hasAccess(userId, nodeURI);
            // add fields for each row
            final Date currentNodeDate = versionInfoProvider.getDateOfVersion(nodeURI);
            final URI newVersion = versionInfoProvider.getNewerVersionUri(nodeURI);
            item.add(new Label("hasaccess", hasaccess ? "yes" : "no"));
            item.add(new DateLabel("currentNodeDate", Model.of(currentNodeDate), labelDateConverter) {

                @Override
                protected void onConfigure() {
                    // only show date if this is not the current, i.e. if there is a newer version
                    setVisible(newVersion != null);
                }

            }.add(new AttributeAppender("title", new AbstractReadOnlyModel<String>() {

                @Override
                public String getObject() {
                    return tooltipDateConverter.convertToString(currentNodeDate, Locale.getDefault());
                }
            })));
            item.add(new MarkupContainer("current") {

                @Override
                protected void onConfigure() {
                    // only show 'current' label if this is the current, i.e. if there is no newer version
                    setVisible(newVersion == null);
                }

            });
            item.add(new ExternalLink("linktoNode", nodeUrl, nodeIdFilter.getURIParam(nodeURI)));

            URI nodePID = resolver.getPID(node);
            if (nodePID != null) {
                item.add(new ExternalLink("linktoPID", UriBuilder.fromUri(handleResolver + nodePID.toString()).build().toString(), nodePID.toString()));
            } else {
                item.add(new ExternalLink("linktoPID", "", "no persistent identifier available"));
            }

//            final int idx = v;
//            item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
//                private static final long serialVersionUID = 1L;
//
//                @Override
//                public String getObject() {
//                    return (idx % 2 == 1) ? "even" : "odd";
//                }
//            }));
        }
    }

    private String getNodeUrl(ExtendedCorpusNode node) {
        String nodeUrl;
        try {
            // allow filter to rewrite, e.g. http->https
            nodeUrl = nodeUriFilter.filterURI(resolver.getUrl(node).toURI()).toString();
        } catch (URISyntaxException ex) {
            // highly unlikely
            logger.warn("Node resolver URL was not a valid URI!" + ex.getMessage());
            nodeUrl = resolver.getUrl(node).toString();
        }
        return nodeUrl;
    }
}
