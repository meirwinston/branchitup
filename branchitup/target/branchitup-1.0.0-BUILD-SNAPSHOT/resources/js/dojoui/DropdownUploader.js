if(!dojo._hasResource["dojoui.DropdownUploader"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.DropdownUploader"] = true;
dojo.provide("dojoui.DropdownUploader");

dojo.require("dojoui.DropDownButton");
dojo.require("dijit.TooltipDialog");
dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.Button");
dojo.require("dijit.form.Form");
dojo.require("dojox.form.Uploader");
dojo.require("dojox.form.uploader.FileList");

/**
 * Plugins will self initialize, so to use one, all that is needed is to require it. 
 * The following example uses the IFrame plugin, which inherits from the HTML5 plugin. 
 * So HTML5 file inputs are used when available and the IFrame technique used elsewhere
 * 
 * dojo 1.6
 */
dojo.require("dojox.form.uploader.plugins.HTML5");

dojo.declare("dojoui.DropdownUploader", dojoui.DropDownButton, {
	uploadButton: null,
	uploader: null,
	popupDialog: null,
	preUpload: null,
	postUpload: null,
	label: "Image",
	dropDown: new dijit.TooltipDialog({}),
    closeable: true,
    uploadId: 0,
    onClick: function(){
    	console.log("drop down on click");
    	this.set("closeable",false);
    },
	fileNameInput: new dijit.form.ValidationTextBox({
		required: "true",
		invalidMessage: "Invalid File Name"
	}),
	/**
	 * Because the Uploader uses your existing form, you must set its enctype to "multipart/form-data" 
	 * which is used for uploading files.
	 */
	_initUploadForm: function(){
		var self = this;
		this.uploadForm = new dijit.form.Form({
			method: "post",
			enctype: "multipart/form-data",
			action: self.url
		});
	},
	_initUploader: function(){
  		var self = this;
  		//---------
  		/**
  		 * name - A key property to the Uploader is "name", which is subsequently used as the name attribute 
  		 * on the file input that is created, or as the field name that Flash assigns to each file. 
  		 * The default name is "uploadedfile" which works with the provided UploadFile.php. 
  		 * The HTML5 spec specifies that attributes that are array-like should use square brackets at 
  		 * the end of the field name. If a HTMl5-capable browser is used, the Uploader appends these 
  		 * brackets. It also appends an "s" at the end to help the server script determine the file data. 
  		 * If the Flash plugin is in use, the Uploader appends "Flash" to the end of the name, 
  		 * or whatever the "flashFieldName" property is set to. If a standard file input is in 
  		 * use (in "Form" mode or IFrame), the name is left as-is. 
  		 * The variations of the name attribute is necessary so that the server page knows what is being 
  		 * sent and can handle it appropriately.
  		 * 
  		 * url - If you don't supply an action in your form, supply an url to upload to.
  		 */
  		this.uploader = new dojox.form.Uploader({
  			name: "myFile[]",
  			type: "file",
//  			id:"fileUploader",
  			label:"Browse...", 
  			multiple:false, 
  			uploadOnSelect:false,
  			url: self.url,
  			form: self.uploadForm
  		});
  		
  		//this plugin can't be installed automatically (I think)
  		this.fileListPlugin = new dojox.form.uploader.FileList({uploader: this.uploader});
  	},
  	
    _initUploadButton: function(){
    	var self = this;
    	this.uploadButton = new dijit.form.Button({
			label: "Upload",
			onClick: function(){
//				self.forceClose();
				self.set("closeable",true);
				self.uploadId++;
				if(self.preUpload){
					self.preUpload(self.uploadId);
				}
				self.uploader.submit();
//				printObject(self.uploader);
			}
    	});
    },
    _initPopupDialog : function(){
    	var t = dojo.create("table");
    	t.border = "1px";
    	t.cellPadding = "0";
    	t.cellSpacing = "0";
    	
    	var row = t.insertRow(t.rows.length);
    	var cell = row.insertCell(0);
//    	cell.style.verticalAlign = "middle";
    	cell.appendChild(this.fileNameInput.domNode);
    	cell = row.insertCell(1);
    	cell.appendChild(this.uploader.domNode);
    	
    	row = t.insertRow(t.rows.length);
    	cell = row.insertCell(0);
    	cell.colSpan = 2;
    	cell.appendChild(this.uploadButton.domNode);
    	
    	//-- list of files plugin
    	row = t.insertRow(t.rows.length);
    	cell = row.insertCell(0);
    	cell.colSpan = 2;
    	this.fileListPlugin.domNode.style.width = "200px";
    	cell.appendChild(this.fileListPlugin.domNode);
    	//--
    	this.uploadForm.domNode.appendChild(t);
    	this.dropDown.attr("content",this.uploadForm);
    },
    _initEvents: function(){
    	var self = this;
    	var _fileArr = [];
    	dojo.connect(this.uploader, "onChange", function(fileArray){
//    	    console.log("DropdownUploader.uploader onchange: " + fileArray);
//    	    console.log(fileArray);
    	    if(fileArray.length > 0){
//    	    	console.log("DropdownUploader:::::::" + dataArray[0].name);
    	    	self.fileNameInput.set("value",fileArray[0].name);
    	    	_fileArr = fileArray;
    	    }
    	});
//    	dojo.connect(self.uploader, "onProgress", function(dataArray){
//    		console.log("uploader onProgress: ");
//    	    console.log(dataArray);//name,bytesTotal,percent,size,type,bytesLoaded
//    	});
    	dojo.connect(self.uploader, "onComplete", function(customEvent){
//    		console.log("DropdownUploader::uploader onComplete: " + self.uploadId + ", " + customEvent);
    	    if(self.postUpload){
    	    	self.postUpload(self.uploadId,customEvent);
			}
    	});
    },
	postCreate: function(){
		this._initUploadForm();
		this._initUploadButton();
		this._initUploader();
		this._initPopupDialog();
		this._initEvents();
		this.inherited(arguments);
	}
});
}