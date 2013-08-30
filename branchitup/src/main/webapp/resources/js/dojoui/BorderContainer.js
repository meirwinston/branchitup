if(!dojo._hasResource["dojoui.BorderContainer"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.BorderContainer"] = true;
dojo.provide("dojoui.BorderContainer");
dojo.require("dijit.layout.BorderContainer");
dojo.declare("dojoui.BorderContainer", dijit.layout.BorderContainer, {
	closeAllowed: true,
	preClose: function(){},
	onClose: function(){
//		console.log("border container on close before close!");
//		console.log(this);
		this.preClose(arguments);
		if(this.closeAllowed){
			return this.inherited(arguments);
		}
		return false;
	}
});
}