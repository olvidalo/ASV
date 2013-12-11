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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

@PropertySource(value = "classpath:config/production/defaultServiceLocations.properties")
/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
@Component
public class NodeActionsConfiguration implements Serializable {
    // otherwise the properties don't get automatically injected with the Value annotations
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    private String amsURL;
    private String rrsURL;
    private String rrsIndexUrl;
    private String annexURL;
    private String mdSearchURL;
    private String trovaURL;
    private String manualURL;
    private String rrsRegister;

    public String getTrovaURL() {
        return trovaURL;
    }

    public String getAmsURL() {
        return amsURL;
    }

    public String getRrsRegister(){
        return rrsRegister;
    }
    
    public String getRrsIndexURL(){
        return rrsIndexUrl;
    }
    
    public String getRrsURL() {
        return rrsURL;
    }

    public String getAnnexURL() {
        return annexURL;
    }
    
    public String getManualURL(){
        return manualURL;
    }

    /**
     *
     * @return
     */
    public String getMdSearchURL() {
        return mdSearchURL;
    }
    @Value("${nl.mpi.amsUrl}")
    public void setAmsURL(String amsURL) {
        this.amsURL = amsURL;
    }

    @Value("${nl.mpi.rrsRegister}")
    public void setRrsRegister(String rrsRegister){
       this.rrsRegister = rrsRegister; 
    }

    
    @Value("${nl.mpi.rrsIndex}")
    public void setRrsIndexURL(String rrsIndexURL){
       this.rrsIndexUrl = rrsIndexURL; 
    }
    
    @Value("${nl.mpi.rrsUrl}")
    public void setRrsURL(String rrsURL) {
        this.rrsURL = rrsURL;
    }

    @Value("${nl.mpi.annexUrl}")
    public void setAnnexURL(String annexURL) {
        this.annexURL = annexURL;
    }

    @Value("${nl.mpi.imdiSearchUrl}")
    public void setMdSearchURL(String mdSearchURL) {
        this.mdSearchURL = mdSearchURL;
    }

    @Value("${nl.mpi.trovaUrl}")
    public void setTrovaURL(String trovaURL) {
        this.trovaURL = trovaURL;
    }

    @Value("${nl.mpi.imdiBrowser.imdiBrowserManualUrl}")
    public void setManualURL(String manualURL) {
        this.manualURL = manualURL;
    }
}
