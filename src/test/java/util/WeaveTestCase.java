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
package util;

import junit.framework.TestCase;

import org.apache.tapestry5.test.PageTester;

public abstract class WeaveTestCase extends TestCase {

	private static PageTester tester;

	@Override
	protected void setUp() throws Exception {
		if (tester == null) {
			tester = new PageTester("nl.intercommit.weaves.test", "WeavesTest");
		}
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		/*
		if (tester != null) {
			tester.shutdown();
		}
		*/
	}
	
	public PageTester getTester() {
		if (tester == null) {
			fail("Tapestry PageTester not initialized yet!");
		}
		return tester;
	}
}
