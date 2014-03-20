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
package nl.intercommit.weaves.services.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;
/**
 * 
 * Add Bootstrap css/js to this stack for easy bootstrap integration
 * 
 */
public class BootstrapScriptStack implements JavaScriptStack {

	@Inject
	private AssetSource as;
	
	@Inject
	@Symbol(SymbolConstants.PRODUCTION_MODE)
	private boolean prod;
	
	@Inject
	@Symbol(nl.intercommit.weaves.SymbolConstants.BOOTSTRAP_ENABLED)
	private boolean bootstrap;
	
	@Override
	public String getInitialization() {
		return null;
	}

	@Override
	public List<Asset> getJavaScriptLibraries() {
		final List<Asset> assets = new ArrayList<Asset>(1);
		// can use the minimized version..
		if (prod) {
			assets.add(as.getUnlocalizedAsset("nl/intercommit/weaves/bootstrap/js/bootstrap.min.js"));
		} else {
			assets.add(as.getUnlocalizedAsset("nl/intercommit/weaves/bootstrap/js/bootstrap.js"));
		}
		if (bootstrap) {
			assets.add(as.getUnlocalizedAsset("nl/intercommit/weaves/bootstrap/js/TapestryOverride.js"));
		}
		return assets;
	}
	
	// Depends on JQUery
	@Override
	public List<String> getStacks() {
		final List<String> requiredStacks = new ArrayList<String>(1);
		requiredStacks.add("jquery");
		return requiredStacks;
	}

	@Override
	public List<StylesheetLink> getStylesheets() {
		final List<StylesheetLink> sheets = new ArrayList<StylesheetLink>(1);
		// can use the minimized version..
		sheets.add(new StylesheetLink(as.getUnlocalizedAsset("nl/intercommit/weaves/bootstrap/css/bootstrap.css")));
		sheets.add(new StylesheetLink(as.getUnlocalizedAsset("nl/intercommit/weaves/bootstrap/css/bootstrap-responsive.css")));
		return sheets;
	}

}
