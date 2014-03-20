/*  Copyright 2014 InterCommIT b.v.
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
package nl.intercommit.weaves.components;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import nl.intercommit.weaves.growler.Message;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Growler-like user feedback based on: https://github.com/prtksxna/growl-prototype/
 * 
 * TODO: Support for zone updates.
 * 
 * @tapestrydoc
 */
@Import(stylesheet={"growler/Growler.css"},library={"growler/Growler.js"})
public class Growler {

	@SessionState(create=false)
	private List<Message> _msgs;
	
	@Inject
	private JavaScriptSupport js;
	
	@Parameter(defaultPrefix = BindingConstants.ASSET, value = "growler/error.png")
	private Asset errorimage;
    
    @Parameter(defaultPrefix = BindingConstants.ASSET, value = "growler/info.png")
	private Asset infoimage;
    
    @Parameter(defaultPrefix = BindingConstants.ASSET, value = "growler/warn.png")
	private Asset warnimage;
    
	@AfterRender
    private void displayUserMessages(final MarkupWriter writer) {
		js.addScript("var growl = new k.Growler();", "");
		String imageUrl = "";
		if (_msgs !=null) {
			for (Message msg: _msgs) {
				switch(msg.getLevel()) {
					case ERROR:{ 
						imageUrl = errorimage.toClientURL();
						js.addScript("growl."+msg.getLevel().name().toLowerCase()+"('"+StringEscapeUtils.escapeJavaScript(URLDecoder.decode(msg.getMessage()))+"',{ className: 'atwork', image: '"+imageUrl+"', sticky: true});", "");
						break;
					}
					case INFO: {
						imageUrl = infoimage.toClientURL(); 
						js.addScript("growl."+msg.getLevel().name().toLowerCase()+"('"+StringEscapeUtils.escapeJavaScript(URLDecoder.decode(msg.getMessage()))+"',{ className: 'atwork', image: '"+imageUrl+"', sticky: true,life: 3});", "");
						break;
					}
					case WARN: {
						imageUrl = warnimage.toClientURL(); 
						js.addScript("growl."+msg.getLevel().name().toLowerCase()+"('"+StringEscapeUtils.escapeJavaScript(URLDecoder.decode(msg.getMessage()))+"',{ className: 'atwork', image: '"+imageUrl+"', sticky: true,life: 5});", "");
						break;
					}
				}
			}
			_msgs.clear();
		}
    }
	
	public void addMessage(final Message msg) {
		if (_msgs == null) {
			_msgs = new ArrayList<Message>();
		}
		_msgs.add(msg);
	}
	
}
