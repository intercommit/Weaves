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
import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;
/**
 * Includes a minified JQuery library and initializes it directly with:
 * 
 * jQuery.noConflict();
 * 
 */
public class JQueryJavaScriptStack implements JavaScriptStack {

	@Inject
	private AssetSource as;
	
	@Inject
	@Symbol(SymbolConstants.PRODUCTION_MODE)
	private boolean prod;
	
	@Override
	public String getInitialization() {
		return null;
	}

	@Override
	public List<Asset> getJavaScriptLibraries() {
		final List<Asset> assets = new ArrayList<Asset>();
		if (prod) {
			assets.add(as.getUnlocalizedAsset("nl/intercommit/weaves/jquery/jquery-2.0.3.min.js"));
		} else {
			assets.add(as.getUnlocalizedAsset("nl/intercommit/weaves/jquery/jquery-2.0.3.js"));
		}
		return assets;
	}

	@Override
	public List<String> getStacks() {
		return Collections.emptyList();
	}

	@Override
	public List<StylesheetLink> getStylesheets() {
		return Collections.emptyList();
	}

}
