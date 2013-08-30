if(!dojo._hasResource["dojoui.UploadForm"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.UploadForm"] = true;
dojo.provide("dojoui.UploadForm");
dojo.require("dijit.form.Form");
dojo.declare("dojoui.UploadForm", dijit.form.Form, {
	method: "post",
	enctype: "multipart/form-data",
	action: self.url,
	getAttributeNode: function(p){ //only used in IE
		if(p == "action"){
			console.log("--->UploadForm.getAttributeNode:1: " + this.action);
			console.log(this.domNode);
			return {value: this.action};
		}
//		console.log("UploadButton.getAttributeNode");
	}
});
}