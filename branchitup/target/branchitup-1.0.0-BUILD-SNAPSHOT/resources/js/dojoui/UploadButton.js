if(!dojo._hasResource["dojoui.UploadButton"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.UploadButton"] = true;
dojo.provide("dojoui.UploadButton");

dojo.require("dojoui.Button");
dojo.require("dijit.TooltipDialog");
dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.Button");
dojo.require("dojoui.UploadForm");
dojo.require("dojox.form.Uploader");
dojo.require("dojox.form.uploader.FileList");
//dojo.require("dijit.form.TextBox");

/**
 * name - A key property to the Uploader is "name", which is subsequently used as the name attribute 
 on the file input that is created, or as the field name that Flash assigns to each file. 
 The default name is "uploadedfile" which works with the provided UploadFile.php. 
 The HTML5 spec specifies that attributes that are array-like should use square brackets at 
 the end of the field name. If a HTMl5-capable browser is used, the Uploader appends these 
 brackets. It also appends an "s" at the end to help the server script determine the file data. 
 If the Flash plugin is in use, the Uploader appends "Flash" to the end of the name, 
 or whatever the "flashFieldName" property is set to. If a standard file input is in 
 use (in "Form" mode or IFrame), the name is left as-is. 
 The variations of the name attribute is necessary so that the server page knows what is being 
 sent and can handle it appropriately.
 
 url - If you don't supply an action in your form, supply an url to upload to.	 
 * Plugins will self initialize, so to use one, all that is needed is to require it. 
 * The following example uses the IFrame plugin, which inherits from the HTML5 plugin. 
 * So HTML5 file inputs are used when available and the IFrame technique used elsewhere
 * 
 * MAKE SURE TO PUT THE IT ON A VISIBLE CONTAINER, OTHERWISE IE WILL NOT LOAD THE SWF FLASH 
 * dojo 1.6
 */
if(dojo.isIE){
	/**
	 * NOTE:
	 * IE8 throws a SWF exception if you create more than one dojox.form.Uploader objects
	 * using the dojox.form.uploader.plugins.Flash plugin
	 */
//	dojo.require("dojox.form.uploader.plugins.IFrame");
	dojo.require("dojox.form.uploader.plugins.Flash");
	console.log("UploadButton.Flash");
}
else{
	dojo.require("dojox.form.uploader.plugins.HTML5");
	console.log("UploadButton.HTML5");
}
/*
this.fileUploader = new dojox.form.FileUploader({
	    fileMask: [
       	    ["Jpeg File","*.jpg;*.jpeg"],
    	    ["GIF File","*.gif"],
    	    ["PNG File","*.png"],
    	    ["All Images","*.jpg;*.jpeg;*.gif;*.png"],
    	],
  	    force: "html",
  	    uploadUrl: self.uploadUrl,
  	    selectMultipleFiles: false,
  	    serverTimeout: 10000, //in milliseconds
  	    showProgress: true,
  	    isDebug:true,
  	    devMode: true
  	},b);
},
 */
dojo.declare("dojoui.UploadButton", dojox.form.Uploader, {
	name: "myFile[]",
	type: "file",
	label:"Browse...", 
	multiple:true, 
	uploadOnSelect:false,
	fileListPlugin: new dojox.form.uploader.FileList({uploader: this}),
	form: new dojoui.UploadForm({}),
//	form: dojo.create("form",{value: "aaa", method: "post",enctype: "multipart/form-data", action: "service/UploadFile",}),
//	style: {width: "50px", height: "20px"},
    onChange: function(){
    	console.log("UploadButton.onChange");
		this.upload();
//    	this.submit();
    },
    fileNameInput: dojo.create("input",{type: "text"}) //try this
//,
//	fileNameInput: new dijit.form.TextBox({
//		required: "true",
//		invalidMessage: "Invalid File Name"
//	})
});
}