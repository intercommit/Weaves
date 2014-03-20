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

import javax.inject.Inject;

import nl.intercommit.weaves.services.IBootstrapElementConfig;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.func.Predicate;
import org.slf4j.Logger;

/**
 * Renders tapestry elements in Bootstrap 3.x style
 * 
 * @tapestrydoc
 *
 */
public class Bootstrap {
	 
	private static final String[] NON_STYLABLE = {"option","p","br","img","dt","dd","tr","td"};
	
	private int labelSize;
	private int inputSize;
	
	@Parameter(required=false,value="2",defaultPrefix="literal")
	private String formLabelSize;
	
	@Inject
	private Logger logger;
	
	@Inject
	private IBootstrapElementConfig config;
	
	@SetupRender
	void init() {
		labelSize = Integer.parseInt(formLabelSize);
		inputSize = 12 - labelSize;
	}

	void afterRenderTemplate(MarkupWriter writer) {
		writer.getElement().getElement(new Predicate<Element>() {
			
			@Override
			public boolean accept(Element arg0) {
				
				final String elementName = arg0.getName();
				final String inputType = arg0.getAttribute("type");
				final String className = arg0.getAttribute("class");
				
				// quickly skip some non-styleable stuff
				if (ArrayUtils.contains(NON_STYLABLE, elementName)) {
					return false;
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Styling element ["+elementName+"] with type ["+inputType+"] and class ["+className+"] ");
				}
				// Use JAVA7 features.
				switch (elementName) { 
					case "form":  {
						arg0.addClassName(config.getElementClassMapping().get("form"));
						break;
					}
					case "dl": {
						arg0.addClassName("dl-horizontal");
						break;
					}
					case "label": {
						arg0.addClassName("control-label");
						if (StringUtils.contains(arg0.getContainer().getAttribute("class"), "form-group")) {
							arg0.addClassName("col-lg-"+labelSize);
						}
						break;
					}
					case "input": {
						if (inputType != null) {
							if ("text".equals(inputType)) {
								arg0.addClassName("form-control");	 // inputtext
							} else if ("submit".equals(inputType)) {
								arg0.addClassName("btn");	// submit
							} else if ("password".equals(inputType)) {
								arg0.addClassName("form-control");	// password
							}
						} else {
							arg0.addClassName("form-control");	 // regular control
						}
						if (StringUtils.contains(arg0.getContainer().getAttribute("class"), "form-group")) {
							arg0.wrap("div", "class","col-lg-"+inputSize);
						}
						break;
					}
					case "select" : {
						arg0.addClassName("form-control");
						if (StringUtils.contains(arg0.getContainer().getAttribute("class"), "form-group")) {
							arg0.wrap("div", "class","col-lg-"+inputSize);
						}
						break;
					}
					case "textarea" : { 
						arg0.addClassName("form-control");
						if (StringUtils.contains(arg0.getContainer().getAttribute("class"), "form-group")) {
							arg0.wrap("div", "class","col-lg-"+inputSize);
						}
						break;
					}
					case "div" : {
						if ("t-beaneditor-row".equals(className)) {
							arg0.addClassName("form-group");
						} else if ("t-error".equals(className)) {
							arg0.addClassName("alert alert-danger");
						} else if ("input-group".equals(className)) {
							arg0.addClassName("col-lg-"+inputSize);
						}
						break;
					}
					case "table" : {
						arg0.addClassName("table");
						break;
					}
				}
				return false;
			}
		});
		
	}
}
