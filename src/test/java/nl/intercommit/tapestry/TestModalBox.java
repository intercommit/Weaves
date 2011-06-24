package nl.intercommit.tapestry;

import junit.framework.TestCase;

import org.apache.tapestry5.test.PageTester;

public class TestModalBox extends TestCase {

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

	
	public void testModalBox() {
		org.apache.tapestry5.dom.Document dom = tester.renderPage("ModalBoxPage");
		
		System.err.println(dom);
		
		assertNotNull(dom);
		assertNull(dom.getElementById("t-error"));
		assertFalse(dom.toString().contains("<h1 class=\"t-exception-report\">"));
		
		assertTrue(dom.getElementById("modalbox")!=null);
		
	}
	
	
}
