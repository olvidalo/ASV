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
package nl.mpi.metadatabrowser.services.cmdi.impl;

import nl.mpi.metadatabrowser.model.cmdi.wicket.components.ResourcePresentation;
import java.util.Collection;
import java.util.Iterator;
import nl.mpi.common.util.spring.SpringContextLoader;
import nl.mpi.lat.ams.Constants;
import nl.mpi.lat.ams.service.LicenseService;
import nl.mpi.lat.auth.authorization.AdvAuthorizationService;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.*;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDINodePresentationProvider implements nl.mpi.metadatabrowser.services.NodePresentationProvider {

    private SpringContextLoader contextLoader;
    private AdvAuthorizationService authoSrv;
    private LicenseService licSrv;
    private CmdiCorpusStructureDB csdb;

    public CMDINodePresentationProvider(CmdiCorpusStructureDB csdb){
        this.csdb = csdb;
    }
    /**
     *
     * Initialises the context loader that will retrieve the beans from the
     * configuration provided by AMS2.
     */
    private void initialiseContextLoader() {
        this.contextLoader = new SpringContextLoader();
        this.contextLoader.init("spring-ams2-core.xml");
    }

    /**
     *
     * @return AuthorizationService bean, to be used by the connection to AMS2
     */
    public AdvAuthorizationService authorizationService() {

        if (this.contextLoader == null) {
            initialiseContextLoader();
        }
        if (this.authoSrv == null) {
            this.authoSrv = (AdvAuthorizationService) contextLoader.getBean(Constants.BEAN_AUTHORIZATION_SRV);
        }
        return this.authoSrv;
    }

    /**
     * @return LicenseService bean, to be used by the connection to AMS2
     */
    public LicenseService licenseService() {

        if (this.contextLoader == null) {
            initialiseContextLoader();
        }
        if (this.licSrv == null) {
            this.licSrv = (LicenseService) contextLoader.getBean(Constants.BEAN_LICENSE_SRV);
        }
        return this.licSrv;
    }

    @Override
    public Component getNodePresentation(String wicketId, Collection<TypedCorpusNode> nodes) {
        //TODO: Implement actual presentation

        //TODO: for resource for id "resourcePresentation" 
        //get TypedCorpusNode node, 
        //getCmdiCorpusStructureDB csdb, 
        //get String userid, 
        //get FedUID fedUid, 
        //get LicenseService licenseService, 
        //get AdvAuthorizationService aSrv.
        // mostly beans ????
        Iterator<TypedCorpusNode> iterator = nodes.iterator();
        while (iterator.hasNext()) {
            TypedCorpusNode node = iterator.next();
            if (node instanceof CMDIMetadata) {
            } else if (node instanceof CMDIResourceTxtType || node instanceof CMDIResourceType) {
                return new ResourcePresentation(wicketId, node , csdb, wicketId, licSrv, authoSrv);
            }
        }



        return new Label(wicketId, nodes.toString());
    }
}
