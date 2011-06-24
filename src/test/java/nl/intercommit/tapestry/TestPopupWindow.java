package nl.intercommit.tapestry;

import junit.framework.TestCase;

import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.test.PageTester;

public class TestPopupWindow extends TestCase {

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

	
	public void testPopupWindow() {
	
		org.apache.tapestry5.dom.Document dom = tester.renderPage("PopupWindowPage");
		Element el = dom.find("html/body/h1");
		if (el != null) {
			if (el.getAttribute("class").equals("t-exception-report")) {
				fail("Tapestry Exception:" + dom);
			}
		}
		
		assertNotNull(dom.getElementById("pw_popupWindowContent"));
	}

	
	
	
}
