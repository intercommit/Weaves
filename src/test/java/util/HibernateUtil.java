package util;

import java.io.File;
import java.net.URISyntaxException;

import junit.framework.Assert;

import nl.intercommit.tapestry.test.entities.Person;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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
