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

public class TestDropDownMenu extends WeaveTestCase {
	
	public void testEmptyMenu() {
		org.apache.tapestry5.dom.Document dom = getTester().renderPage("DropDownMenuPage");
		
		assertNull(dom.getElementById("t-error"));
		assertNotNull(dom.getElementById("root"));
		
		assertEquals("<ul id=\"root\" class=\"level1\"></ul>",dom.getElementById("root").toString().trim());
		
	}
	
	
	public void testFullMenu() {
	
		org.apache.tapestry5.dom.Document dom = getTester().renderPage("DropDownMenuPage/full");
		
		assertNotNull(dom.getElementById("root"));
		
		assertEquals("<ul id=\"root\" class=\"level1\"><li id=\"none\"><a href=\"/foo/pagedgridpage\"><div style=\"display:inline; margin-right: 5px;\"><img src=\"/images/menu1.png\"/></div>top1</a><ul class=\"level2\"><li><a href=\"/foo/dropdownmenupage\">item1</a></li><li><a href=\"/foo/dropdownmenupage\">item2</a></li></ul></li><li id=\"toplevelselected\"><a href=\"/foo/switchpage\">top2</a><ul class=\"level2\"><li><a href=\"/foo/dropdownmenupage\">item1</a></li><li><a href=\"/foo/dropdownmenupage\">item2<div class=\"morelevels\"> &gt; </div></a><ul class=\"level3\"><li><a href=\"/foo/dropdownmenupage\">sub1</a></li><li><a href=\"/foo/dropdownmenupage\">sub2</a></li><li><a href=\"/foo/dropdownmenupage\">sub3</a></li></ul></li></ul></li></ul>",dom.getElementById("root").toString());
		
	}
	
}
