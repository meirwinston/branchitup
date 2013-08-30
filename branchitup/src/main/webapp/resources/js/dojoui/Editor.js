if(!dojo._hasResource["dojoui.Editor"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.Editor"] = true;
dojo.provide("dojoui.Editor");
dojo.require("dojoui.Button");
dojo.require("dijit.Editor");
dojo.require("dijit._editor.plugins.FontChoice"); // 'fontName','fontSize','formatBlock'
dojo.require("dijit._editor.plugins.TextColor");
dojo.require("dijit._editor.plugins.ViewSource");

//dojo.require("dijit._editor.plugins.LinkDialog");

//--

//dojo.require("dojoui.DropDownButton");
//dojo.require("dijit.TooltipDialog");
//dojo.require("dijit.form.TextBox");
//dojo.require("dijit.form.Button");
//dojo.require("dijit.form.Form");
//dojo.require("dojox.form.FileUploader");
/*
"Arial", 
"Helvetica", 
"sans-serif";
"Arial Black", 
"Gadget", 
"Bookman Old Style", 
"serif"
"Comic Sans MS", 
"cursive",
"Courier",
"monospace",
"Courier New"
"Garamond"
"Georgia"
"Impact", 
"Charcoal",
"Lucida Console", 
"Monaco", 
"monospace",
"Lucida Sans Unicode", 
"Lucida Grande", 
"MS Sans Serif", 
"Geneva",
"MS Serif", 
"New York",
"Palatino Linotype", 
"Book Antiqua", 
"Palatino", 
"Symbol",
"Tahoma",
"Times New Roman",
"Times",
"Trebuchet MS",
"Helvetica",
"Verdana",
"Webdings",
"Wingdings", 
"Zapf Dingbats"


 */
dojo.declare("dojoui.Editor", dijit.Editor, {
	xhtml: true, //it has no effect
	postCreate: function(){
		//this.extraPlugins = ['foreColor','hiliteColor','createLink',{name:'dijit._editor.plugins.FontChoice', command:'fontName', generic:true}];
		var supportedFonts = [
//		                      "Arial",
//		                      "Arial Black",
//		                      "Times New Roman",
//		                      "Comic Sans MS","Courier",
//		                      "Courier New", 
//		                      "Cursive",
//		                      "Helvetica",
//		                      "Impact"
"Arial", 
"Helvetica", 
"sans-serif",
"Arial Black", 
"Gadget", 
"Bookman Old Style", 
"serif",
"Comic Sans MS", 
"cursive",
"Courier",
"monospace",
"Courier New",
"Garamond",
"Georgia",
"Impact", 
"Charcoal",
"Lucida Console", 
"Monaco", 
"monospace",
"Lucida Sans Unicode", 
"Lucida Grande", 
"MS Sans Serif", 
"Geneva",
"MS Serif", 
"New York",
"Palatino Linotype", 
"Book Antiqua", 
"Palatino", 
"Symbol",
"Tahoma",
"Times New Roman",
"Times",
"Trebuchet MS",
"Helvetica",
"Verdana",
"Webdings",
"Wingdings", 
"Zapf Dingbats"
		                      ];
		this.extraPlugins = [
		                     'foreColor',
		                     'hiliteColor',
		                     'viewsource',
		                     {name:'dijit._editor.plugins.FontChoice', command:'fontName', generic:false, values: supportedFonts},
		                     {name:'dijit._editor.plugins.FontChoice', command:'fontSize', generic:false, values: [1,2,3,4,5,6,7]}/*,
		                     {name:'dijit._editor.plugins.FontChoice', command:'formatBlock', plainText: false}*/];
		this.inherited(arguments);
	},
//	getValue: function(){
//		return this.getBody().innerHTML;
//	},
	setDisabled: function(boolean){
		this.attr("disabled",boolean);
	},
	setReadOnly: function(boolean){
		this.attr("readOnly",boolean);//to allow a tooltip to be shown
	},
	connectOnClick: function(f){
		return dojo.connect(this, 'onClick', f);
	},
	getToolbar: function(){
		return this.toolbar;
	},
	getDocument: function(){
		var doc = this.editingArea.firstChild.contentDocument;
		if(!doc){ //IE
			doc = this.editingArea.firstChild.contentWindow.document;
		}
		return doc;
	},
	getBody: function(){
		var b = this.editingArea.getElementsByTagName("iframe")[0];
		if(!b.contentDocument){ //for IE
			b = b.contentWindow.document.body;
		}
		else{
			b = b.contentDocument.body;
		}
		return b;
	},
	insertAction: function(o,place){ //o.label, o.onClick, o.style, o.id
		var button = new dojoui.Button(o);
		if(typeof place != 'undefined'){
			this.toolbar.addChild(button,place);
		}
		else{
			this.toolbar.addChild(button);
		}
		return button;
	},
	insertButton: function(button,place){ 
		if(typeof place != 'undefined'){
			this.toolbar.addChild(button,place);
		}
		else{
			this.toolbar.addChild(button);
		}
	}
//	insertUploadImageAction: function(o){
//		var self = this;
//		var uploadFileNameInput = new dijit.form.ValidationTextBox({
//			required: "true",
//			invalidMessage: "Invalid File Name"
//		});
//		this.uploadForm = new dijit.form.Form({
//			method: "post",
//			enctype: "multipart/form-data"
//		});
//		var browseButton = dojo.create("span");
//		browseButton.innerHTML = "Select Image...";
//		document.body.appendChild(browseButton);
//		
//		
//		var uploadFileMask = [
//		    ["Jpeg File","*.jpg;*.jpeg"],
//		    ["GIF File","*.gif"],
//		    ["PNG File","*.png"],
//		    ["All Images","*.jpg;*.jpeg;*.gif;*.png"],
//		];
//
//		var fileUploader = new dojox.form.FileUploader({
//		    fileMask: uploadFileMask,
//		  //  htmlFieldName: "myhtmlFieldName",
//		    force: "html",
//		    uploadUrl: o.uploadUrl,
//		    selectMultipleFiles: false,
//		    serverTimeout: 10000, //in milliseconds
//		    showProgress: true,
//		    isDebug:true,
//		    devMode: true
//		},browseButton);
//		
////		dojo.connect(fileUploader, "onChange", function(dataArray){
////		    console.log("uploader onchange: " + dataArray.name);
////		    console.log(dataArray);
//////		    _fileNameInput.attr("value",o.name);
////		    if(dataArray.length > 0){
////		    	uploadFileNameInput.attr("value",dataArray[0].name);
////		    }
////		    
////		});
////		dojo.connect(fileUploader, "onProgress", function(dataArray){
////			console.log("uploader onProgress: ");
////		    console.log(dataArray);//name,bytesTotal,percent,size,type,bytesLoaded
////		});
////		dojo.connect(fileUploader, "onComplete", function(dataArray){
////			console.log("uploader onComplete: ");
////		    console.log(dataArray); //file,name,type,height,width
////		    //fileNameInput.attr("value","");
////		});
//		
//		var uploadButton = new dijit.form.Button({
//			label: "Upload",
//			onClick: function(){
//				self.insertImageButton.forceClose();
////				fileUploader.upload();
//				if(self.onUploadImage){
//					var val = this.attr("value") + "<img src='resources/images/disk_blue20x20.png' />"
//					this.attr("value",val);
//					self.onUploadImage();
//				}
//			}
//		});
//		//-- Layout
//		
//		var t = dojo.create("table");
//		t.border = "0px";
//		t.cellPadding = "0";
//		t.cellSpacing = "0";
//		
//		var rowIndex = 0;
//		var row = t.insertRow(rowIndex++);
//		var cell = row.insertCell(0);
//		cell.appendChild(uploadFileNameInput.domNode);
//		cell = row.insertCell(1);
////		cell.appendChild(_browseButton);
//		cell.appendChild(fileUploader.domNode);
//		
//		row = t.insertRow(rowIndex++);
//		cell = row.insertCell(0);
//		cell.colSpan = 2;
//		cell.appendChild(uploadButton.domNode);
//		
//		//--
//		
//		var dialog = new dijit.TooltipDialog({
//			content: t
//	    });
//	    this.insertImageButton = new dojoui.DropDownButton({
//	        label: "Image",
//	        dropDown: dialog,
//	        closeable: false
//	    });
//	    
////	    this.closeUploadImageDropDown = this.insertImageButton.closeDropDown;
////	    this.insertImageButton.closeDropDown = function(){
////	    	console.log("close dropdown");
////	    };
//	    
//	    dojo.connect(fileUploader, "onChange", function(dataArray){
//		    console.log("uploader onchange: " + dataArray.name);
//		    console.log(dataArray);
//		    if(dataArray.length > 0){
//		    	uploadFileNameInput.attr("value",dataArray[0].name);
//		    }
////		    printObject(insertImageButton);
//		});
//		dojo.connect(fileUploader, "onProgress", function(dataArray){
//			console.log("uploader onProgress: ");
//		    console.log(dataArray);//name,bytesTotal,percent,size,type,bytesLoaded
//		});
//		dojo.connect(fileUploader, "onComplete", function(dataArray){
//			console.log("uploader onComplete: ");
//		    console.log(dataArray); //file,name,type,height,width
//		    //fileNameInput.attr("value","");
//		});
//	    this.toolbar.addChild(this.insertImageButton);
//	}
});
}

/*
Abadi MT Condensed Light
Albertus Extra Bold
Albertus Medium
Antique Olive
Arial
Arial Black
Arial MT
Arial Narrow
Bazooka
Book Antiqua
Bookman Old Style
Boulder
Calisto MT
Calligrapher
Century Gothic
Century Schoolbook
Cezanne
CG Omega
CG Times
Charlesworth
Chaucer
Clarendon Condensed
Comic Sans MS
Copperplate Gothic Bold
Copperplate Gothic Light
Cornerstone
Coronet
Courier
Courier New
Cuckoo
Dauphin
Denmark
Fransiscan
Garamond
Geneva
Haettenschweiler
Heather
Helvetica
Herald
Impact
Jester
Letter Gothic
Lithograph
Lithograph Light
Long Island
Lucida Console
Lucida Handwriting
Lucida Sans
Lucida Sans Unicode
Marigold
Market
Matisse ITC
MS LineDraw
News GothicMT
OCR A Extended
Old Century
Pegasus
Pickwick
Poster
Pythagoras
Sceptre
Sherwood
Signboard
Socket
Steamer
Storybook
Subway
Tahoma
Technical
Teletype
Tempus Sans ITC
Times
Times New Roman
Times New Roman PS
Trebuchet MS
Tristan
Tubular
Unicorn
Univers
Univers Condensed
Vagabond
Verdana
Westminster 	Allegro
Amazone BT
AmerType Md BT
Arrus BT
Aurora Cn BT
AvantGarde Bk BT
AvantGarde Md BT
BankGothic Md BT
Benguiat Bk BT
BernhardFashion BT
BernhardMod BT
BinnerD
Bremen Bd BT
CaslonOpnface BT
Charter Bd BT
Charter BT
ChelthmITC Bk BT
CloisterBlack BT
CopperplGoth Bd BT
English 111 Vivace BT
EngraversGothic BT
Exotc350 Bd BT
Freefrm721 Blk BT
FrnkGothITC Bk BT
Futura Bk BT
Futura Lt BT
Futura Md BT
Futura ZBlk BT
FuturaBlack BT
Galliard BT
Geometr231 BT
Geometr231 Hv BT
Geometr231 Lt BT
GeoSlab 703 Lt BT
GeoSlab 703 XBd BT
GoudyHandtooled BT
GoudyOLSt BT
Humanst521 BT
Humanst 521 Cn BT
Humanst521 Lt BT
Incised901 Bd BT
Incised901 BT
Incised901 Lt BT
Informal011 BT
Kabel Bk BT
Kabel Ult BT
Kaufmann Bd BT
Kaufmann BT
Korinna BT
Lydian BT
Monotype Corsiva
NewsGoth BT
Onyx BT
OzHandicraft BT
PosterBodoni BT
PTBarnum BT
Ribbon131 Bd BT
Serifa BT
Serifa Th BT
ShelleyVolante BT
Souvenir Lt BT
Staccato222 BT
Swis721 BlkEx BT
Swiss911 XCm BT
TypoUpright BT
ZapfEllipt BT
ZapfHumnst BT
ZapfHumnst Dm BT
Zurich BlkEx BT
Zurich Ex BT 
 */