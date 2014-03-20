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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.FieldValidationSupport;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.AfterRenderTemplate;
import org.apache.tapestry5.annotations.BeforeRenderTemplate;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.corelib.mixins.RenderDisabled;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.FieldValidatorDefaultSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.util.EnumSelectModel;

/**
 * @tapestrydoc
 */
@SupportsInformalParameters
@Import(library="editableselect/EditableSelect.js",stylesheet="editableselect/EditableSelect.css")
public class EditableSelectBox extends AbstractField {
	
    public static final String CHANGE_EVENT = "change";
 
    /**
     * Allows a specific implementation of {@link ValueEncoder} to be supplied. This is used to create client-side
     * string values for the different options.
     * 
     * @see ValueEncoderSource
     */
    @Parameter
    private ValueEncoder encoder;

    @Inject
    private ComponentDefaultProvider defaultProvider;

    /**
     * The model used to identify the option groups and options to be presented to the user. This can be generated
     * automatically for Enum types.
     */
    @Parameter(required = true, allowNull = false)
    private List<String> items;
    
    // additional class name(s)
    @Parameter(required = false, defaultPrefix=BindingConstants.LITERAL)
    private String className;

    @Inject
    private Request request;

    @Inject
    private ComponentResources resources;

    @Environmental
    private ValidationTracker tracker;

    /**
     * Performs input validation on the value supplied by the user in the form submission.
     */
    @Parameter(defaultPrefix = BindingConstants.VALIDATE)
    private FieldValidator<Object> validate;

    /**
     * The value to read or update.
     */
    @Parameter(required = true, principal = true, autoconnect = true)
    private Object value;

    /**
     * Binding the zone parameter will cause any change of Select's value to be handled as an Ajax request that updates
     * the
     * indicated zone. The component will trigger the event {@link EventConstants#VALUE_CHANGED} to inform its
     * container that Select's value has changed.
     * 
     * @since 5.2.0
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String zone;

    @Inject
    private FieldValidationSupport fieldValidationSupport;

    @Inject
    private JavaScriptSupport javascriptSupport;

    @SuppressWarnings("unused")
    @Mixin
    private RenderDisabled renderDisabled;

    private String selectedClientValue;
 
    @SuppressWarnings(
    { "unchecked" })
    @Override
    protected void processSubmission(String elementName)
    {
        final String submittedValue = request.getParameter(elementName);
        
        tracker.recordInput(this, submittedValue);

        Object selectedValue = toValue(submittedValue);

        putPropertyNameIntoBeanValidationContext("value");

        try
        {
            fieldValidationSupport.validate(selectedValue, resources, validate);

            value = selectedValue;
        }
        catch (ValidationException ex)
        {
            tracker.recordError(this, ex.getMessage());
        }

        removePropertyNameFromBeanValidationContext();
    }

    void afterRender(MarkupWriter writer)
    {
        writer.end();
    }

    void beginRender(MarkupWriter writer)
    {
    	selectedClientValue = tracker.getInput(this);

        // Use the value passed up in the form submission, if available.
        // Failing that, see if there is a current value (via the value parameter), and
        // convert that to a client value for later comparison.
        
        if (selectedClientValue == null)
            selectedClientValue = value == null ? null : encoder.toClient(value);
        
        String computedClassName =  "select-input";
        if (StringUtils.isNotBlank(className)) {
        	computedClassName = computedClassName + " " + className;
        }
        
        writer.element("input","name",getControlName(),"id",getClientId(),"type","text","class",computedClassName,"autocomplete","off","value",selectedClientValue);

        putPropertyNameIntoBeanValidationContext("value");

        validate.render(writer);

        removePropertyNameFromBeanValidationContext();

        resources.renderInformalParameters(writer);
        
        decorateInsideField();

        // Disabled is via a mixin

        if (this.zone != null)
        {
            Link link = resources.createEventLink(CHANGE_EVENT);

            JSONObject spec = new JSONObject("selectId", getClientId(), "zoneId", zone, "url", link.toURI());

            javascriptSupport.addInitializerCall("linkSelectToZone", spec);
        }
    }

    Object onChange(@RequestParameter(value = "t:selectvalue", allowBlank = true)
    final String selectValue)
    {
        final Object newValue = toValue(selectValue);

        CaptureResultCallback<Object> callback = new CaptureResultCallback<Object>();

        this.resources.triggerEvent(EventConstants.VALUE_CHANGED, new Object[]
        { newValue }, callback);

        this.value = newValue;

        return callback.getResult();
    }

    protected Object toValue(String submittedValue)
    {
        return InternalUtils.isBlank(submittedValue) ? null : this.encoder.toValue(submittedValue);
    }

    @SuppressWarnings("unchecked")
    ValueEncoder defaultEncoder()
    {
        return defaultProvider.defaultValueEncoder("value", resources);
    }

    @SuppressWarnings("unchecked")
    SelectModel defaultModel()
    {
        Class valueType = resources.getBoundType("value");

        if (valueType == null)
            return null;

        if (Enum.class.isAssignableFrom(valueType))
            return new EnumSelectModel(valueType, resources.getContainerMessages());

        return null;
    }

    /**
     * Computes a default value for the "validate" parameter using {@link FieldValidatorDefaultSource}.
     */
    Binding defaultValidate()
    {
        return defaultProvider.defaultValidatorBinding("value", resources);
    }

    /**
     * Renders the options, including the blank option.
     */
    @BeforeRenderTemplate
    void options(MarkupWriter writer)
    {
        JSONObject obj = new JSONObject();
        obj.put("data", new JSONArray(items.toArray()));
        javascriptSupport.addScript("var "+getClientId()+"Options="+ obj.toString()+";", "");
    }
    
    @AfterRenderTemplate
    void initializeBox() {
    	javascriptSupport.addScript("EditableSelect.init('"+getClientId()+"',"+getClientId()+"Options);", "");
    }

    @Override
    public boolean isRequired()
    {
        return validate.isRequired();
    }
}
