Object.extend(Ajax.InPlaceEditor.prototype, {
	createEditField: function() {
		if (this.options.selectOptions) {
			var fld = document.createElement('select');
				fld.name = this.options.paramName;
			this.options.selectOptions.each(function(el) {
				var opt = document.createElement('option');
					opt.value = el.value;
					opt.appendChild(document.createTextNode(el.name));
				if (el.selected==true) {
					opt.setAttribute('selected','selected');
				}
				fld.appendChild(opt);
			});
		} else {
			var text = (this.options.loadTextURL ? this.options.loadingText : this.getText());
			var fld;
			if (1 >= this.options.rows && !/\r|\n/.test(this.getText())) {
				fld = document.createElement('input');
				fld.type = 'text';
				var size = this.options.size || this.options.cols || 0;
				if (0 < size) fld.size = size;
			} else {
				fld = document.createElement('textarea');
				fld.rows = (1 >= this.options.rows ? this.options.autoRows : this.options.rows);
				fld.cols = this.options.cols || 40;
			}
			fld.name = this.options.paramName;
			fld.value = text; // No HTML breaks conversion anymore
			fld.className = 'editor_field';
		}
		if (this.options.submitOnBlur) 
		  fld.onblur = this._boundSubmitHandler;
		  this._controls.editor = fld;
		if (this.options.loadTextURL)
		  this.loadExternalText();
		  this._form.appendChild(this._controls.editor);
	}
});
