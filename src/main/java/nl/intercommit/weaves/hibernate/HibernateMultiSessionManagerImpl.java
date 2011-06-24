package nl.intercommit.weaves.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.ioc.services.ThreadCleanupListener;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateMultiSessionManagerImpl implements HibernateMultiSessionManager,
		ThreadCleanupListener {

	private final SessionFactorySource sessionFactorySource;
	private Map<String,Session> sessions;
	
	
	public HibernateMultiSessionManagerImpl(final SessionFactorySource sessionFactorySource) {
		this.sessionFactorySource = sessionFactorySource;
		this.sessions = new HashMap<String, Session>();
	}
	
	public Session getSession(Class<?> entityClass) {
		return getSession(sessionFactorySource.getFactoryID(entityClass));
	}

	public Session getSession(String factoryID) {
		Session session = sessions.get(factoryID);
		if (session == null) {
			session = createSession(factoryID);
		}
		return session;
	}

	public void threadDidCleanup() {
		try {
		
			for (Session session : sessions.values()) {
				try {
					Transaction tx  = session.getTransaction();
					if (tx.isActive()) {
						tx.rollback();
					}
					tx = null;
				} catch (Exception e) {
					System.err.println("[HibernateSessionManagerImpl] could not rollback transaction!");
				}
				if (session.isOpen()) {
					session.close();
				}
				session = null;
			}
			sessions = new HashMap<String, Session>();
			
		} catch(HibernateException he) {
			System.err.println("[HibernateSessionManagerImpl] hibernate exception: " + he);
		}
		
	}

	private Session createSession(String factoryID) {
		final Session session = sessionFactorySource.createSession(factoryID);
		// start a TX on this session. just like Tapestry does
		session.beginTransaction();
		sessions.put(factoryID, session);
		return session;
	}
	
}
