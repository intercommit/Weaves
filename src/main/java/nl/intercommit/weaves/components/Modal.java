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
*/package nl.intercommit.weaves.components;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.base.AbstractLink;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.ClientBehaviorSupport;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * A bootstrap modal see: http://getbootstrap.com/javascript/#modals
 * 
 */
public class Modal extends AbstractLink {

	@Parameter(allowNull=true,defaultPrefix=BindingConstants.LITERAL)
	private String block;
	
	@Parameter(allowNull=true,defaultPrefix=BindingConstants.LITERAL)
	private String event;
	
	@Parameter
	private Object[] context;
	
	@Inject
	@Symbol(nl.intercommit.weaves.SymbolConstants.BOOTSTRAP_ENABLED)
	private boolean bootstrap;
	
	@Inject
	private ComponentResources resources;
	
	@Inject
	private JavaScriptSupport jss;
	
	@Inject
	private ClientBehaviorSupport cbs;
	
	@Inject
	private AjaxResponseRenderer arr;
	
	@Inject
	private Request request;
	
	@Inject
	private TypeCoercer tc;
	
	private String _modalId;
	private String _generatedZoneID;
	  
	@SetupRender
	private void init() {
		if (!bootstrap) {
			throw new RuntimeException("Bootstrap (stack) should be enabled for this component");
		}
		if (block == null && event == null) {
			throw new RuntimeException("Either block or event must be specified");
		}
		_generatedZoneID = jss.allocateClientId("modalZone");
		cbs.addZone(_generatedZoneID, "show", "none");
		_modalId = jss.allocateClientId(resources);
	}
	
	@OnEvent(value="fetchModalContent")
	private void openModal(final Object... localcontext) {
		if (block != null) {
			arr.addRender(tc.coerce(localcontext[1],String.class), resources.getPage().getComponentResources().findBlock(block));	
		} else {
			resources.triggerEvent(event,ArrayUtils.subarray(localcontext,2,localcontext.length), new ComponentEventCallback<Block>() {
				
				public boolean handleResult(Block result) {
					if (result != null) {
						arr.addRender(tc.coerce(localcontext[1],String.class), result);
						return true;
					}
					return false;
				};
			});
		}
		
		arr.addCallback(new JavaScriptCallback() {
			
			@Override
			public void run(JavaScriptSupport javascriptSupport) {
				javascriptSupport.addScript("$T5_JQUERY('#m_"+tc.coerce(localcontext[0],String.class)+"').modal('toggle');");
			}
		});
	}
	
	void beginRender(MarkupWriter writer)
    {
        if (isDisabled()) return;
        final Link link = resources.createEventLink("fetchModalContent",ArrayUtils.addAll(new Object[] {_modalId, _generatedZoneID},context));
        writeLink(writer, link);

        if (_generatedZoneID != null)
        {
            if (!request.isXHR())
                writer.getElement().forceAttributes(MarkupConstants.ONCLICK, MarkupConstants.WAIT_FOR_PAGE);

            cbs.linkZone(getClientId(), _generatedZoneID, link);
        }
    }
	
	 void afterRender(MarkupWriter writer) {
        if (isDisabled()) return;
        writer.end(); // <a>
    }
	
	@CleanupRender
	void createModal(MarkupWriter writer) {
		 // write the modal , somewhere below the body
		 final Element body = writer.getDocument().getRootElement().find("body");
		 final Element modal = body.element("div", "class","modal","id","m_"+_modalId);
		 final Element dialog = modal.element("div", "class","modal-dialog");
		 final Element content = dialog.element("div", "class","modal-content");
		 content.element("div", "id",_generatedZoneID);
	 }
}