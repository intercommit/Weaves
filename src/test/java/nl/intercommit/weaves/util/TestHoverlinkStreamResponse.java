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
package nl.intercommit.weaves.util;

import java.io.IOException;

import junit.framework.TestCase;

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
