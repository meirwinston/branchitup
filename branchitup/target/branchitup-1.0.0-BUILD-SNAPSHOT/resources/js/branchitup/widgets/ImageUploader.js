function ImageUploader(args){
	var _self = this;
	var _fileUploader = null;
	
	this.upload = function(){
		_fileUploader.upload();
	};
	
	function initLayout(){
		_self.domNode = _fileUploader.domNode;
		_self.domNode.style.height = "20px";
		_self.domNode.style.width = "155px";
		_self.domNode.style.display = "inline-block";
		
		_self.domNode.childNodes[1].style.width = "150px";
//		_self.domNode.childNodes[1].style.position = "";
//		_self.domNode.childNodes[1].style.cursor = "pointer";
		_self.domNode.childNodes[1].style.textAlign = "";
		_self.domNode.childNodes[1].className = "dijitReset dijitInline dijitButtonNode";	
		_self.domNode.childNodes[2].style.cursor = "pointer";
		_self.domNode.childNodes[2].style.height = "20px";
	}
	
	this.setUploadUrl = function(url){
		_fileUploader.set("uploadUrl",url);
	};
	
	function init(){
		dojo.require("dijit.form.Form");
		dojo.require("dijit.form.Button");
		dojo.require("dijit.form.ValidationTextBox");
		dojo.require("dojox.form.FileUploader");
		
		_form = new dijit.form.Form({
			id: generateId(),
			method: "post",
			enctype: "multipart/form-data"
		});
		var browseButton = dojo.create("div",{width: "100px", innerHTML: "Upload&nbsp;Image..."});
		document.body.appendChild(browseButton);
		
//		_uploadButton = new dijit.form.Button({
//			id: generateId(), 
//			label: "Upload",
//			onClick: function(){
//				_self.upload();
//			}
//		});
		var fileMask = [
		    ["Jpeg File","*.jpg;*.jpeg"],
		    ["GIF File","*.gif"],
		    ["PNG File","*.png"],
		    ["All Images","*.jpg;*.jpeg;*.gif;*.png"]
		];
		var fileUploaderArgs = {
			fileMask: fileMask,
		  //  htmlFieldName: "myhtmlFieldName",
		    force: "html",
		    uploadUrl: "service/FileUpload?params={\"method\":\"html5\",\"album\":\"Default\"}",
		    selectMultipleFiles: false,
		    serverTimeout: 10000, //in milliseconds
		    showProgress: true,
		    isDebug:false,
		    devMode: false,
		    onChange: function(fileDescriptors){
		    	for(var i in fileDescriptors){
//		    		console.log("ImageUploader.onChange");
//		    		console.log(fileDescriptors[i]);
		    		var ext = fileDescriptors[i].name.substring(fileDescriptors[i].name.lastIndexOf('.') + 1).toLowerCase();
		    		if(ext != 'jpg' && ext != 'jpeg' && ext != 'png' && ext != 'gif' && ext != 'bmp'){
		    			dialogManager.openDialog({
		    				title: "Unsupported Image File",
		    				content: "The type of file you selected is not supported, please select a valid image file",
		    				acknowledge: true
		    			});
		    			return;
		    		}
		    		else{
		    			_self.upload();
		    		}
		    	}
		    }
		};
		
		if(args.uploadUrl){
			fileUploaderArgs['uploadUrl'] = args.uploadUrl; 
		}
		_fileUploader = new dojox.form.FileUploader(fileUploaderArgs,browseButton);
		_fileUploader.startup();
		
		dojo.connect(_fileUploader, "onChange", function(dataArray){
//		    console.log("uploader onchange: " + dataArray.name);
//		    console.log(dataArray);
//		    _fileNameInput.attr("value",o.name);
//		    if(dataArray.length > 0){
//		    	_fileNameInput.attr("value",dataArray[0].name);
//		    }
		    if(args.onChange){
		    	args.onChange(dataArray);
		    }
		});
		dojo.connect(_fileUploader, "onProgress", function(dataArray){
//			console.log("uploader onProgress: ");
//		    console.log(dataArray);//name,bytesTotal,percent,size,type,bytesLoaded
			if(args.onProgress){
				args.onProgress(dataArray);
			}
		});
		dojo.connect(_fileUploader, "onComplete", function(dataArray){
//			console.log("uploader onComplete: ");
//		    console.log(dataArray); //file,name,type,height,width
//		    _fileNameInput.attr("value","");
			
			if(args.onComplete){
				args.onComplete(dataArray);
			}
		});

		//dojo.connect(dijit.byId("myUploadButton"), "onClick", function(){
		//    uploader.upload();
		//});
		
		initLayout();
	}
	init();
}
