if(!dojo._hasResource["dojoui.MultiSelectMenuButton"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.MultiSelectMenuButton"] = true;
dojo.provide("dojoui.MultiSelectMenuButton");

dojo.require("dijit.form.DropDownButton");
	
dojo.declare("dojoui.MultiSelectMenuButton", dijit.form.DropDownButton, {
	createCheckedMenuItem: function(o){ //o: label, checked, onClick
		var self = this;
		o.parentMenu = self.dropDown;
		o.closable = false;
		
		var item = new dijit.CheckedMenuItem(o);
		
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
	getMenuItems: function(){
		var children = this.dropDown.getChildren();
		return children;
	},
	getCheckedItems: function(){
		var arr = [];
		var children = this.dropDown.getChildren();
		for(var i = 0 ; i < children.length ; i++){
			if(children[i].checked){
				arr.push(children[i].params);
			}
		}
		return arr;
	},
	postCreate: function(){
		this.dropDown = new dijit.Menu({});
		this.jsId = this.id;
		this.widgetId = this.id;
		this.inherited(arguments);
	},
	setDisabled: function(boolean){
		this.attr("disabled",boolean);
	}
});
}