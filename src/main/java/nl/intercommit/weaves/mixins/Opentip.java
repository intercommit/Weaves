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


	

