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

public class TestTextMarker extends WeaveTestCase {

	
	public void testTextMarkerComponent() {
	
		org.apache.tapestry5.dom.Document dom = getTester().renderPage("TextMarkerPage");
		assertNotNull(dom.getElementById("result"));
		assertEquals("<div class=\"textmarker\">Lorem <div class=\"highlight\">ipsum</div> dolor sit amet...<div class=\"highlight\">Ut</div> labore et dolo...<div class=\"highlight\">Ut</div> enim ad minim ...<div class=\"highlight\">exercitation</div> ullamco labori...<div class=\"highlight\">ut</div> aliquip ex ea ...<div class=\"highlight\">ut</div>e irure dolor i...</div>",dom.getElementById("result").getChildMarkup());
	}

	public void testTextMarkerComponentMaxLength() {
		
		org.apache.tapestry5.dom.Document dom = getTester().renderPage("TextMarkerPage/30");
		assertNotNull(dom.getElementById("result"));
		assertEquals("<div class=\"textmarker\">Lorem <div class=\"highlight\">ipsum</div> dolor sit amet...<div class=\"highlight\">Ut</div></div>",dom.getElementById("result").getChildMarkup());
	}
	
	
}
