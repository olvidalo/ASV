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

import nl.mpi.archiving.tree.corpusstructure.CorpusStructureDBFactory;
import nl.mpi.metadatabrowser.model.cmdi.CmdiCorpusStructureDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class CmdiCorpusStructureDBFactoryImpl implements CorpusStructureDBFactory {

    private final static Logger logger = LoggerFactory.getLogger(CmdiCorpusStructureDBFactoryImpl.class);
    private final String dbname;

    public CmdiCorpusStructureDBFactoryImpl(String dbname) {
	logger.info("Constructed with dbname='{}'", dbname);
	this.dbname = dbname;
    }

    @Override
    public CmdiCorpusStructureDB createCorpusStructureDB() {
	logger.debug("Creating new CmdiCorpusStructureDB with dbname='{}'", dbname);
	throw new UnsupportedOperationException("Not supported yet");
	//TODO: Implement CmdiCorpusStructureDB and return an instance here
	//return new CmdiCorpusStructureDBImpl(dbname); 
    }
}
