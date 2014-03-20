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

import org.apache.tapestry5.Asset2;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
/**
 * A pumped-up selectbox based on 'chosen' https://github.com/harvesthq/chosen
 * 
 * @tapestrydoc
 */
@Import(library="chosen/chosen.jquery.js") 
public class Chosen extends EnhancedSelect {
	
	@Inject
	private JavaScriptSupport js;
	
	@Inject
	@Symbol(nl.intercommit.weaves.SymbolConstants.BOOTSTRAP_ENABLED)
	private boolean bootstrap;
	
	@Inject @Path("chosen/chosen.css")
	private Asset2 chosenCss;
	
	@Inject @Path("chosen/chosen-bootstrap.css")
	private Asset2 chosenBs;
	
	@Parameter(allowNull=true,defaultPrefix=BindingConstants.PROP)
	private JSONObject options;
	
	@AfterRender
	private void enhancheSelect() {
		if (options == null) {
			options = new JSONObject();
		}
		
		js.addScript("$T5_JQUERY(\"#%s\").chosen("+options.toCompactString()+");", getClientId());
		
		if (bootstrap) {
			js.importStylesheet(chosenBs);
		} else {
			js.importStylesheet(chosenCss);
		}
	}
}
