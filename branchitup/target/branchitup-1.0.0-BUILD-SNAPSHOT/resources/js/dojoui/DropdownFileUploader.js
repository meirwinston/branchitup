if(!dojo._hasResource["dojoui.DropdownFileUploader"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.DropdownFileUploader"] = true;
dojo.provide("dojoui.DropdownFileUploader");

dojo.require("dojoui.DropDownButton");
dojo.require("dijit.TooltipDialog");
dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.Button");
dojo.require("dijit.form.Form");
dojo.require("dojox.form.FileUploader");
/** dojo 1.4 */
dojo.declare("dojoui.DropdownFileUploader", dojoui.DropDownButton, {
	uploadButton: null,
	fileUploader: null,
	popupDialog: null,
	preUpload: null,
	postUpload: null,
	uploadUrl: "upload/FileUpload",
	label: "Image",
	dropDown: new dijit.TooltipDialog({}),
    closeable: false,
    uploadId: 0,
	fileNameInput: new dijit.form.ValidationTextBox({
		required: "true",
		invalidMessage: "Invalid File Name"
	}),
	uploadForm: new dijit.form.Form({
		method: "post",
		enctype: "multipart/form-data"
	}),
  	_initFileUploader: function(){
  		var self = this;
  		
  		var b = dojo.create("span"); //this is the panel of the fileUploader
		b.innerHTML = "Select Image...";
		b.style.color = "blue";
//		b.style.width = "100px";
		b.style.cursor = "pointer";
		document.body.appendChild(b);
  		//---------
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
    _initUploadButton: function(){
    	var self = this;
    	this.uploadButton = new dijit.form.Button({
			label: "Upload",
			onClick: function(){
				self.forceClose();
				self.uploadId++;
				if(self.preUpload){
					self.preUpload(self.uploadId);
				}
				self.fileUploader.upload();
			}
    	});
    },
    _initPopupDialog : function(){
    	var t = dojo.create("table");
    	t.border = "0px";
    	t.cellPadding = "0";
    	t.cellSpacing = "0";
    	
    	var row = t.insertRow(t.rows.length);
    	var cell = row.insertCell(0);
    	cell.appendChild(this.fileNameInput.domNode);
    	cell = row.insertCell(1);
    	cell.appendChild(this.fileUploader.domNode);
    	
    	row = t.insertRow(t.rows.length);
    	cell = row.insertCell(0);
    	cell.colSpan = 2;
    	cell.appendChild(this.uploadButton.domNode);
    	
    	this.dropDown.attr("content",t);
    },
    _initEvents: function(){
    	var self = this;
    	dojo.connect(this.fileUploader, "onChange", function(dataArray){
//    	    console.log("DropdownFileUploader.uploader onchange: " + dataArray);
//    	    console.log(dataArray);
    	    if(dataArray.length > 0){
//    	    	console.log("DropdownFileUploader:::::::" + dataArray[0].name);
    	    	self.fileNameInput.set("value",dataArray[0].name);
    	    }
    	});
//    	dojo.connect(_fileUploader, "onProgress", function(dataArray){
//    		console.log("uploader onProgress: ");
//    	    console.log(dataArray);//name,bytesTotal,percent,size,type,bytesLoaded
//    	});
    	dojo.connect(this.fileUploader, "onComplete", function(dataArray){
//    		console.log("DropdownFileUploader::uploader onComplete: " + self.uploadId);
//    	    console.log(dataArray); //file,name,type,height,width
    	    if(self.postUpload){
    	    	self.postUpload(self.uploadId,dataArray);
			}
    	});
    },
	postCreate: function(){
		this._initUploadButton();
		this._initFileUploader();
		this._initPopupDialog();
		this.inherited(arguments);
		this._initEvents();
	}
});
}