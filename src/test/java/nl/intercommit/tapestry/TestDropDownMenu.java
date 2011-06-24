package nl.intercommit.tapestry;

import junit.framework.TestCase;

import org.apache.tapestry5.test.PageTester;

public class TestDropDownMenu extends TestCase {

	private PageTester tester;

	@Override
	protected void setUp() throws Exception {
		if (tester == null) {
			tester = new PageTester("nl.intercommit.tapestry.test", "InterCommitTest");
		}
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		if (tester != null) {
			tester.shutdown();
		}
	}
	
	public void testEmptyMenu() {
		org.apache.tapestry5.dom.Document dom = tester.renderPage("DropDownMenuPage");
		
		assertNull(dom.getElementById("t-error"));
		assertNotNull(dom.getElementById("root"));
		
		assertEquals("<ul id=\"root\" class=\"level1\"></ul>",dom.getElementById("root").toString().trim());
		
	}
	
	
	public void testFullMenu() {
	
		org.apache.tapestry5.dom.Document dom = tester.renderPage("DropDownMenuPage/full");
		
		assertNotNull(dom.getElementById("root"));
		
		assertEquals("<ul id=\"root\" class=\"level1\"><li id=\"none\"><a href=\"/foo/pagedgridpage\"><div style=\"display:inline; margin-right: 5px;\"><img src=\"/images/menu1.png\"/></div>top1</a><ul class=\"level2\"><li><a href=\"/foo/dropdownmenupage\">item1</a></li><li><a href=\"/foo/dropdownmenupage\">item2</a></li></ul></li><li id=\"none\"><a href=\"/foo/switchpage\">top2</a><ul class=\"level2\"><li><a href=\"/foo/dropdownmenupage\">item1</a></li><li><a href=\"/foo/dropdownmenupage\">item2<div class=\"morelevels\"> &gt; </div></a><ul class=\"level3\"><li><a href=\"/foo/dropdownmenupage\">sub1</a></li><li><a href=\"/foo/dropdownmenupage\">sub2</a></li><li><a href=\"/foo/dropdownmenupage\">sub3</a></li></ul></li></ul></li></ul>",dom.getElementById("root").toString());
		
	}
	
}
