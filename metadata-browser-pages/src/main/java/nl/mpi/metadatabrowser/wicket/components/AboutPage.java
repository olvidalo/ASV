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

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */

public final class AboutPage extends WebPage {
    public AboutPage(PageParameters parameters) {
        try {
            Properties versionProps = new Properties();
            versionProps.load(this.getClass().getResource("/metadata_browser.properties").openStream());

            add(new Label("version", versionProps.getProperty("metadata_browser.version")));
                add(new Label("build", versionProps.getProperty("metadata_browser.build")));
                add(new Label("date", versionProps.getProperty("metadata_browser.date")));
                add(new Label("year", versionProps.getProperty("metadata_browser.year")));
                add(new Label("appname", "Metadata-Browser"));
                add(new Label("title", "About Metadata Browser"));
        } catch (IOException ex) {
            Logger.getLogger(AboutPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }
