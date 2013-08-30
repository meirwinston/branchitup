dojo.require("dijit.form.ValidationTextBox"); //required to avoid dijit.showTooltip is not a function
dojo.require("dijit.layout.ContentPane");
dojo.require("dijit.layout.BorderContainer");
dojo.require("dojoui.Editor");
dojo.require("dijit.form.Form");
dojo.require("dojo.data.ItemFileReadStore");
dojo.require("dijit.form.ComboBox");
dojo.require("dijit.form.CheckBox");

var oldVisibility = null;
var sheetId = null;
var permissions = null;

var allowEditingBlock;
var sheetNameInput = new dijit.form.ValidationTextBox({
	id: generateId(),
	value: "",
	required: true,
	style: "width: 400px",
	maxLength: 255,
	placeHolder: "Type in sheet name (up to 255 characters)",
	regExp: Constants.RegularExpression.MULTILINGUAL_NAME,
	invalidMessage: "Invalid Sheet Name"
});

//allows free text as well
var folderNameInput = new dijit.form.ComboBox({
	id: generateId(),
	style: "width: 400px;",
	required: true,
	placeHolder: "Type in folder name (up to 50 characters)",
	store : new dojo.data.ItemFileReadStore( {
		data : {
			identifier : "value",
			items : [{value: "Default", name: "Default"}]
		}
	}),
	value: "Default"
});

var visibilityInput = new dijit.form.FilteringSelect({
	style: "width: 100px;",
	required: true,
	store : new dojo.data.ItemFileReadStore( {
		data : {
			identifier : "value",
			items : [{value: "PUBLIC", name: "Public"}, {value: "PRIVATE", name: "Private"}]
		}
	}),
	value: "PRIVATE",
	onChange: function(){
//		console.log("visibility onchange " + this.value);
		if(this.value == "PRIVATE"){
			allowEditingBlock.style.visibility = 'hidden';
		}
		else{
			allowEditingBlock.style.visibility = 'visible';
		}
	}
});

var allowEditingInput = new dijit.form.CheckBox({
	name: "allowEditing",
	checked: true
});

var sheetForm = dijit.form.Form({
	method: "post"
//	action: "service/NewSheet"
});

var submitButton = new dijit.form.Button({
	label: "<b>Save</b>",
	onClick: function(){
		if(sheetId){
			updateSheet();
		}
		else{
			newSheet();
		}
	}
});

var cancelButton = new dijit.form.Button({
	label: "Cancel",
	onClick: toggleForm
});

function onHeaderUploadImageClick(){
//	console.log("onHeaderUploadImageClick::::");
	toggleImagesPane();
}

function setFolders(folders){
	if(folders.length < 0) return;
	if(folders.length == 0){
		folders = ["Default"];
	}
	var arr = [];
//	console.log("setFolders");
//	console.log(folders);
	for(var i in folders){
		if(folders[i]){
			arr.push({value: folders[i], name: folders[i]});
		}
	}
	if(arr.length == 0) return;
	var store = new dojo.data.ItemFileReadStore( {
		data : {
			identifier : "value",
			items : arr
		}
	});
	
	var prevValue = folderNameInput.get("value");
//	console.log("prevValue " + prevValue);
	var prevValueExists = false;
	for(var i in arr){
		if(arr[i].value == prevValue){
			prevValueExists = true;
			break;
		}
	}
//	console.log("prevValueExsits " + prevValueExists);
	folderNameInput.set("store",store);
	if(prevValueExists){
		folderNameInput.set("value",prevValue);
	}
	else{
		folderNameInput.set("value",arr[0].value);
	}
}

function fetchFolders(sheetId){
	branchitup.jsonServlet.doGet("GetUserFolders",{sheetId: sheetId},function(response){
//		console.log("GetUserFolders:response:");
//		console.log(response);
		setFolders(response.folders);
	});	
}

function createFormLayout(){
	var t = dojo.create("table");
	var td,tr;
	
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.colSpan = 2;
	td.className = "blueHeading3";
	td.id = "sheeteditor.formTitleDom";
	
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.className = "blueHeading4";
	td.innerHTML = "Name: ";
	td = tr.insertCell(tr.cells.length);
	td.appendChild(sheetNameInput.domNode);
	
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.className = "blueHeading4";
	td.innerHTML = "Folder Name: ";
	td = tr.insertCell(tr.cells.length);
	td.appendChild(folderNameInput.domNode);
	
//	tr = t.insertRow(t.rows.length);
//	td = tr.insertCell(tr.cells.length);
//	td.className = "blueHeading4";
//	td.innerHTML = "Visibility: ";
//	td = tr.insertCell(tr.cells.length);
//	td.appendChild(visibilityInput.domNode);
//	td.appendChild(dojo.create("label",{style: "padding-left: 10px; color: grey;", innerHTML: "<small>Making your sheets public allows others to use your sheets in their own books.  If you plan on compiling your sheets as part of your book you should keep the sheets as private.</small>"}));
	allowEditingBlock = dojo.create("span",{id: "sheeteditor.allowEditingBlock"});
	allowEditingBlock.style.paddingLeft = "20px";
	allowEditingBlock.style.visibility = 'hidden';
	allowEditingBlock.appendChild(allowEditingInput.domNode);
	allowEditingBlock.appendChild(dojo.create("label",{id: "sheeteditor.allowEditingLabel", innerHTML: "Allow Editing"}));
//	td.appendChild(allowEditingBlock);
	
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.className = "blueHeading4";
	td = tr.insertCell(tr.cells.length);
	td.appendChild(submitButton.domNode);
	td.appendChild(cancelButton.domNode);
	return t;
}

sheetForm.domNode.appendChild(createFormLayout());

var sheetEditor = new dojoui.Editor({
	id: "sheetEditor.editor",
	style: "font-family: arial;"
	/*'class': "branchitupSheetEditor"*/
});

var borderContainer = new dijit.layout.BorderContainer({
	id: "sheeteditor.borderContainer",
//	style: ("height: " + (window.screen.height - 200) + "px; width: 100%;"),
	design: "headline", //sidebar,headline
	gutters: false
});

var imagesContainer = new ImagesContainer({
	onInsert: function(e){
//		console.log("sheet-editor.onInsert " + e.target.getAttribute("url"));
		var html = "<img src='" + e.target.getAttribute("url") + "' />";
		//generates an error, if trying to add a node after hebrew text
		sheetEditor.execCommand("inserthtml",html);
		
	},
	onClose: function(e){
		toggleImagesPane();
	},
	onUploadComplete: function(){
		//rendering issue only on the first image
		//the container has to be resized.
		if(imagesContainer.countVisibleImages() <= 1){
			borderContainer.layout();
		}
	}
});

var d = dojo.create("div",{innerHTML: "&nbsp;", style: "width: 210mm;"});
document.body.appendChild(d);
var editorWidth = d.clientWidth;
//console.log("The width is " + editorWidth);
document.body.removeChild(d);

//var dd = dojo.create("span",{id:"sheeteditor.editorContainer"});
//dd.appendChild(sheetEditor.domNode);
var editorPane = new dijit.layout.ContentPane({
	id: "sheeteditor.editorPane",
	region: "center",
	content: sheetEditor
});

var rightPane = new dijit.layout.ContentPane({
	id: "sheeteditor.rightPane",
	region: "right",
	style: ("width: " + ((window.innerWidth - editorWidth)/2) + "px;"),
	content: "&nbsp;"
});

var leftPane = new dijit.layout.ContentPane({
	id: "sheeteditor.leftPane",
	region: "left",
	style: ("width: " + ((window.innerWidth - editorWidth)/2) + "px;"),
	content: "&nbsp;"
});

var imagesPane = new dijit.layout.ContentPane({
	id: "sheeteditor.imagesPane",
	region: "top",
	splitter: true,
	/*style: "height: 165px",*/
	content: imagesContainer
});

var formPane = new dijit.layout.ContentPane({
	id: "sheeteditor.formPane",
	region: 'top',
	style: "height: 180px",
	splitter: false,
	content: sheetForm
});
sheetEditor.insertAction({
	id: generateId(),
	label: "<img src='resources/images/photo_scenery_20x20.png' />",
	style: "color: blue;",
	title: "Upload Images...",
//		onClick: toggleForm
	onClick: toggleImagesPane
},0);

sheetEditor.insertAction({
	id: generateId(),
	label: "<img src='resources/images/disk_blue20x20.png' />",
	style: "color: blue;",
	title: "Save Sheet...",
	onClick: toggleForm
},0);

function toJsonSheet(){
	var o = {
		name: sheetNameInput.get("value"),
		folderName: folderNameInput.get("value"),
		visibility: visibilityInput.get("value"),
		allowEditing: allowEditingInput.get("checked")
	};
	if(sheetId){
		o['sheetId'] = sheetId;
	}
	var content = sheetEditor.getValue();
	if(content != ""){
		o.content = content;
	}
	var body = this.sheetEditor.getBody();
	o['cssText'] = body.style.cssText;
	
//	//--image file ids
//	var hash = {};
//	var imgTags = dojo.query("img[src^=imageservice]",body); //an img element whose "src" attribute value begins exactly with the string "imageservice"
//	for(var i = 0 ; i < imgTags.length ; i++){
//		var params = yblob.parseUrlParams(imgTags[i].src);
//		if(params.id){
//			hash[params.id] = true;
//		}
//	}
//	
//	o.imageFileIds = [];
//	for(var k in hash){
//		o.imageFileIds.push(k);
//	}
	return o;
};

