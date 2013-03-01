package nl.intercommit.weaves.services.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;
/**
 * Includes a minified JQuery 1.7.2 library and initializes it directly with:
 * 
 * jQuery.noConflict();
 * 
 */
public class JQueryJavaScriptStack implements JavaScriptStack {

	@Inject
	private AssetSource as;
	
	@Override
	public String getInitialization() {
		return null;
	}

	@Override
	public List<Asset> getJavaScriptLibraries() {
		List<Asset> assets = new ArrayList<Asset>();
		assets.add(as.getUnlocalizedAsset("nl/intercommit/weaves/jquery/jquery-1.9.1.min.js"));
		assets.add(as.getUnlocalizedAsset("nl/intercommit/weaves/jquery/jquery_init.js"));
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
