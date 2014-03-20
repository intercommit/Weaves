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
