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
package nl.intercommit.weaves;

import util.WeaveTestCase;

public class TestConfirm extends WeaveTestCase {

	
	public void testConfirmationPopup() {
		org.apache.tapestry5.dom.Document dom = getTester().renderPage("ConfirmPage");
		assertNotNull(dom);
		assertNull(dom.getElementById("t-error"));
		assertFalse(dom.toString().contains("<h1 class=\"t-exception-report\">"));
		
		System.err.println(dom);
		
		assertNotNull(dom.getElementById("actionlink"));
		assertTrue(dom.toString().contains("new Confirm('actionlink', 'Are you sure?')"));
	}
	
	
}
