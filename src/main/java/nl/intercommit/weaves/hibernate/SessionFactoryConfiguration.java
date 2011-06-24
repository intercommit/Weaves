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
