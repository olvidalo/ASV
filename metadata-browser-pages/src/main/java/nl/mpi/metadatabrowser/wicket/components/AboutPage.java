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
package nl.mpi.metadatabrowser.wicket.components;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */

@PropertySource(value = "classpath:config/production/metadata_browser.properties")

public final class AboutPage extends WebPage {
    
    // otherwise the properties don't get automatically injected with the Value annotations
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    /**
     *
     * @param parameters
     */
    @Value("${version}")
    private String version;
    
    @Value("${build}")
    private String build;
    
    @Value("${date}")
    private String date;
    
    @Value("${year}")
    private String year;
            
    public AboutPage(PageParameters parameters) {
        add(new Label("version", version));
        add(new Label("build", build));
        add(new Label("date", date));
        add(new Label("year", year));
        add(new Label("appname", "Metadata-Browser"));
        add(new Label("title", "About Metadata Browser"));
    }
}
