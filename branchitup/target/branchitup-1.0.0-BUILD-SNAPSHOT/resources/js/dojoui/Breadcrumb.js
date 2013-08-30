if(!dojo._hasResource["dojoui.Breadcrumb"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.Breadcrumb"] = true;
dojo.provide("dojoui.Breadcrumb");
dojo.require("dijit._Widget");
//usage: var label = new dojoui.Label({value: "Filter", style: {color: "gray"}});
dojo.declare("dojoui.Breadcrumb", dijit._Widget, {
	items: [],
	addItem: function(item){
		this.items.push(item);
		this.render();
	},
	setItems: function(items){
		this.items = items;
		this.render();
	},
	removeLastItem: function(){
		this.items.splice(this.items.length-1,1);
		this.render();
	},
	render: function(){
		this.domNode.innerHTML = "";
		var items = this.items;
		for(var i in items){
			var a = dojo.create("a");
			a.item = items[i];
			a.innerHTML = items[i].label;
			if(items[i].href){
				a.href = items[i].href;
			}
			else if(items[i].func){
				dojo.connect(a,"click",items[i].func);
				a.style.color = "blue";
				a.style.textDecoration = "underline";
				a.style.cursor = "pointer";
			}
//			console.log("*******" + items[i].label);
//			console.log(a);
			this.domNode.appendChild(a);
			
			if(i < items.length - 1){ //seperator
				var s = dojo.create("span");
				s.innerHTML = "&nbsp;&nbsp;/&nbsp;&nbsp;";
				this.domNode.appendChild(s);
			}
		}
	},
	postCreate: function(){
		var d = dojo.create("div");
		if(this.style){
			for(var k in this.style){
				d.style[k] = this.style[k];
			}
		}
		this.domNode = d;
		this.render();
	},
	_setValueAttr: function(newValue){ //inherit this method as a connection to attr("value",newValue)
		this.domNode.innerHTML = newValue;
	}
});
}