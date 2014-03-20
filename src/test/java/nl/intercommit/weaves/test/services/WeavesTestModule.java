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
package nl.intercommit.weaves.test.services;

import java.io.File;

import nl.intercommit.weaves.hibernate.SessionFactoryConfiguration;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.services.LibraryMapping;

public class WeavesTestModule {

	public static void contributeComponentClassResolver(
			Configuration configuration) {
		configuration.add(new LibraryMapping("weave", "nl.intercommit.weaves"));
	}

	public void contributeSessionFactorySource(MappedConfiguration<String,SessionFactoryConfiguration> configuration) {
		try {
			final File configFile = new File(this.getClass().getResource("/factory1.cfg.xml").toURI());
			final SessionFactoryConfiguration factory1Config = new SessionFactoryConfiguration(new String[] {"nl.intercommit.weaves.test.entities"},configFile);
			configuration.add("Factory1",factory1Config);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new TapestryException("Could not initialize session factory due to: ["+e.getMessage()+"]",e);
		}
	 }
	
}
