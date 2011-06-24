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
package nl.intercommit.weaves.components;

import java.io.InputStream;
import java.util.Properties;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Events(value=HoverLink.RETRIEVE_CONTENT_EVENT)
@Import(library = "hoverlink/Hoverlink.js",stylesheet="hoverlink/HoverLink.css")
public class HoverLink implements ClientElement {
	
	public static final String RETRIEVE_CONTENT_EVENT = "retrieveContent";

	@Parameter(value="250")
	private Integer width;
	
	@Parameter(value="200")
	private Integer height;
	
	@Parameter(defaultPrefix = BindingConstants.ASSET, value = "hoverlink/hover_close.jpg")
    private Asset closeButton;
    
	@Parameter(defaultPrefix = BindingConstants.ASSET, value="hoverlink/hoverlink_css.properties") 
	private Asset ballooncss;
	
	@Parameter(allowNull=true)
	private Object context;
	
	
    @Inject
    private ComponentResources resources;
    
    @Inject
    private JavaScriptSupport jsSupport;

    private JSONObject balloonStyle;
    
    private String clientId;
    
    @SetupRender
    void createBalloonStyle() {
    	Properties props = new Properties();
    	balloonStyle = new JSONObject();
    	InputStream io = null;
    	try {
    		io = ballooncss.getResource().openStream();
    		props.load(io);
    	
    		for (Object key: props.keySet()) {
    			balloonStyle.put((String)key,new JSONLiteral((String)props.get(key)));
    		}
    	} catch (Exception e) {
    		throw new TapestryException("Could not read " + ballooncss,this,e);
    	} finally {
    		if (io != null) {
    			try {
    				io.close();
    			} catch(Exception e) {}
    		}
    	}
    	clientId = null;
    }
    
    void beginRender(MarkupWriter writer) {
        writer.element("div","style","display:inline;");
        writer.element("a", "href","#","id",getClientId(),"class","ic_t5-hoverlink","onclick","return false;");
    }

    void afterRender(MarkupWriter writer) {
        writer.end();  // </a>
        writer.writeRaw("<script type=\"text/javascript\">var hb_"+getClientId()+" = new HelpBalloon("+getBalloonOptions().toCompactString() + ");");
        writer.writeRaw(" hb_"+getClientId()+".balloonDimensions = " + getBalloonDimensions().toCompactString() +";");
        writer.writeRaw("</script>");
        writer.end(); // </div>
    }
    
    private JSONObject getBalloonOptions() {
    	final JSONObject params = new JSONObject();
    	if (context != null) {
    		params.put("dataURL",resources.createEventLink("retrieveContent",context).toAbsoluteURI());
    	} else {
    		params.put("dataURL",resources.createEventLink("retrieveContent").toAbsoluteURI());
    	}
    	params.put("cacheRemoteContent", false);
    	params.put("icon",new JSONLiteral("$('"+getClientId()+"')"));
    	params.put("button", closeButton.toClientURL());
    	params.put("balloonStyle", balloonStyle);
    	params.put("contentMargin", 5);
    	params.put("autoHideTimeout",2000); // autohide in ms
    	params.put("useEvent",new JSONArray().put("mouseover"));
    	return params;
    }
    
    private JSONArray getBalloonDimensions() {
    	final JSONArray dimensions = new JSONArray();
    	dimensions.put(width);
    	dimensions.put(height);
    	return dimensions;
    }
    
    @Override
    public String getClientId() {
    	if (clientId == null) {
    		clientId = jsSupport.allocateClientId(resources);
    	}
    	return clientId;
    }
}
