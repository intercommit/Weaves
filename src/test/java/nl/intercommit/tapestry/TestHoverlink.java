package nl.intercommit.tapestry;

import junit.framework.TestCase;

import org.apache.tapestry5.test.PageTester;

public class TestHoverlink extends TestCase {

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

	
	public void testHoverLinkComponent() {
		org.apache.tapestry5.dom.Document dom = tester.renderPage("HoverlinkPage");
		
		assertNotNull(dom);
		assertNotNull(dom.getElementById("hoverlink"));
		
		assertTrue(dom.getElementById("hoverlink").toString().equals("<a onclick=\"return false;\" class=\"ic_t5-hoverlink\" id=\"hoverlink\" href=\"#\">consectetur</a>"));
		assertTrue(dom.toString().contains("\"dataURL\":\"http://localhost/foo/hoverlinkpage.hoverlink:retrievecontent\""));
		
	}
	
	
}
