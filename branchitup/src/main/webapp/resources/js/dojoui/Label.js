if(!dojo._hasResource["dojoui.Label"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.Label"] = true;
dojo.provide("dojoui.Label");
dojo.require("dijit._Widget");
//usage: var label = new dojoui.Label({value: "Filter", style: {color: "gray"}});
dojo.declare("dojoui.Label", dijit._Widget, {
	value: "",
	postCreate: function(){
		var d = dojo.create("span");
		d.innerHTML = this.value;
		if(this.style){
			d.style.cssText = this.style;
		}
		if(this['class']){
			d.className = this['class'];
		}
		this.domNode = d;
	},
	_setValueAttr: function(newValue){ //inherit this method as a connection to attr("value",newValue)
		this.domNode.innerHTML = newValue;
	}
});
}