/**
 * Overrides some tapestry &|| prototype default functions to add specific bootstrap design
 * or jQuery checks..
 */

Tapestry.ErrorPopup.prototype.createUI = function () {
    this.innerSpan = new Element("span", {'class':'label label-danger'});
    this.outerDiv = $(new Element("div", {
        'id': this.field.id + "_errorpopup",
        'class': 't-error-popup'
    })).update(this.innerSpan).hide();

    var body = $(document.body);

    body.insert({
        bottom: this.outerDiv
    });

    this.outerDiv.absolutize();

    this.outerDiv.observe("click", function (event) {
        this.ignoreNextFocus = true;

        this.stopAnimation();

        this.outerDiv.hide();

        this.field.activate();

        event.stop();
    }.bindAsEventListener(this));

    this.queue = {
        position: 'end',
        scope: this.field.id
    };

    Event.observe(window, "resize", this.repositionBubble.bind(this));

    document.observe(Tapestry.FOCUS_CHANGE_EVENT, function (event) {
        if (this.ignoreNextFocus) {
            this.ignoreNextFocus = false;
            return;
        }

        if (event.memo == this.field) {
            this.fadeIn();
            return;
        }

        /*
         * If this field is not the focus field after a focus change, then
         * its bubble, if visible, should fade out. This covers tabbing
         * from one form to another.
         */
        this.fadeOut();

    }.bind(this));
};

// dont call hide (prototype) on Bootstrapped 'dropdown open' elements...
Element.addMethods({
	  hide: function(element) {
			if (element.className=='dropdown open') {
				return element;	
			}
			element = $(element);
			element.style.display = 'none';
			return element;
		}
});
