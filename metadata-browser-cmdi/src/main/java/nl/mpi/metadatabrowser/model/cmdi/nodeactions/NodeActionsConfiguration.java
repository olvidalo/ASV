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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

//@PropertySource(value = "classpath:config/production/defaultServiceLocations.properties")
/**
 * Configuration class that holds all the getters and setters for each parameter
 * needed. Parameters are defined in the context.xml from tomcat
 * <p>
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
@Component
public class NodeActionsConfiguration implements Serializable {

    private String amsURL;
    private String rrsURL;
    private String rrsIndexUrl;
    private String annexURL;
    private String mdSearchURL;
    private String yamsSearchURL;
    private String trovaURL;
    private String manualURL;
    private String rrsRegister;
    private String forceHttpOrHttps;
    private String forceHttpsPrefix;
    private String amscs2URL;
    private static final Logger logger = Logger.getLogger(NodeActionsConfiguration.class.getName());
    // what to assume as default for "same" protocol if it is not known whether https is used
    final boolean ssl = false;

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
     * @return url for AMS based on cs2
     */
    public String getAmsURLForcs2() {
        return amscs2URL;
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

    /**
     *
     * @return URL for metadata seach
     */
    public String getMdSearchURL() {
        return mdSearchURL;
    }

    /**
     *
     * @return URL for yams seach
     */
    String getYamsSearchURL() {
        return yamsSearchURL;
    }

    /**
     *
     * @return value for secure connection
     */
    public String getForceHttpOrHttps() {
        return forceHttpOrHttps;
    }

    /**
     *
     * @return prefix URL for nodes
     */
    public String getForceHttpsPrefix() {
        return forceHttpsPrefix;
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
        amsURL = processLinkProtocol(amsURL, ssl);
        checkWarning(amsURL, "nl.mpi.amsUrl");
        this.amsURL = amsURL;
    }

    /**
     *
     * @param amscs2Url
     * <p>
     */
    @Value("${nl.mpi.amscs2Url}")
    public void setAmsURLForcs2(String amscs2Url) {
        amscs2Url = processLinkProtocol(amscs2Url, ssl);
        checkWarning(amscs2Url, "nl.mpi.amscs2Url");
        this.amscs2URL = amscs2Url;
    }

    /**
     *
     * @param rrsRegister
     */
    @Value("${nl.mpi.rrsRegister}")
    public void setRrsRegister(String rrsRegister) {
        rrsRegister = processLinkProtocol(rrsRegister, ssl);
        checkWarning(rrsRegister, "nl.mpi.rrsRegister");
        this.rrsRegister = rrsRegister;
    }

    /**
     *
     * @param rrsIndexURL
     */
    @Value("${nl.mpi.rrsIndex}")
    public void setRrsIndexURL(String rrsIndexURL) {
        rrsIndexURL = processLinkProtocol(rrsIndexURL, ssl);
        checkWarning(rrsIndexURL, "nl.mpi.rrsIndex");
        this.rrsIndexUrl = rrsIndexURL;
    }

    /**
     *
     * @param rrsURL
     */
    @Value("${nl.mpi.rrsUrl}")
    public void setRrsURL(String rrsURL) {
        rrsURL = processLinkProtocol(rrsURL, ssl);
        checkWarning(rrsURL, "nl.mpi.rrsUrl");
        this.rrsURL = rrsURL;
    }

    /**
     *
     * @param annexURL
     */
    @Value("${nl.mpi.annexUrl}")
    public void setAnnexURL(String annexURL) {
        annexURL = processLinkProtocol(annexURL, ssl);
        checkWarning(annexURL, "nl.mpi.annexUrl");
        this.annexURL = annexURL;
    }

    /**
     *
     * @param mdSearchURL
     */
    @Value("${nl.mpi.imdiSearchUrl}")
    public void setMdSearchURL(String mdSearchURL) {
        mdSearchURL = processLinkProtocol(mdSearchURL, ssl);
        checkWarning(mdSearchURL, "nl.mpi.imdiSearchUrl");
        this.mdSearchURL = mdSearchURL;
    }

    /**
     * Only use for CMDI so can be null
     * <p>
     * @param yamsSearchURL
     */
    @Value("${nl.mpi.yamsSearchUrl}")
    public void setYamsSearchURL(String yamsSearchURL) {
        yamsSearchURL = processLinkProtocol(yamsSearchURL, ssl);
        checkWarning(yamsSearchURL, "nl.mpi.yamsSearchUrl");
        this.yamsSearchURL = yamsSearchURL;
    }

    /**
     *
     * @param trovaURL
     */
    @Value("${nl.mpi.trovaUrl}")
    public void setTrovaURL(String trovaURL) {
        trovaURL = processLinkProtocol(trovaURL, ssl);
        checkWarning(trovaURL, "nl.mpi.trovaUrl");
        this.trovaURL = trovaURL;
    }

    /**
     *
     * @param manualURL
     */
    @Value("${nl.mpi.imdiBrowser.imdiBrowserManualUrl}")
    public void setManualURL(String manualURL) {
        manualURL = processLinkProtocol(manualURL, ssl);
        checkWarning(manualURL, "nl.mpi.manualUrl");
        this.manualURL = manualURL;
    }

    /**
     *
     * @param forceHttpOrHttps can be "Http" or "Https"
     */
    @Value("${nl.mpi.imdiBrowser.forceHttpOrHttps}")
    public void setForceHttpOrHttps(String forceHttpOrHttps) {
        forceHttpOrHttps = processLinkProtocol(forceHttpOrHttps, ssl);
        checkWarning(forceHttpOrHttps, "nl.mpi.forceHttpsOrHttps");
        this.forceHttpOrHttps = forceHttpOrHttps;
    }

    /**
     *
     * @param forceHttpsPrefix
     */
    @Value("${nl.mpi.imdiBrowser.forceHttpsPrefix}")
    public void setForceHttpsPrefix(String forceHttpsPrefix) {
        forceHttpsPrefix = processLinkProtocol(forceHttpsPrefix, ssl);
        checkWarning(forceHttpsPrefix, "nl.mpi.forHttpsPrefix");
        this.forceHttpsPrefix = forceHttpsPrefix;
    }

    /**
     * Method that will convert url from http to Https or otherwise depending on
     * current url and wished secure connection level setup in context.xml.
     * <p>
     * @param url, url as string to be check for secure connection
     * @param isHttps, boolean that give information whether url is already
     * secure or not
     * @return String url transformed depending on forceHttporHttps value
     */
    public String processLinkProtocol(final String url, final boolean isHttps) {
        if (url == null) {
            logger.warn("Null URL encountered in Configuration.processLinkProtocol");
            return null;
        }
        if (url.startsWith("http://localhost/") || url.startsWith("http://localhost:")
                || url.startsWith("http://127.0.0.1/") || url.startsWith("http://127.0.0.1:")) {
            // the AMS refresh trigger URL tends to be a localhost http one
            return url; // real SSL certificates are never valid for "localhost"
        }
        if (forceHttpsPrefix != null && forceHttpsPrefix.trim().length() > 0
                && url.startsWith(forceHttpsPrefix)) {
            return url.replace("http:", "https:");
        }
        if (forceHttpOrHttps == null) {
            return url;
        } else if ("http".equalsIgnoreCase(forceHttpOrHttps)
                || ("same".equalsIgnoreCase(forceHttpOrHttps) && !isHttps)) {
            return url.replace("https:", "http:");
        } else if ("https".equalsIgnoreCase(forceHttpOrHttps)
                || ("same".equalsIgnoreCase(forceHttpOrHttps) && isHttps)) {
            return url.replace("http:", "https:");
        }
        return url;
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
            logger.info(name + " was initialized with value: " + parameter);
        } else {
            logger.error("Required parameter " + name + " was not initialized properly");
        }
    }
}
