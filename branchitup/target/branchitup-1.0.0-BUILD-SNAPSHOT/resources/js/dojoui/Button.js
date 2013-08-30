if(!dojo._hasResource["dojoui.Button"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.Button"] = true;
dojo.provide("dojoui.Button");

dojo.require("dijit.form.Button");
dojo.declare("dojoui.Button", dijit.form.Button, {
	setDisabled: function(boolean){
		this.attr("disabled",boolean);
	},
	setReadOnly: function(boolean){
		this.attr("readOnly",boolean);//to allow a tooltip to be shown
	},
	setLabel: function(label){
		this.attr("label",label);
	},
	getLabel: function(label){
		return this.attr("label");
	},
	connectOnClick: function(f){
		return dojo.connect(this, 'onClick', f);
	},
	setAttribute: function(key,value){
		this.attr(key,value);
	},		
	getAttribute: function(key){
		return this.attr(key);
	}
});
}