function validateForm(){
	var messages = [];
	if(!sheetForm.isValid()){
		messages.push("Form has invalid input");
	}
	var v;
	v = sheetNameInput.get("value");
	if(!v || v == ""){
		messages.push("Name cannot be empty");
	}
	v = folderNameInput.get("value");
	if(!v || v == ""){
//		folderNameInput.set("value","Default");
	}
	v = visibilityInput.get("value");
	if(!v || v == ""){
		messages.push("Visibility cannot be empty");
	}
	else{
		if(v == "PRIVATE"){
			if(oldVisibility && oldVisibility == "PUBLIC"){
				messages.push("Public sheet cannot be set to Private");
			}
		}
	}
	v = sheetEditor.getValue();
	//65535 is the total bytes of type 'text' in mysql
	//UTF-8 requires 3 bytes for each character
	//65535/3=21845
//	if(v != null && v.length > 21845){
//		messages.push("The sheet's content exceeded the limit of 21845 characters, try breaking down the content into several sheets.");
//	}
	if(messages.length > 0){
		throw messages;
	}
}

function newSheet(){
	try{
		validateForm();
	}
	catch(messages){
		var ul = dojo.create("ul");
		for(var i in messages){
			ul.appendChild(dojo.create("li",{innerHTML: messages[i]}));
		}
		dialogManager.openDialog({
			title: "Invalid Input",
			content: ul,
			acknowledge: true
		});
		return;
	}
	var args = toJsonSheet();
//	console.log(args);
//	return;
	branchitup.jsonServlet.doPost("NewSheet",args,function(response){
//		console.log("newSheet:1:response");
//		console.log(response);
		if(response.sheetId){
			sheetId = response.sheetId;
		}
		else if(response.type == 'exception'){
			dialogManager.openDialog({
				title: "Saving Problem",
				style: "height: 200px;width: 300px;",
				content: "The Sheet could not be saved. Please try again!",
				acknowledge: true
			});
		}
		toggleForm();
	},function(response){
		console.log(response);
		
		dialogManager.openDialog({
			title: "Saving Problem",
			style: "height: 200px; width: 300px;",
			content: response.message,
			acknowledge: true
		});
	});
}

function updateSheet(){
//	console.log("updateSheet");
	try{
		validateForm();
	}
	catch(messages){
		var ul = dojo.create("ul");
		for(var i in messages){
			ul.appendChild(dojo.create("li",{innerHTML: messages[i]}));
		}
		dialogManager.openDialog({
			title: "Invalid Input",
			content: ul,
			acknowledge: true
		});
		return;
	}
	var args = toJsonSheet();
//	console.log(args);
	branchitup.jsonServlet.doPost("UpdateSheet",args,function(response){
//		console.log("UpdateSheet:1:response");
//		console.log(response);
		toggleForm();
	},function(response){
//		console.log("sheet-editor.js");
//		console.log(response);
		dialogManager.openDialog({
			title: response.title,
			content: response.message,
			acknowledge: true
		});
	});
}
function toggleImagesPane(){
	var tabs = borderContainer.getChildren();
	var tab = null;
	for(var i = 0 ; i < tabs.length ; i++){
		if(tabs[i] == imagesPane){
			tab = tabs[i];
			break;
		}
	}
	if(tab == null){
		borderContainer.removeChild(formPane);
		borderContainer.addChild(imagesPane);
		imagesPane.set("style","height: 'auto'");
//		console.log("calling imagesPane.startup");
//		imagesPane.startup();
	}
	else{
		borderContainer.removeChild(imagesPane);
	}
}
function toggleForm(){
	var tabs = borderContainer.getChildren();
	var tab = null;
	for(var i = 0 ; i < tabs.length ; i++){
		if(tabs[i] == formPane){
			tab = tabs[i];
			break;
		}
	}
	if(tab == null){
		//if sheetId is null -> new sheet
		if(sheetId == null || (permissions && permissions.ownedByUser)){
			fetchFolders();
		}
		else{
			if(sheetId){
				if(submitButton.get("disabled")){
					dialogManager.openDialog({
						title: "Edits are not permitted",
						content: "The owner of this sheet does not permit edits.",
						acknowledge: true 
					});
					return;
				}
			}
//			folderNameInput.set("value",null);
		}
		borderContainer.removeChild(imagesPane);
		borderContainer.addChild(formPane);
		if(sheetId){
			dojo.byId("sheeteditor.formTitleDom").innerHTML = ("Update Sheet ID " + sheetId);
		}
		else{
			dojo.byId("sheeteditor.formTitleDom").innerHTML = ("New Sheet");
		}
		
	}
	else{
		borderContainer.removeChild(formPane);
	}
}
function init(){
	var centerPane = dojo.byId("sheeteditor.centerPane");
	centerPane.parentNode.replaceChild(borderContainer.domNode,centerPane);
	borderContainer.startup();
	
	borderContainer.addChild(leftPane);
	borderContainer.addChild(editorPane);
	borderContainer.addChild(rightPane);
	
	console.log("GGGG");
	console.log(permissions);
};

dojo.addOnLoad(init);