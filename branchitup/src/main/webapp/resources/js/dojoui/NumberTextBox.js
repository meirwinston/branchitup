if(!dojo._hasResource["dojoui.NumberTextBox"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.NumberTextBox"] = true;
dojo.provide("dojoui.NumberTextBox");

dojo.require("dijit.form.NumberTextBox");
dojo.declare("dojoui.NumberTextBox", dijit.form.NumberTextBox, {
//	jsId: this.id,
//	widgetId: this.id,
//	maxLength: 3,
//	constraints: {min: 0, max: 10, places: 0}, //places - number of figures after the decimal point
//	invalidMessage: "Invalid Value",
//	required: true,
	postCreate: function(){
	this.attr("constraints",{min: 15, max: 365, places: 0});
		this.inherited(arguments);
		
	},
	getValue: function(){
		return this.attr("value");
	},
	setValue: function(value){
		this.attr("value",value);
	},
	isValid: function(){
		return (this.state == "");
	},
	setDisabled: function(b){
		this.attr("disabled",b);
	},
	setInvalidMessage: function(message){
		this.attr("invalidMessage",message);
	}
});
}