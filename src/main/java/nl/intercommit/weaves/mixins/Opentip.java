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
package nl.intercommit.weaves.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * 
 * @tapestrydoc
 *
 */
@SupportsInformalParameters
@Import(library = {"opentip/opentip.min.js", "opentip/opentip_init.js"}, stylesheet = "opentip/opentip.css")
public class Opentip
{
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String tip;
   
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String tipTitle;
   
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String tipEvent;
   
   @Parameter(allowNull=true,defaultPrefix=BindingConstants.PROP)
   private Object context;
   
   @InjectContainer
   private ClientElement element;
   
   @Inject
   private JavaScriptSupport javaScriptSupport;
   
   @Inject
   private ComponentResources resources;
   
   @AfterRender
   void addJavascript()
   {
      final JSONObject params = new JSONObject();
      
      params.put("elementId", element.getClientId());
      params.put("tipTitle", tipTitle);
      params.put("tip", tip);
      
      if(tipEvent != null)
      {
         params.put("url", createAjaxTipEvent());
      }
      
      params.put("options", createOptionsFromInformalParameters());
      
      javaScriptSupport.addInitializerCall("setupOpentip", params);      
   }

   private String createAjaxTipEvent() {
	   return resources.createEventLink(tipEvent,context).toURI();
   }

   private JSONObject createOptionsFromInformalParameters()
   {
      JSONObject options = new JSONObject();

      for(String parameterName: resources.getInformalParameterNames())
      {
         options.put(parameterName, 
            resources.getInformalParameter(parameterName, String.class));
      }
      
      return options;
   }

}


	

