package nl.intercommit.tapestry.test.pages;

import nl.intercommit.weaves.components.HoverLink;
import nl.intercommit.weaves.util.HoverlinkStreamResponse;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.OnEvent;

public class HoverLinkPage {

	@OnEvent(component="hoverlink",value=HoverLink.RETRIEVE_CONTENT_EVENT)
	private StreamResponse returnContent1() {
		return new HoverlinkStreamResponse("Popup Title","Popup test content");
	}
}
