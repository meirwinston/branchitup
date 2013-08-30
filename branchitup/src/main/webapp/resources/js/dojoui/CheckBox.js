if(!dojo._hasResource["dojoui.CheckBox"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.CheckBox"] = true;
dojo.provide("dojoui.CheckBox");
dojo.require("dijit.form.CheckBox");

dojo.declare("dojoui.CheckBox", dijit.form.CheckBox, {
//		containerNode: document.createElement("span"), //without this line we can an exception???
	containerNode: {},
	showLabel: true,
	postCreate: function(){
		this.inherited(arguments);
//		if(this.label){
//			var l = dojo.create("label",{'for': this.id, innerHTML: this.label});
//			document.body.appendChild(l);
//			console.log("set label in checkbox: " + this.id + ", " + this.label);
//		}
	},
	setDisabled: function(boolean){
		this.set("disabled",boolean);
	},
	setReadOnly: function(boolean){
		this.set("readOnly",boolean);//to allow a tooltip to be shown
	},
	setLabel: function(label){
		this.set("label",label);
	},
	connectOnClick: function(f){
		var _self = this;
		return dojo.connect(this, 'onClick', function(e){e.src = _self; f(e);});
	}
});
}