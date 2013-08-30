if(!dojo._hasResource["dojoui.DropDownButton"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.DropDownButton"] = true;
dojo.provide("dojoui.DropDownButton");
dojo.require("dijit.form.DropDownButton");
dojo.require("dijit.Menu");

dojo.declare("dojoui.DropDownButton", dijit.form.DropDownButton, {
	closeable: true,
	createMenuItem: function(o){
		var self = this;
		var item = new dijit.MenuItem({
			label: o.label,
			//style: "display: none;",
			parentMenu: self.dropDown,
			onClick: o.onClick
		});
		this.dropDown.addChild(item);
		return item;
	},
	removeMenuItem: function(menuItem){
		this.dropDown.removeChild(menuItem);
	},
	clear: function(){
		var children = this.dropDown.getChildren();
		for ( var i = 0; i < children.length; i++) {
			this.dropDown.removeChild(children[i]);
		}
	},
	postCreate: function(){
		this.dropDown = new dijit.Menu({});
		this.inherited(arguments);
	},
	setDisabled: function(boolean){
		this.set("disabled",boolean);
	},
	closeDropDown: function(){
		if(this.closeable){
			this.inherited(arguments);
		}
	},
	forceClose: function(){
		this.closeable = true;
		this.closeDropDown();
		this.closeable = false;
	}
//		setReadOnly: function(boolean){
//			this.attr("readOnly",boolean);//to allow a tooltip to be shown
//		},
//		setLabel: function(label){
//			this.attr("label",label);
//		},
//		connectOnClick: function(f){
//			return dojo.connect(this, 'onClick', function(e){e.src = _self; f(e);});
//		}
});
}