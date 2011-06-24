package nl.intercommit.tapestry;

import junit.framework.TestCase;

import org.apache.tapestry5.test.PageTester;

public class TestTextMarker extends TestCase {

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

	
	public void testTextMarkerComponent() {
	
		org.apache.tapestry5.dom.Document dom = tester.renderPage("TextMarkerPage");
		assertNotNull(dom.getElementById("result"));
		assertEquals("<div class=\"textmarker\">Lorem <div class=\"highlight\">ipsum</div> dolor sit amet...<div class=\"highlight\">Ut</div> labore et dolo...<div class=\"highlight\">Ut</div> enim ad minim ...<div class=\"highlight\">exercitation</div> ullamco labori...<div class=\"highlight\">ut</div> aliquip ex ea ...<div class=\"highlight\">ut</div>e irure dolor i...</div>",dom.getElementById("result").getChildMarkup());
	}

	public void testTextMarkerComponentMaxLength() {
		
		org.apache.tapestry5.dom.Document dom = tester.renderPage("TextMarkerPage/30");
		assertNotNull(dom.getElementById("result"));
		assertEquals("<div class=\"textmarker\">Lorem <div class=\"highlight\">ipsum</div> dolor sit amet...<div class=\"highlight\">Ut</div></div>",dom.getElementById("result").getChildMarkup());
	}
	
	
}
