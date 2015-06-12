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
package nl.mpi.metadatabrowser.wicket;

import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import nl.mpi.metadatabrowser.services.TemplatesStore;

/**
 * Locator interface to be used in (exceptional!) cases where dependency
 * injection cannot provide a solution, {@literal e.g.} in Wicket models where
 * services should not be serialised
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public interface MetadataBrowserServicesLocator {

    NodeResolver getNodeResolver();

    NodeTypeIdentifier getNodeTypeIdentifier();

    CorpusStructureProvider getCorpusStructureProvider();
    
    /**
     * 
     * @return store with transformation templates for visualisation
     */
    TemplatesStore getTemplatesProvider();
    
    /**
     * 
     * @return settings for the metadata browser web application
     */
    Settings getSettings();

    /**
     * Instance where a singleton instance of the service locator can be registered
     */
    static class Instance {

        private static MetadataBrowserServicesLocator locator;

        public static void set(MetadataBrowserServicesLocator instance) {
            locator = instance;
        }

        public static MetadataBrowserServicesLocator get() {
            return locator;
        }

    }
}
