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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.services.Response;

public class HoverlinkStreamResponse implements StreamResponse {

	private StringBuilder build;
	
	private final String title;
	private final String content;
	
	public HoverlinkStreamResponse(final String title, final String content) {
		this.title = title;
		this.content = content;
		build = new StringBuilder(1024);	
	}
	
	@Override
	public String getContentType() {
		return "text/xml";
	}
	
	@Override
	public InputStream getStream() throws IOException {
		build.append("<HelpBalloon>");
		build.append("<title>").append(title);
		build.append("</title>");
		build.append("<content>").append(encodeHTML(content));
		build.append("</content>");
		build.append("</HelpBalloon>");
		
		return new ByteArrayInputStream(build.toString().getBytes("UTF-8"));  
	}

	@Override
	public void prepareResponse(Response response) {
		// TODO Auto-generated method stub
		
	}
	
	private String encodeHTML(String s)
	{
	    StringBuffer out = new StringBuffer();
	    for(int i=0; i<s.length(); i++) {
	        char c = s.charAt(i);
	        if(c > 127 || c=='"' || c=='<' || c=='>') {
	           out.append("&#"+(int)c+";");
	        } else {
	            out.append(c);
	        }
	    }
	    return out.toString();
	}
}

