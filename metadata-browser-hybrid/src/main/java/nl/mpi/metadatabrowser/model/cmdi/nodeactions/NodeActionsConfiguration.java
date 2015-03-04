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

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

/**
 * Configuration class that holds all the getters and setters for each parameter
 * needed. Parameters are defined in the context.xml from tomcat
 * <p>
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
@Component
public class NodeActionsConfiguration implements Serializable {

    public final static String DEFAULT_ANNEX_MIMETYPES = "text/plain text/x-eaf+xml text/x-chat text/x-shoebox-text text/x-toolbox-text text/x-subrip text/praat-textgrid";

    private String amsURL;
    private String rrsURL;
    private String rrsIndexUrl;
    private String annexURL;
    private String mdSearchURL;
    private String trovaURL;
    private String manualURL;
    private String rrsRegister;
    private String imexUrl;
    private String annexMimeTypes = DEFAULT_ANNEX_MIMETYPES;
    private static final Logger logger = LoggerFactory.getLogger(NodeActionsConfiguration.class);

    /**
     * @getters
     */
    // otherwise the properties don't get automatically injected with the Value annotations
    /**
     *
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     *
     * @return
     */
    public String getTrovaURL() {
        return trovaURL;
    }

    /**
     *
     * @return url for AMS
     */
    public String getAmsURL() {
        return amsURL;
    }

    /**
     *
     * @return value for registration
     */
    public String getRrsRegister() {
        return rrsRegister;
    }

    /**
     *
     * @return value for RRSindex
     */
    public String getRrsIndexURL() {
        return rrsIndexUrl;
    }

    /**
     *
     * @return URL for RRS
     */
    public String getRrsURL() {
        return rrsURL;
    }

    /**
     *
     * @return URL for ANNEX
     */
    public String getAnnexURL() {
        return annexURL;
    }

    /**
     *
     * @return URL for the manual
     */
    public String getManualURL() {
        return manualURL;
    }

    public String getAnnexMimeTypes() {
        return annexMimeTypes;
    }

    /**
     *
     * @return URL for metadata seach
     */
    public String getMdSearchURL() {
        return mdSearchURL;
    }

    public String getImexUrl() {
        return imexUrl;
    }

    /**
     * Setters
     */
    /**
     *
     * @param amsURL
     * <p>
     */
    @Value("${nl.mpi.amsUrl}")
    public void setAmsURL(String amsURL) {
        checkWarning(amsURL, "nl.mpi.amsUrl");
        this.amsURL = amsURL;
    }

    /**
     *
     * @param rrsRegister
     */
    @Value("${nl.mpi.rrsRegister}")
    public void setRrsRegister(String rrsRegister) {
        checkWarning(rrsRegister, "nl.mpi.rrsRegister");
        this.rrsRegister = rrsRegister;
    }

    /**
     *
     * @param rrsIndexURL
     */
    @Value("${nl.mpi.rrsIndex}")
    public void setRrsIndexURL(String rrsIndexURL) {
        checkWarning(rrsIndexURL, "nl.mpi.rrsIndex");
        this.rrsIndexUrl = rrsIndexURL;
    }

    /**
     *
     * @param rrsURL
     */
    @Value("${nl.mpi.rrsUrl}")
    public void setRrsURL(String rrsURL) {
        checkWarning(rrsURL, "nl.mpi.rrsUrl");
        this.rrsURL = rrsURL;
    }

    /**
     *
     * @param annexURL
     */
    @Value("${nl.mpi.annexUrl}")
    public void setAnnexURL(String annexURL) {
        checkWarning(annexURL, "nl.mpi.annexUrl");
        this.annexURL = annexURL;
    }

    /**
     *
     * @param mdSearchURL
     */
    @Value("${nl.mpi.imdiSearchUrl}")
    public void setMdSearchURL(String mdSearchURL) {
        checkWarning(mdSearchURL, "nl.mpi.imdiSearchUrl");
        this.mdSearchURL = mdSearchURL;
    }

    /**
     *
     * @param trovaURL
     */
    @Value("${nl.mpi.trovaUrl}")
    public void setTrovaURL(String trovaURL) {
        checkWarning(trovaURL, "nl.mpi.trovaUrl");
        this.trovaURL = trovaURL;
    }

    @Value("${nl.mpi.metadatabrowser.annexMimeTypes:" + DEFAULT_ANNEX_MIMETYPES + "}")
    public void setAnnexMimeTypes(String annexMimeTypes) {
        this.annexMimeTypes = annexMimeTypes;
    }

    /**
     *
     * @param manualURL
     */
    @Value("${nl.mpi.metadatabrowser.manualUrl}")
    public void setManualURL(String manualURL) {
        checkWarning(manualURL, "nl.mpi.metadatabrowser.manualUrl");
        this.manualURL = manualURL;
    }

    @Value("${nl.mpi.imexUrl}")
    public void setImexUrl(String imexUrl) {
        checkWarning(imexUrl, "nl.mpi.imexUrl");
        this.imexUrl = imexUrl;
    }
    
    

    /**
     * If 'parameter' is null, log an error about 'name' not being set.
     * <p>
     * @param parameter, String of the parameter value that needs to be checked
     * (e.g value of amsURL)
     * @param name, String of the parameter name (e.g amsURL)
     */
    private void checkWarning(String parameter, String name) {
        if (parameter != null) {
            logger.info("{} was initialized with value: {}", name, parameter);
        } else {
            logger.error("Required parameter {} was not initialized properly", name);
        }
    }
}
