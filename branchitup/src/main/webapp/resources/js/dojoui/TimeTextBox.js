if(!dojo._hasResource["dojoui.TimeTextBox"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.TimeTextBox"] = true;
dojo.provide("dojoui.TimeTextBox");
dojo.require("dijit.form.TimeTextBox");
	
dojo.declare("dojoui.TimeTextBox", dijit.form.TimeTextBox, {	
	postCreate: function(){
		this.inherited(arguments);
	},
	setDisabled: function(b){
		this.attr("disabled",b);
	},
	setValue: function(date){
		this.attr("value",date);
	},
	getValue: function(){
		return this.attr("value");
	}
});
}