package nl.intercommit.tapestry;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.tapestry5.internal.test.TestableResponse;
import org.apache.tapestry5.test.PageTester;

public class TestEditableSelect extends TestCase {

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
	
	public void testConfirmationPopup() {
		org.apache.tapestry5.dom.Document dom = tester.renderPage("EditableSelectPage");
		assertNotNull(dom);
		assertNull(dom.getElementById("t-error"));
		assertFalse(dom.toString().contains("<h1 class=\"t-exception-report\">"));
		
		assertNotNull(dom.getElementById("editableselectbox"));
		
		assertEquals("off",dom.getElementById("editableselectbox").getAttribute("autocomplete"));
		
			Map<String, String > params = new HashMap<String, String>();
        params.put(dom.getElementById("editableselectbox").getAttribute("id"), "test");
		
		TestableResponse resp = tester.submitFormAndReturnResponse(dom.getElementById("form"),params);
		
		assertEquals(200,resp.getStatus());
		
	}
	
	
}
