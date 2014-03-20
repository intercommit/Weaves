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

import org.apache.commons.lang.ArrayUtils;
import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.FieldValidationSupport;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.SelectModelVisitor;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.BeforeRenderTemplate;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.corelib.data.BlankOption;
import org.apache.tapestry5.corelib.mixins.RenderDisabled;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.internal.util.SelectModelRenderer;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.FieldValidatorDefaultSource;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ValueEncoderFactory;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.util.EnumSelectModel;

/**
 * Select an item from a list of values, using an [X]HTML &lt;select&gt; element on the client side. Any validation
 * decorations will go around the entire &lt;select&gt; element.
 * <p/>
 * A core part of this component is the {@link ValueEncoder} (the encoder parameter) that is used to convert between
 * server-side values and unique client-side strings. In some cases, a {@link ValueEncoder} can be generated automatically from
 * the type of the value parameter. The {@link ValueEncoderSource} service provides an encoder in these situations; it
 * can be overriden by binding the encoder parameter, or extended by contributing a {@link ValueEncoderFactory} into the
 * service's configuration.
 *
 * @tapestrydoc
 */
@Events(
        {EventConstants.VALIDATE, EventConstants.VALUE_CHANGED + " when 'zone' parameter is bound"})
public class EnhancedSelect extends AbstractField
{
    public static final String CHANGE_EVENT = "change";

    private class Renderer extends SelectModelRenderer
    {

        public Renderer(MarkupWriter writer)
        {
            super(writer, encoder);
        }

        @Override
        protected boolean isOptionSelected(OptionModel optionModel, String clientValue)
        {
            return isSelected(clientValue);
        }
    }

    /**
     * A ValueEncoder used to convert the server-side object provided by the
     * "value" parameter into a unique client-side string (typically an ID) and
     * back. Note: this parameter may be OMITTED if Tapestry is configured to
     * provide a ValueEncoder automatically for the type of property bound to
     * the "value" parameter.
     *
     * @see ValueEncoderSource
     */
    @Parameter
    private ValueEncoder encoder;
    
    @Parameter(value="false")
	private boolean multiple;
    
    /**
     * The separator used to split a multi value field ,defaults to 'space' ' ' 
     */
    @Parameter(defaultPrefix=BindingConstants.LITERAL,value="literal: ")
    private String multiSeparator;

    @Inject
    private ComponentDefaultProvider defaultProvider;

    // Maybe this should default to property "<componentId>Model"?
    /**
     * The model used to identify the option groups and options to be presented to the user. This can be generated
     * automatically for Enum types.
     */
    @Parameter(required = true, allowNull = false)
    private SelectModel model;

    /**
     * Controls whether an additional blank option is provided. The blank option precedes all other options and is never
     * selected. The value for the blank option is always the empty string, the label may be the blank string; the
     * label is from the blankLabel parameter (and is often also the empty string).
     */
    @Parameter(value = "auto", defaultPrefix = BindingConstants.LITERAL)
    private BlankOption blankOption;

    /**
     * The label to use for the blank option, if rendered. If not specified, the container's message catalog is
     * searched for a key, <code><em>id</em>-blanklabel</code>.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String blankLabel;

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

    @Environmental
    private FormSupport formSupport;

    @Inject
    private JavaScriptSupport javascriptSupport;

    @SuppressWarnings("unused")
    @Mixin
    private RenderDisabled renderDisabled;

    private String selectedClientValue;

    private boolean isSelected(String clientValue) {
    	if (multiple) {
    		if (org.apache.commons.lang.StringUtils.isNotBlank(selectedClientValue)) {
    			return ArrayUtils.contains(selectedClientValue.split(multiSeparator), clientValue);
    		}
    	}
        return TapestryInternalUtils.isEqual(clientValue, selectedClientValue);
    }

    @SuppressWarnings(
            {"unchecked"})
    @Override
    protected void processSubmission(String controlName)
    {
    	String submittedValue = null;
    	// my multivalue hack
    	if (multiple) {
    		submittedValue = ArrayUtils.toString(request.getParameters(controlName));
    		
    		System.err.println("submittewd:" +submittedValue);
    		
    		System.err.println("SEPARATER: '" + multiSeparator + "'");
    		
    		submittedValue = submittedValue.replace(",",multiSeparator);
    		submittedValue = submittedValue.substring(1, submittedValue.length()-1);
    	} else {
    		submittedValue = request.getParameter(controlName);
    	}

        tracker.recordInput(this, submittedValue);

        Object selectedValue = toValue(submittedValue);

        putPropertyNameIntoBeanValidationContext("value");

        try
        {
            fieldValidationSupport.validate(selectedValue, resources, validate);

            value = selectedValue;
        } catch (ValidationException ex)
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
    	if (multiple) {
    		writer.element("select", "name", getControlName(), "id", getClientId(),"multiple","true");
    	} else {
    		writer.element("select", "name", getControlName(), "id", getClientId());
    	}

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
                {newValue}, callback);

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

    Object defaultBlankLabel()
    {
        Messages containerMessages = resources.getContainerMessages();

        String key = resources.getId() + "-blanklabel";

        if (containerMessages.contains(key))
            return containerMessages.get(key);

        return null;
    }

    /**
     * Renders the options, including the blank option.
     */
    @BeforeRenderTemplate
    void options(MarkupWriter writer)
    {
        selectedClientValue = tracker.getInput(this);

        // Use the value passed up in the form submission, if available.
        // Failing that, see if there is a current value (via the value parameter), and
        // convert that to a client value for later comparison.

        if (selectedClientValue == null)
            selectedClientValue = value == null ? null : encoder.toClient(value);
        if (showBlankOption())
        {
            writer.element("option", "value", "");
            writer.write(blankLabel);
            writer.end();
        }

        SelectModelVisitor renderer = new Renderer(writer);

        model.visit(renderer);
    }

    @Override
    public boolean isRequired()
    {
        return validate.isRequired();
    }

    private boolean showBlankOption()
    {
        switch (blankOption)
        {
            case ALWAYS:
                return true;

            case NEVER:
                return false;

            default:
                return !isRequired();
        }
    }

    // For testing.

    void setModel(SelectModel model)
    {
        this.model = model;
        blankOption = BlankOption.NEVER;
    }

    void setValue(Object value)
    {
        this.value = value;
    }

    void setValueEncoder(ValueEncoder encoder)
    {
        this.encoder = encoder;
    }

    void setValidationTracker(ValidationTracker tracker)
    {
        this.tracker = tracker;
    }

    void setBlankOption(BlankOption option, String label)
    {
        blankOption = option;
        blankLabel = label;
    }
}
