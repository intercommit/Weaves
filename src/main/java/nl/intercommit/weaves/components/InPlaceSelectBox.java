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

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.util.TextStreamResponse;

/**
 * A "just in place" select/update component that doesnt have to be enclosed in a form
 *
 * Copied from http://js.antinoc.net/?q=node/23
 * 
 * @tapestrydoc
 * 
 */
@SupportsInformalParameters
@Import(library = {"classpath:${tapestry.scriptaculous.path}/controls.js","InPlaceSelectBox.js"})
public class InPlaceSelectBox implements ClientElement {

	public final static String SAVE_EVENT = "save";

    /**
     * The id used to generate a page-unique client-side identifier for the component. If a
     * component renders multiple times, a suffix will be appended to the to id to ensure
     * uniqueness. The uniqued value may be accessed via the
     * {@link #getClientId() clientId property}.
     */
    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
    private String clientId;

    /**
     * The value to be read and updated. This is not necessarily a string, a translator may be provided to convert
     * between client side and server side representations. If not bound, a default binding is made to a property of the
     * container matching the component's id. If no such property exists, then you will see a runtime exception due to
     * the unbound value parameter.
     */
    @Parameter(required = true, principal = true)
    private String value;

    /**
     * Size of the input text tag.
     */
    @Parameter(value = "20", required = false, defaultPrefix = BindingConstants.LITERAL)
    private String size;

    /**
     * Key, Value map with selectbox contents
     */
    @Parameter(required = true)
    private Map<Long, String> content;
    
    @Parameter(required = false,value="false")
    private Boolean blankOption;
    
    @Parameter(required= false,value="message:title")
    private String emptyMessageKey; 
    
    /**
     * The context for the link (optional parameter). This list of values will be converted into strings and included in
     * the URI.
     */
    @Parameter(required = false)
    private List<?> context;

    @Inject
    private ComponentResources resources;

    @Inject
    private Messages messages;

    @Environmental
    private JavaScriptSupport jsSupport;

    @Inject
    private Request request;

    private String assignedClientId;
   
    private Object[] contextArray;
    
    void setupRender() {
        assignedClientId = jsSupport.allocateClientId(clientId);
        contextArray = context == null ? new Object[0] : context.toArray();
    }

    void beginRender(MarkupWriter writer) {
        writer.element("span", "id", getClientId());

        if (value != null && value.length() > 0)
            writer.write(value);
        else
            writer.writeRaw(emptyMessageKey);
    }

    void afterRender(MarkupWriter writer)
    {
        writer.end();

        Link link = resources.createEventLink(EventConstants.ACTION, contextArray);
        jsSupport.addScript("new Ajax.InPlaceEditor('%s', '%s', {cancelControl: 'button', cancelText: '%s', " +
                "clickToEditText: '%s', savingText: '%s', okText: '%s', htmlResponse: true, size: %s, stripLoadedTextTags: true," +
                "selectOptions: [%s]});",
                                getClientId(), link.toAbsoluteURI(),
                                messages.get("cancelbutton"),
                                messages.get("title"),
                                messages.get("saving"),
                                messages.get("savebutton"),
                                size,
                                getSelectboxContent());
    }
    
    private String getSelectboxContent() {
    	final StringBuilder builder = new StringBuilder();
    	
    	JSONObject entry;
    	
    	if (blankOption) {
    		entry = new JSONObject();
    		entry.append("name", "");
    		builder.append(entry.toString()+",");
    	}
    	for (final Long key: content.keySet()) {
    		entry = new JSONObject();
    		entry.append("name", content.get(key));
    		entry.append("value", key);
    		entry.append("selected", (content.get(key).equals(value)?"true":"false"));
    		builder.append(entry+",");
    	}
    	builder.deleteCharAt(builder.length()-1);
    	return builder.toString().trim();
    }
    

    StreamResponse onAction(Object[] ctx) throws UnsupportedEncodingException
    {
    	String valueText = request.getParameter("value");
    	
    	resources.triggerEvent(SAVE_EVENT, ArrayUtils.add(ctx, valueText), null);

        if (valueText == null || valueText.length() == 0) {
        	valueText = emptyMessageKey;
        } else {
        	valueText = content.get(Long.valueOf(valueText));
        }
        return new TextStreamResponse("text/html", new String(valueText.getBytes("UTF8")));
    }

    /**
     * Returns a unique id for the element. This value will be unique for any given rendering of a page. This value is
     * intended for use as the id attribute of the client-side element, and will be used with any DHTML/Ajax related
     * JavaScript.
     */
    public String getClientId()
    {
        return assignedClientId;
    }


}
