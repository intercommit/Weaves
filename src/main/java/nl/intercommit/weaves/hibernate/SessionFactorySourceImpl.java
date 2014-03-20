/*  Copyright 2014 InterCommIT b.v.
*
*  This file is part of the "Weaves" project hosted on https://github.com/intercommit/Weaves
*
*  Weaves is free software: you can redistribute it and/or modify
*  it under the terms of the GNU Lesser General Public License as published by
*  the Free Software Foundation, either version 3 of the License, or
*  any later version.
*
*  Weaves is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with Weaves.  If not, see <http://www.gnu.org/licenses/>.
*
*/
package nl.intercommit.weaves.hibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.apache.tapestry5.ioc.services.RegistryShutdownListener;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class SessionFactorySourceImpl implements SessionFactorySource,
		RegistryShutdownListener {
	  
	private final Map<String, SessionFactory> symbolMap = new HashMap<String, SessionFactory>();
	private final Map<Class<?>, String> entityMap = new HashMap<Class<?>, String>();
	private final ClassNameLocator classNameLocator;

	public SessionFactorySourceImpl(final ClassNameLocator classNameLocator,
			final Map<String,SessionFactoryConfiguration> configurations) {
		this.classNameLocator = classNameLocator;
		
		if (configurations.isEmpty()) {
			throw new TapestryException("Contribute at least one SessionFactoryConfiguration to the SessionFactorySource.",this,null);
		}
			
		for (final String key: configurations.keySet()) {
			setupSessionFactory(key,configurations.get(key));
		}
	}

	private void setupSessionFactory(final String factoryIdentifier,final SessionFactoryConfiguration configuration) {
		
		final AnnotationConfiguration hibernateConfig = new AnnotationConfiguration();
		final List<Class<?>> entities = loadEntityClasses(configuration);
		
		// this loads the file containing the hibernate.cfg.xml specs
		configuration.configure(hibernateConfig);
		// i hope this is here alreaady..
		// add any additional classes
		if (configuration.getAnnotatedClasses() != null) {
			entities.addAll(Arrays.asList(configuration.getAnnotatedClasses()));	
		}
		// Load entity classes
		for (final Class<?> entityClass : entities) {
			hibernateConfig.addAnnotatedClass(entityClass);
			entityMap.put(entityClass, factoryIdentifier);
		}
		
		final SessionFactory sf = hibernateConfig.buildSessionFactory();
		
		if (symbolMap.containsKey(factoryIdentifier)) {
			throw new RuntimeException("Sessionfactory with name ["+factoryIdentifier+"]. Is already configured! Cannot continue configuration.");
		}
		symbolMap.put(factoryIdentifier, sf);
	}

	private List<Class<?>> loadEntityClasses(
			final SessionFactoryConfiguration configuration) {
		final ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();

		final List<Class<?>> entityClasses = new ArrayList<Class<?>>();

		for (final String packageName : configuration.getPackageNames()) {
			for (final String className : classNameLocator
					.locateClassNames(packageName)) {
				try {
					Class<?> entityClass = null;
					entityClass = classLoader.loadClass(className);
					if (entityClass.getAnnotation(javax.persistence.Entity.class) != null
							|| entityClass.getAnnotation(javax.persistence.MappedSuperclass.class) != null) {
						entityClasses.add(entityClass);
					}
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return entityClasses;
	}

	public SessionFactory getSessionFactory(final String factoryID) {
		SessionFactory sf = symbolMap.get(factoryID);
		if (sf == null) {
			throw new RuntimeException("No session factory found for factoryID: " + factoryID);
		}
		return sf;
	}

	public SessionFactory getSessionFactory(final Class<?> factoryID) {
		SessionFactory sf = getSessionFactory(entityMap.get(factoryID));
		if (sf == null) {
			throw new RuntimeException("No session factory found for entity: " + factoryID);
		}
		return sf;
	}

	public void registryDidShutdown() {
		for (final SessionFactory sessionFactory : symbolMap.values()) {
			sessionFactory.close();
		}
	}

	public Session createSession(Class<?> entityClass) {
		return createSession(getFactoryID(entityClass));
	}

	public Session createSession(String factoryID) {
		return getSessionFactory(factoryID).openSession();
	}

	public String getFactoryID(Class<?> entityClass) {
		return entityMap.get(entityClass);
	}
}
