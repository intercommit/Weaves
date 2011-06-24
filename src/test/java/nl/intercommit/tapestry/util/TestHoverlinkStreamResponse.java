package nl.intercommit.tapestry.util;

import java.io.IOException;

import junit.framework.TestCase;

import nl.intercommit.weaves.util.HoverlinkStreamResponse;

import org.apache.commons.io.IOUtils;

public class TestHoverlinkStreamResponse extends TestCase {

	public void testContent() {
		HoverlinkStreamResponse hsr = new HoverlinkStreamResponse("Test title","<html><body>And some comment</body></html");
		
		try {
			final String output = new String(IOUtils.toByteArray(hsr.getStream()));
			
			assertEquals("<HelpBalloon><title>Test title</title><content>&#60;html&#62;&#60;body&#62;And some comment&#60;/body&#62;&#60;/html</content></HelpBalloon>",output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
	}
}
