if(!dojo._hasResource["dojoui.Placeholder"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.Placeholder"] = true;
dojo.provide("dojoui.Placeholder");
dojo.require("dijit._Widget");
//usage: var label = new dojoui.Placeholder({value: "Filter", style: {color: "gray"}});
//a container to queue up multiple widgets that can be presented one at the time
dojo.declare("dojoui.Placeholder", dijit._Widget, {
	value: 0,
	widgets: [],
	postCreate: function(){
		var d = dojo.create("span");
		if(this.style){
			d.style.cssText = this.style;
		}
		this.domNode = d;
		
		if(this.widgets.length > 0){
			this.attr("value",0);
		}
	},
	_setValueAttr: function(index){ //inherit this method as a connection to attr("value",newValue)
		this.domNode.innerHTML = "";
		if(index >= 0 && index < this.widgets.length && this.widgets[this.value]){
			this.value = index;
			this.domNode.appendChild(this.widgets[this.value].domNode);
		}
//		else{
//			console.log("Error dojoui.Placeholder:" + index +":" + this.widgets[this.value]);
//		}
	}
});
}