package nl.intercommit.weaves.hibernate;

import org.hibernate.Session;

public interface HibernateMultiSessionManager {

   public Session getSession(final String factoryId);
   
   public Session getSession(final Class<?> entityClass);
  
}

