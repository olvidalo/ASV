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
package nl.mpi.metadatabrowser.wicket;

import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;

/**
 * Abstract base class for tests that require dependency injection of (mock) objects and services. Based on blog post by Petri Kainulainen
 * found at {@link http://www.petrikainulainen.net/programming/tips-and-tricks/mocking-spring-beans-with-apache-wicket-and-mockito/}
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public abstract class AbstractWicketTest {

    private ApplicationContextMock applicationContextMock;
    private WicketTester tester = null;

    @Before
    public final void setUpAbstractWicketTest() throws Exception {
	applicationContextMock = new ApplicationContextMock();
	tester = new WicketTester();
	// register spring injector with mock application context
	tester.getApplication().getComponentInstantiationListeners().add(new SpringComponentInjector(tester.getApplication(), applicationContextMock));
	setUp();
    }

    public abstract void setUp() throws Exception;

    public final void putBean(String name, Object bean) {
	getApplicationContextMock().putBean(name, bean);
    }

    public final ApplicationContextMock getApplicationContextMock() {
	return applicationContextMock;
    }

    public final WicketTester getTester() {
	return tester;
    }
}
