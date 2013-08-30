if(!dojo._hasResource["dojoui.Toolbar"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.Toolbar"] = true;
dojo.provide("dojoui.Toolbar");
dojo.require("dijit._Widget");
dojo.declare("dojoui.Toolbar", dijit._Widget, {
	children: [],
	postCreate: function(){
		var t = dojo.create("table");
		t.cellSpacing = 0;
		t.cellPadding = 0;
		t.border = "0px";
		if(this['class']){
			t.className = this['class'];
		}
		else{
			t.className = "tundra dijitToolbar";
		}
		
		t.style.width = "100%";
		
		t.insertRow(0);
		this.domNode = t;
	},
	addChild: function(a, style){
		this.children.push(a);
		var r = this.domNode.rows[0];
		var c = r.insertCell(r.cells.length);
		c.appendChild(a.domNode);
		
		if(style){
			for(var k in style){
				c.style[k] = style[k];
			}
//			c.style.cssText = style;
//			console.log("toolbar " + style);
//			console.log(c);
		}
	},
	insertChild: function(a, index){
		this.children.push(a);
		var r = this.domNode.rows[0];
		var c = r.insertCell(index);
		c.appendChild(a.domNode);
	}
//	_setValueAttr: function(newValue){ //inherit this method as a connection to attr("value",newValue)
//		this.domNode.innerHTML = newValue;
//	}
});
//--
//dojo.declare("dojoui.Toolbar", dijit._Widget, {
//	children: [],
//	postCreate: function(){
//		var t = dojo.create("table");
//		t.cellSpacing = 0;
//		t.cellPadding = 0;
//		t.border = "0px";
//		t.className = "soria dijitToolbar";
//		t.style.width = "100%";
////		t.style.height = "10px";
//		
//		var r = t.insertRow(0);
//		var c = r.insertCell(r.cells.length);
//		c.style.width = "100%";
//		this.domNode = t;
//	},
//	addChild: function(a){
//		this.children.push(a);
//		var r = this.domNode.rows[0];
//		var c = r.insertCell(r.cells.length-1);
////		c.style.height = "20px";
//		c.appendChild(a.domNode);
////		if(a.domNode.style.width){
////			c.style.width = a.domNode.style.width; 
////		}
////		else if(a.domNode.clientWidth){
////			c.style.width = a.domNode.clientWidth; 
////		}
//	}
////	_setValueAttr: function(newValue){ //inherit this method as a connection to attr("value",newValue)
////		this.domNode.innerHTML = newValue;
////	}
//});
}