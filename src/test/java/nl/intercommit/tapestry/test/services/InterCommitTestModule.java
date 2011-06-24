package nl.intercommit.tapestry.test.services;

import java.io.File;

import nl.intercommit.weaves.hibernate.SessionFactoryConfiguration;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.services.LibraryMapping;

public class InterCommitTestModule {

	public static void contributeComponentClassResolver(
			Configuration configuration) {
		configuration.add(new LibraryMapping("ic-t5", "nl.intercommit.tapestry"));
	}

	public void contributeSessionFactorySource(MappedConfiguration<String,SessionFactoryConfiguration> configuration) {
		try {
			final File configFile = new File(this.getClass().getResource("/factory1.cfg.xml").toURI());
			final SessionFactoryConfiguration factory1Config = new SessionFactoryConfiguration(new String[] {"nl.intercommit.tapestry.test.entities"},configFile);
			configuration.add("Factory1",factory1Config);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TapestryException("Could not initialize session factory due to: ["+e.getMessage()+"]",e);
		}
	 }
	
}
