package nl.intercommit.weaves.hibernate;

import org.apache.tapestry5.ioc.annotations.UsesMappedConfiguration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@UsesMappedConfiguration(key=String.class,value=SessionFactoryConfiguration.class)
public interface SessionFactorySource {
	   public SessionFactory getSessionFactory(String factoryID);

	   public SessionFactory getSessionFactory(Class<?> entityClass);

	   public Session createSession(String factoryID);

	   public String getFactoryID(Class<?> entityClass);
}
