package nl.intercommit.tapestry;

import junit.framework.TestCase;

import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.test.PageTester;

public class TestSwitch extends TestCase {

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

	
	public void testSwitchComponent() {
	
		org.apache.tapestry5.dom.Document dom = tester.renderPage("SwitchPage/one");
		assertNotNull(dom.getElementById("switch"));
		Element el = dom.getElementById("switch");
		assertEquals("one!",el.getChildMarkup().trim());

		
		dom = tester.renderPage("SwitchPage/unknown");
		assertNotNull(dom.getElementById("switch"));
		el = dom.getElementById("switch");
		assertEquals("<h1>default</h1>",el.getChildMarkup().trim());
		
		
		dom = tester.renderPage("SwitchPage/someother");
		assertNotNull(dom.getElementById("switch"));
		el = dom.getElementById("switch");
		assertEquals("someother...",el.getChildMarkup().trim());
		
		dom = tester.renderPage("SwitchPage");
		assertNotNull(dom.getElementById("switch"));
		el = dom.getElementById("switch");
		assertEquals("<h1>default</h1>",el.getChildMarkup().trim());
	}

	
	
	
}
