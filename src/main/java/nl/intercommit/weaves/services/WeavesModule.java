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
package nl.intercommit.weaves.services;

import nl.intercommit.weaves.hibernate.HibernateMultiSessionManager;
import nl.intercommit.weaves.hibernate.HibernateMultiSessionManagerImpl;
import nl.intercommit.weaves.hibernate.SessionFactorySource;
import nl.intercommit.weaves.hibernate.SessionFactorySourceImpl;
import nl.intercommit.weaves.services.internal.JQueryJavaScriptStack;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.javascript.JavaScriptStack;

public class WeavesModule {
	
    public static void contributeComponentClassResolver(
             Configuration<LibraryMapping> configuration){
        configuration.add(new LibraryMapping("weaves", "nl.intercommit.weaves"));
    }
    
   public static void bind(ServiceBinder binder) {
      binder.bind(SessionFactorySource.class, SessionFactorySourceImpl.class);
   }
   
   public static void contributeJavaScriptStackSource(MappedConfiguration<String, JavaScriptStack>
  	configuration) {
        configuration.addInstance("jquery", JQueryJavaScriptStack.class);
  } 

   
   @Scope(ScopeConstants.PERTHREAD)
   public static HibernateMultiSessionManager buildHibernateMultiSessionManager(
		   SessionFactorySource sessionFactorySource,
		   PerthreadManager perthreadManager)
   {
       HibernateMultiSessionManagerImpl service = new HibernateMultiSessionManagerImpl(sessionFactorySource);
       perthreadManager.addThreadCleanupListener(service);
       return service;
   }
}
