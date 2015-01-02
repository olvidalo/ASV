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

import java.net.URI;
import java.util.Collection;
import javax.ws.rs.core.UriBuilder;
import nl.mpi.metadatabrowser.model.NodeActionSingletonBean;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.services.NodeIdFilter;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * Class that calls redirect to Annotation Content Search (TROVA).
 */
@Component
public class CMDITrovaNodeAction extends RedirectingNodeAction implements NodeActionSingletonBean, BeanNameAware {

    private final NodeActionsConfiguration nodeActionsConfiguration;
    private final NodeIdFilter nodeIdFilter;
    private String beanName;

    @Autowired
    public CMDITrovaNodeAction(NodeActionsConfiguration nodeActionsConfiguration, NodeIdFilter nodeIdFilter) {
        this.nodeActionsConfiguration = nodeActionsConfiguration;
        this.nodeIdFilter = nodeIdFilter;
    }

    @Override
    public String getName() {
        return "Content Search";
    }

    @Override
    public String getTitle() {
        return "Search the content of the textual materials below the selected branch";
    }

    @Override
    protected URI getTarget(Collection<TypedCorpusNode> nodes) {
        URI targetURI = null;
        UriBuilder uriBuilder = UriBuilder.fromUri(nodeActionsConfiguration.getTrovaURL());
        for (TypedCorpusNode node : nodes) {
            //Buil redirect to trova action
            URI nodeId = node.getNodeURI();
            String nodeid = nodeIdFilter.getURIParam(nodeId);
            targetURI = uriBuilder.queryParam("nodeid", nodeid).build();
        }
        return targetURI;
    }

    @Override
    public String getBeanName() {
        return beanName;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

}
