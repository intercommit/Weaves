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
package nl.intercommit.weaves.services;

import nl.intercommit.weaves.SymbolConstants;
import nl.intercommit.weaves.hibernate.HibernateMultiSessionManager;
import nl.intercommit.weaves.hibernate.HibernateMultiSessionManagerImpl;
import nl.intercommit.weaves.hibernate.SessionFactorySource;
import nl.intercommit.weaves.hibernate.SessionFactorySourceImpl;
import nl.intercommit.weaves.services.internal.BootstrapScriptStack;
import nl.intercommit.weaves.services.internal.JQueryJavaScriptStack;

import org.apache.tapestry5.ComponentParameterConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.MarkupRenderer;
import org.apache.tapestry5.services.MarkupRendererFilter;
import org.apache.tapestry5.services.javascript.JavaScriptStack;

public class WeavesModule {

	public static void contributeComponentClassResolver(
			Configuration<LibraryMapping> configuration) {
		configuration
				.add(new LibraryMapping("weaves", "nl.intercommit.weaves"));
	}

	public static void bind(ServiceBinder binder) {
		binder.bind(SessionFactorySource.class, SessionFactorySourceImpl.class);
	    binder.bind(IBootstrapElementConfig.class,BootstrapElementConfigImpl.class);
	}

	public static void contributeJavaScriptStackSource(
			MappedConfiguration<String, JavaScriptStack> configuration,
			@Inject @Symbol(SymbolConstants.BOOTSTRAP_ENABLED) Boolean bootstrapped) {
		configuration.addInstance("jquery", JQueryJavaScriptStack.class);
		 if (bootstrapped) {
        	 configuration.addInstance("bootstrap", BootstrapScriptStack.class);
         }
	}

	// disable bootstrap per default.
	public static void contributeFactoryDefaults(
			MappedConfiguration<String, Object> configuration) {
		
		if (System.getProperty(SymbolConstants.BOOTSTRAP_ENABLED, "false")
				.equals("true")) {
			configuration.add(SymbolConstants.BOOTSTRAP_ENABLED, true);
			configuration.override(
					ComponentParameterConstants.GRID_TABLE_CSS_CLASS, "table");
		} else {
			configuration.add(SymbolConstants.BOOTSTRAP_ENABLED, false);
		}
	}

	@Scope(ScopeConstants.PERTHREAD)
	public static HibernateMultiSessionManager buildHibernateMultiSessionManager(
			SessionFactorySource sessionFactorySource,
			PerthreadManager perthreadManager) {
		HibernateMultiSessionManagerImpl service = new HibernateMultiSessionManagerImpl(
				sessionFactorySource);
		perthreadManager.addThreadCleanupListener(service);
		return service;
	}
	
	// disable the default stylesheet, we're on Bootstrap now
   @Contribute(MarkupRenderer.class)
   public static void deactiveDefaultCSS(OrderedConfiguration<MarkupRendererFilter> configuration,
		   @Inject @Symbol(SymbolConstants.BOOTSTRAP_ENABLED) Boolean bootstrapped) {
	   
	   if (bootstrapped) {
		   configuration.override("InjectDefaultStylesheet", null);
	   }
   }
   
   /* 
    * Configures the styling classes for different HTML elements
    * 
    */
   public static void contributeIBootstrapElementConfig(MappedConfiguration<String, Object> configuration) {
	   configuration.add("form", "form-horizontal");
   }
}
