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
package nl.mpi.metadatabrowser.cmdi.ams;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import nl.mpi.lat.ams.AmsLicense;
import nl.mpi.lat.ams.IAmsRemoteService;
import nl.mpi.metadatabrowser.services.AmsService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * implements the service for ams. 
 */
public class AmsRemoteServiceCMDIAdapter implements AmsService {

    @Autowired
    private IAmsRemoteService amsRemoteService;

    /**
     * Method that return a number of licenses for a specific node as a List of String. The String will be used for display
     * @param nodeURI, URI of the node for which we want licenses
     * @return List<String> of licenses for the given node URI
     */
    @Override
    public List<String[]> getLicense(URI nodeURI) {
        List<String[]> licenseViews = new ArrayList<>();
        List<AmsLicense> licenses = amsRemoteService.getLicenseAcceptance(nodeURI.toString());
        for (AmsLicense license : licenses) {
            String[] licenseData = new String[2];
            licenseData[0] = license.getName();
            licenseData[1] = license.getLinkToLicense();
            licenseViews.add(licenseData);
        }
        return licenseViews;
    }
}
