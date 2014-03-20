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

import java.io.File;
import java.util.Arrays;

import org.hibernate.cfg.Configuration;

public class SessionFactoryConfiguration {
	
	private final String[] packageNames;
	private final Class<?>[] annotatedClasses;
	private final File configFile;

	public SessionFactoryConfiguration(final String[] packageNames,final File configFile) {
		this(packageNames,null,configFile);
	}
	
	public SessionFactoryConfiguration(final String[] packageNames,final Class<?>[] annotatedClasses, final File configFile) {
		this.configFile = configFile;
		//http://pmd.sourceforge.net/rules/sunsecure.html
		this.packageNames = Arrays.copyOf(packageNames, packageNames.length);
		if (annotatedClasses != null) {
			this.annotatedClasses = Arrays.copyOf(annotatedClasses,annotatedClasses.length);
		} else {
			this.annotatedClasses = null;
		}
	}

	public String[] getPackageNames() {
		return packageNames;
	}
	
	public Class<?>[] getAnnotatedClasses() {
		return annotatedClasses;
	}
	
	public final void configure(final Configuration configuration) {
		configuration.configure(configFile);
	}
}
