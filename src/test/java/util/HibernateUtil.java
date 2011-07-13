/*  Copyright 2011 InterCommIT b.v.
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
package util;

import java.io.File;
import java.net.URISyntaxException;

import junit.framework.Assert;
import nl.intercommit.weaves.test.entities.Person;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private static Class[] entities = new Class[] {Person.class};
	
	private Configuration config;
    private static SessionFactory factory;
    
    public HibernateUtil() {
    	
    	if (factory == null) {
    	
	    	System.err.println("INSTANTIATING HIBERNATE UTIL!");
	    	
	    	File file = null;
	    	try {
	    		file = new File(this.getClass().getResource("/factory1.cfg.xml").toURI().getPath());
	    		Assert.assertNotNull(file);
	    	} catch (URISyntaxException use) {
	    		throw new HibernateException("Could not load config file!");
	    	}
	    	
	    	final AnnotationConfiguration config = new AnnotationConfiguration().configure(file);
			for (Class entity: HibernateUtil.entities ) {
				config.addAnnotatedClass(entity);
				
			}
			this.config = config;
	    	this.factory = config.buildSessionFactory();
    	}
	}

	public Session create() {
		return factory.openSession();
	}

	public Configuration getConfiguration() {
		return config;
	}

	public SessionFactory getSessionFactory() {
		return factory;
	}
	
	public void shutdown() {
		factory.close();
	}
}
