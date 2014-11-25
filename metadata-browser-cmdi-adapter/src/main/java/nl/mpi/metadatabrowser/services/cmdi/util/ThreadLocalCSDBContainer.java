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
package nl.mpi.metadatabrowser.services.cmdi.util;

import java.io.Serializable;
import nl.mpi.archiving.corpusstructure.adapter.db.proxy.ArchiveObjectsDBFactory;
import nl.mpi.archiving.corpusstructure.adapter.db.proxy.CSDBContainer;
import nl.mpi.archiving.corpusstructure.adapter.db.proxy.CorpusStructureDBFactory;
import nl.mpi.corpusstructure.ArchiveObjectsDB;
import nl.mpi.corpusstructure.CorpusStructureDB;
import nl.mpi.versioning.manager.VersioningAPIImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * CDSB container that creates (on request) thread local CSDB instances, and
 * closes the connection when an instance is set to null
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class ThreadLocalCSDBContainer implements CSDBContainer, Serializable {

    private final static ThreadLocal<CorpusStructureDB> localCSDB = new ThreadLocal<>();
    private final static ThreadLocal<ArchiveObjectsDB> localAO = new ThreadLocal<>();

    private final CorpusStructureDBFactory csdbFactory;
    private final ArchiveObjectsDBFactory aoFactory;

    @Autowired
    public ThreadLocalCSDBContainer(CorpusStructureDBFactory csdbFactory, ArchiveObjectsDBFactory aoFactory) {
        this.csdbFactory = csdbFactory;
        this.aoFactory = aoFactory;
    }

    @Override
    public ArchiveObjectsDB getAo() {
        ArchiveObjectsDB ao = localAO.get();
        if (ao == null) {
            ao = aoFactory.newAO();
            localAO.set(ao);
        }
        return ao;
    }

    @Override
    public CorpusStructureDB getCsdb() {
        CorpusStructureDB csdb = localCSDB.get();
        if (csdb == null) {
            csdb = csdbFactory.newCSDB();
            localCSDB.set(csdb);
        }
        return csdb;
    }

    /**
     * Sets the AO instance. If null, closes the current instance if present
     *
     * @param ao null to close and remove
     */
    @Override
    public void setAo(ArchiveObjectsDB ao) {
        if (ao == null) {
            final ArchiveObjectsDB current = localAO.get();
            if (current != null) {
                current.close();
                localAO.remove();
            }
        } else {
            localAO.set(ao);
        }
    }

    /**
     *
     * Sets the CSDB instance. If null, closes the current instance if present
     *
     * @param csdb null to close and remove
     */
    @Override
    public void setCsdb(CorpusStructureDB csdb) {
        if (csdb == null) {
            final CorpusStructureDB current = localCSDB.get();
            if (current != null) {
                current.close();
                localCSDB.remove();
            }
        } else {
            localCSDB.set(csdb);
        }
    }

    /**
     *
     * @return @throws UnsupportedOperationException always
     */
    @Override
    public VersioningAPIImpl getVersioningDb() { // not used in metadata browser
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @return @throws UnsupportedOperationException always
     */
    @Override
    public void setVersioningDb(VersioningAPIImpl db) { // not used in metadata browser
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
