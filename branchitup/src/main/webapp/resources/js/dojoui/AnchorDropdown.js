if(!dojo._hasResource["dojoui.AnchorDropdown"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
	dojo._hasResource["dojoui.AnchorDropdown"] = true;
	dojo.provide("dojoui.AnchorDropdown");
//	dojo.require("dijit._Widget");
	dojo.require("dijit.TooltipDialog");
	dojo.require("dojoui.DropDownButton");
	
	dojo.declare("dojoui.AnchorDropdown", dojoui.DropDownButton, {
		postCreate: function(){
			this.dropDown = new dijit.TooltipDialog({content: this.dropdownContent});
			this.inherited(arguments);
			this.domNode.firstChild.className = "";
			this.domNode.firstChild.firstChild.className = "";
			this.domNode.firstChild.firstChild.style.color = "blue";
			this.domNode.firstChild.firstChild.style.textDecoration = "underline";
			this.domNode.firstChild.firstChild.style.cursor = "pointer";
		}
	});
}
	