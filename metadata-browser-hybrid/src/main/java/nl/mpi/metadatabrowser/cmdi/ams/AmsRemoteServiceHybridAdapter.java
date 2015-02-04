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
import nl.mpi.archiving.corpusstructure.core.service.ams.AmsAuthorizationService;
import nl.mpi.archiving.corpusstructure.core.service.ams.AmsLicenseService;

import nl.mpi.metadatabrowser.services.AmsService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * This class uses the old ams based on corpus structure. It is an implementation of the Ams service that help retrieve licenses
 */
public class AmsRemoteServiceHybridAdapter implements AmsService {

    @Autowired
    private AmsAuthorizationService authService;
    @Autowired
    private AmsLicenseService licenseService;

    /**
     * Method that return a List of licenses for a specific node as String. String will be further use for display
     * @param nodeURI URI of the node for whcih we want licenses
     * @return List<String>, list of licenses as String for the given node URI
     */
    @Override
    public List<String[]> getLicense(URI nodeURI) {
        List<String[]> licenseViews = new ArrayList<String[]>();
        List<nl.mpi.archiving.corpusstructure.core.service.ams.AmsLicense> licenses = authService.getLicenseAcceptance(nodeURI, null);
//        final List<AmsLicense> newLicenses = new ArrayList<>(licenses.size());
        for (nl.mpi.archiving.corpusstructure.core.service.ams.AmsLicense license : licenses) {
            String url = licenseService.getLicenseLink(license);
            String[] licenseData = new String[2];
            licenseData[0] = license.getName();
            licenseData[1] = url;
            licenseViews.add(licenseData);
//            nl.mpi.lat.ams.AmsLicense li = new AmsLicense();
//            li.setLinkToLicense(url);
//            newLicenses.add(li);
        }
        return licenseViews;
    }
}
