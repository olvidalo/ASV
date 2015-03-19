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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import nl.mpi.metadatabrowser.model.ActionSelectionRequest;
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
@Component
public class CMDIViewNodeAction extends SingleNodeActionSingletonBean {
    
    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    @Autowired
    private NodeActionsConfiguration nodeActionsConfiguration;
    @Autowired
    private ViewInAnnexAction annexViewAction;
    @Autowired
    private ViewResourceAction resourceViewAction;
    private Set<String> annexMimeTypes;
    private Set<String> optionalAnnexMimeTypes;

    /**
     * Default constructor for spring
     */
    protected CMDIViewNodeAction() {
    }
    
    @PostConstruct
    public void init() {
        final String annexTypes = nodeActionsConfiguration.getAnnexMimeTypes();
        annexMimeTypes = ImmutableSet.copyOf(annexTypes.split("\\s"));
        
        final String optionalAnnexTypes = nodeActionsConfiguration.getOptionalAnnexMimeTypes();
        optionalAnnexMimeTypes = ImmutableSet.copyOf(optionalAnnexTypes.split("\\s"));
    }

    /**
     *
     * @param nodeActionsConfiguration NodeActionsConfiguration, get Annex url
     * @param resourceViewAction
     * @param annexViewAction
     */
    protected CMDIViewNodeAction(NodeActionsConfiguration nodeActionsConfiguration, ViewResourceAction resourceViewAction, ViewInAnnexAction annexViewAction) {
        this.nodeActionsConfiguration = nodeActionsConfiguration;
        this.resourceViewAction = resourceViewAction;
        this.annexViewAction = annexViewAction;
    }
    
    @Override
    protected NodeActionResult execute(final TypedCorpusNode node) throws NodeActionException {
        logger.debug("View on {} requested", node);
        
        if (isOptionalAnnexViewable(node)) {
            final ImmutableList<NodeAction> actionsList = ImmutableList.<NodeAction>of(resourceViewAction, annexViewAction);
            return new SimpleNodeActionResult(new ActionSelectionRequest(actionsList));
        } else if (isAnnexViewable(node)) {
            return annexViewAction.execute(node);
        } else {
            return resourceViewAction.execute(node);
        }
    }
    
    private boolean isAnnexViewable(final TypedCorpusNode node) {
        return annexMimeTypes.contains(node.getFormat());
    }
    
    private boolean isOptionalAnnexViewable(final TypedCorpusNode node) {
        return optionalAnnexMimeTypes.contains(node.getFormat());
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
