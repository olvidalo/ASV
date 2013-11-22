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
package nl.mpi.metadatabrowser.services.cmdi.provider;

import java.io.Serializable;
import javax.persistence.EntityManagerFactory;
import nl.mpi.archiving.corpusstructure.core.database.dao.impl.DaoFactoryImpl;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProviderFactory;
import nl.mpi.archiving.corpusstructure.provider.db.CorpusStructureProviderImpl;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class ProductionCorpusStructureProviderFactory implements CorpusStructureProviderFactory, Serializable {

//    @PersistenceContext
//    private EntityManager em;
    @Autowired
    @SpringBean
    private transient EntityManagerFactory emf;

    @Override
    public CorpusStructureProvider createCorpusStructureDB() {
	if (emf == null) {
	    Injector.get().inject(this);
	}
	//final EntityManagerFactory emf = applicationContext.getBean(EntityManagerFactory.class);
	return new CorpusStructureProviderImpl(new DaoFactoryImpl(emf));
    }
}
