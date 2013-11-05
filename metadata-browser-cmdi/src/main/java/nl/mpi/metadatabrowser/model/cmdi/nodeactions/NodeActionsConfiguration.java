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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletContextPropertyPlaceholderConfigurer;
@PropertySource(value="classpath:config/production/defaultServiceLocations.properties")
/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
@Component
public class NodeActionsConfiguration {
    // otherwise the properties don't get automatically injected with the Value annotations
//    @Bean
//    public static ServletContextPropertyPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
//        return new ServletContextPropertyPlaceholderConfigurer();
//    }
    
    // otherwise the properties don't get automatically injected with the Value annotations
	    @Bean
	    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
	        return new PropertySourcesPlaceholderConfigurer();
	    }
    
    public NodeActionsConfiguration(){
    
    }
    @Value("${amsURL}")
    private String amsURL;
    @Value("${rrsURL}")
    private String rrsURL;
    @Value("${annexURL}")
    private String annexURL;
    @Value("${mdSearchURL}")
    private String mdSearchURL;
    @Value("${trova_url}")
    private String trovaURL;    

    
    public String getTrovaURL(){
        return trovaURL;
    }
    public String getAmsURL(){
        return amsURL;
    }
    
    public String getRrsURL(){
        return rrsURL;
    }
    
    public String getAnnexURL(){
        return annexURL;
    }
    
    /**
     *
     * @return
     */
    public String getMdSearchURL(){
        return mdSearchURL;
    }
}
