dojo.require("dojo.date.locale");
dojo.require("dojo.io.script");
dojo.require("dojoui.Dialog");
dojo.require("dojoui.Button");

var __globalId = 0;
function generateId(){
	return ((++__globalId) + "");
}
function importJS(files,callback){
	var count = 0;
	var f = function(e){
		count++;
		if(count == files.length && callback){
			callback();
		}
	};
	for(var i = 0 ; i < files.length ; i++){
		dojo.io.script.get({
	        url: files[i],
	        load: f
	    });
	}
}

function JsonAjax(args){
	this._jsonAjaxRequestId = 0;
	this.inProcess = false;
	this.args = args;
}
JsonAjax.prototype.generateRequestId = function(){
	this._jsonAjaxRequestId++;
	return this._jsonAjaxRequestId;
};
//JsonAjax.prototype.onDoPostBegin = function(e){
//};
//JsonAjax.prototype.onDoPostComplete = function(response){
//};
//JsonAjax.prototype.onDoPostError = function(response){
//};

JsonAjax.prototype.onRequestBegin = function(e){
};
JsonAjax.prototype.onRequestComplete = function(response){
};
JsonAjax.prototype.onRequestError = function(response){
};

JsonAjax.prototype.doGet = function(command,jsonData,callback,exceptionCallback){
	//header.showWaitIcon(); //defined in header.tag
	this.onRequestBegin({commmand: command, jsonData: jsonData});
	var jsonAjaxRequestId = this.generateRequestId();
	var content = {content : dojo.toJson(jsonData), jsonAjaxRequestId: jsonAjaxRequestId};
	this.inProcess = true;
	var self = this;
	dojo.xhrGet({
	    url: ("service/" + command),
	    content: content,
	    handleAs: "json",
	    requestId: (jsonAjaxRequestId + ""),
	    error: function(m){
	    	console.log("main ajax.doGet Error: ");
	    	console.log(m);
	    },
	    handle: function(response,ioArgs){
			try{
				if(!response){
					window.location.href = "";
				}
				if(response.type == "exception"){
					if(typeof exceptionCallback == 'function'){
						console.log("ajax.doGet1 exception " + response);
						exceptionCallback(response);
					}
					else if(self.args.exceptionCallback){
						console.log("ajax.doGet2 exception " + response);
						self.args.exceptionCallback.call(self.args.objectContext,response);
					}
					else{
						console.log("ajax.doGet3 exception " + response);
						console.log(self.args);
					}
					self.onRequestError(response);
				}
				else if(response.type == "redirect"){
					window.location = response.url;
				}
				else{
					if(typeof callback != 'undefined'){
						callback(response);
						self.onRequestComplete(response);
					}
				}
			}
			catch(e){
				console.log(e);
			}
			self.inProcess = false;
	    } 
	});
};
JsonAjax.prototype.doPost = function(command,jsonData,callback,exceptionCallback){
	//header.showWaitIcon(); //defined in header.tag
	this.onRequestBegin({commmand: command, jsonData: jsonData});
	var jsonAjaxRequestId = this.generateRequestId();
	var content = {content : dojo.toJson(jsonData), jsonAjaxRequestId: jsonAjaxRequestId};
	this.inProcess = true;
	var self = this;
	dojo.xhrPost({
	    url: ("service/" + command),
	    content: content,
	    handleAs: "json",
	    requestId: (jsonAjaxRequestId + ""),
	    error: function(m){console.log("ajax.doPost Error: " + m);},
	    handle: function(response,ioArgs){
			try{
				if(response.type == "exception"){
					if(typeof exceptionCallback == 'function'){
						console.log("ajax.doPost1 exception " + response);
						exceptionCallback(response);
					}
					else if(self.args.exceptionCallback){
						console.log("ajax.doPost2 exception " + response);
						self.args.exceptionCallback.call(self.args.objectContext,response);
					}
					else{
						console.log("ajax.doPost3 exception " + response);
						console.log(self.args);
					}
					self.onRequestError(response);
				}
				else if(response.type == "redirect"){
					window.location = response.url;
				}
				else{
					if(typeof callback != 'undefined'){
						callback(response);
						self.onRequestComplete(response);
					}
				}
			}
			catch(e){
				console.log(e);
			}
			self.inProcess = false;
	    } 
	});
};
JsonAjax.prototype.doSynchronousPost = function(action,jsonData){
	var jsonAjaxRequestId = this.generateRequestId();
	var content = {content : dojo.toJson(jsonData), jsonAjaxRequestId: jsonAjaxRequestId};
	this.inProcess = true;
	var methodResponse = null;
	dojo.xhrPost({
	    url: ("service?command=" + action),
	    content: content,
	    handleAs: "json",
	    sync: true,
	    error: function(data){
			showExceptionDialog(data);
		},
	    load: function(response,ioArgs){
			try{
				if(response.type == "exception"){
					showExceptionDialog(response);
				}
				else{
					methodResponse = response;
				}
				_isIthis.inProcessalse;
			}
			catch(e){
				console.log(e);
			}
	    } 
	});
	return methodResponse;
};
//JsonAjax.prototype.downloadAttachement = function(attachmentId,byBookId){
//	var form = dojo.create("form");
//	if(!byBookId){ //download by attachment ID
//		form.action = "attachment_service?id=" + attachmentId;
//	}
//	form.method = "post";
//	document.body.appendChild(form);
//	form.submit();
//	document.body.removeChild(form);
//};

JsonAjax.prototype.downloadAttachement = function(attachmentId,params){
	var form = dojo.create("form");
	
	form.action = "attachment_service?id=" + attachmentId;
	if(params){
		for(var k in params){
			form.action += ("&" + k + "=" + params[k]);
		}
	}
	
	form.method = "post";
	document.body.appendChild(form);
	form.submit();
	document.body.removeChild(form);
};

function printObject(o){
	if(o.constructor == Array){
		for(var i = 0 ; i < o.length ; i++){
			var s = "";
			for(var k in o[i]){
				s += (k + ": " + o[i][k] + ", ");
			}
			console.log("["+s+"]");
		}
	}
	else{
		for(var key in o){
			console.log(key + ": " + o[key]);
		}
	}
}
//------------
var Constants = {
	Publication: {
		BookRole:{
			WRITING:{
				symbolIcon: "resources/images/writing_20x20.png",
				deficiencyLabel: "Writing",
				publisherRoleLabel: "Wrote"
			},
			ILLUSTRATING: {
				symbolIcon: "resources/images/illustrating_24x20.png",
				deficiencyLabel: "Illustration",
				publisherRoleLabel: "Illustrated"
			},
			TRANSLATING: {
				symbolIcon: "resources/images/translating_26x20.png",
				deficiencyLabel: "Translation",
				publisherRoleLabel: "Translated"
			},
			EDITING: {
				symbolIcon: "resources/images/editing_14x20.png",
				deficiencyLabel: "Editing",
				publisherRoleLabel: "Edited"
			},
			PROOF_READING: {
				symbolIcon: "resources/images/co-writing_21x20.png",
				deficiencyLabel: "Proof Read",
				publisherRoleLabel: "Proof-read"
			}
		}
	},
	RegularExpression: {
		EMAIL: "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}",
		ENGLISH_NAME: "[ a-zA-Z0-9._-]+",
		ENGLISH_TEXT: "[ a-zA-Z0-9._-]+",
		MULTILINGUAL_TEXT: "",
		MULTILINGUAL_NAME: "[^<>%$]+", //exclude %, $, <, >
		USER_NAME: "[a-zA-Z0-9._-]+"
	}
};

//---------------
function DialogManager(args){
	this.dialog = new dojoui.Dialog({
		id: generateId(),
		title: "Problem!",
		style: "height: 350px; width: 450;",
		content: ""
	});
}

DialogManager.prototype.showAlertMessage = function(a){
	if(!this.dialog){
		console.log("BranchItIp.prototype.showAlertMessage: dialog is undefined, check why?");
		return;
	}
	this.dialog.set("style","style: height: 350px; width: 450;");
	if(a.title){
		this.dialog.set("title",a.title);
	}
	else{
		this.dialog.set("title","Problem!");
	}
	if(a.content){
		this.dialog.set("content", a.content);
	}
	this.dialog.show();
};
DialogManager.prototype.closeDialog = function(){
	this.dialog.onCancel();
};
DialogManager.prototype.getDialog = function(){
	return this.dialog;
};
DialogManager.prototype.openDialog = function(a){
	if(!this.dialog){
		console.log("BranchItIp.prototype.showAlertMessage: dialog is undefined, check why?");
		return;
	}
	if(a.style){
		this.dialog.set("style",a.style);
	}
	else{
		this.dialog.set("style","style: height: 350px; width: 450px;");
	}
	if(a.title){
		this.dialog.set("title",a.title);
	}
	if(a.href){
		this.dialog.set("href",a.href);
	}
	
	if(a.content){
		this.dialog.set("content",a.content);
	}
//	console.log("---ddd:");
//	console.log(a);
	if(a.onOK){
		var d = this.dialog;
		var okButton = new dojoui.Button({
			id: generateId(),
			label: a.okLabel ? a.okLabel : "OK",
			onClick: function(){
				a.onOK();
				if(a.dismiss){
					d.onCancel();
				}
			}
		});
		var cancelButton = new dojoui.Button({
			id: generateId(),
			label: a.cancelLabel ? a.cancelLabel : "Cancel",
			onClick: function(){d.onCancel(); if(a.onCancel) a.onCancel();}
		});
		var div = dojo.create("div"); //bottom: 0;position: absolute;
		div.style.position = "absolute";
		div.style.bottom = 0;
		div.style.paddingBottom = "10px";
		div.appendChild(okButton.domNode);
		div.appendChild(cancelButton.domNode);
		this.dialog.containerNode.appendChild(div);
		
//		this.dialog.bottomPane = div;
	}
	else if(a.acknowledge){
		var d = this.dialog;
		var okButton = new dojoui.Button({
			id: generateId(),
			label: "OK",
			onClick: function(){
				d.onCancel();
				if(typeof a.acknowledge == "function"){
					a.acknowledge();
				}
			}
		});
		var div = dojo.create("div"); //bottom: 0;position: absolute;
		div.style.position = "absolute";
		div.style.bottom = 0;
		div.appendChild(okButton.domNode);
		this.dialog.containerNode.appendChild(div);
		
//		this.dialog.bottomPane = div;
	}
	
	this.dialog.show();
	return this.dialog;
//	this.dialog.containerNode.style.height = "100%"; //replace 'auto' value
};

DialogManager.prototype.popGenericException = function(a){
	console.log("main.js:popGenericException");
	console.log(a);
//	console.log(BranchItIp.prototype.showAlertMessage);
	console.log(this.showAlertMessage);
	console.log(this);
	this.showAlertMessage(a);
};

//-------------

function BranchItIp(args){
	//from within this.popGenericException the context is not BranchItIp but this following json object
	//this within json args = BranchItIp function
	this.jsonServlet = new JsonAjax({exceptionCallback: this.popGenericException, objectContext: this, aaa: "I am BranchItIp Function service args"});
	this.loaded = false;
//	this.dialog = null;
	this.serverProps = {};
//	this.dialog = new dojoui.Dialog({
//		id: generateId(),
//		title: "Problem!",
//		style: "height: 350px; width: 450;",
//		content: ""
//	});
}

BranchItIp.prototype.startup = function(){ //is called on index.js load
//	console.log("BranchItIp.prototype.startup");
//	console.log(branchitup.mainTabContainer);
//	this.dialog = new dojoui.Dialog({
//		id: generateId(),
//		title: "Problem!",
//		style: "height: 350px; width: 450;"
//	});
//	
//	this.mainTabContainer = new MainTabContainer({});
};

BranchItIp.prototype.formatDateTime = function (date){
	return dojo.date.locale.format(date,{datePattern: "MMM dd, yyyy HH:mm:ss", selector: "date"});
};
BranchItIp.prototype.formatDate = function (date){
	return dojo.date.locale.format(date,{datePattern: "MMM dd, yyyy", selector: "date"});
};

BranchItIp.prototype.formatGmtMillisDate = function(gmtMillis){ //needs the offset
	return this.formatDateTime(new Date(gmtMillis));
};
BranchItIp.prototype.formatMillisToDate = function(millis){ //needs the offset
	return this.formatDateTime(new Date(millis));
};
BranchItIp.prototype.formatXmlDate=  function(xml){
	var d = new Date(xml.year,xml.month,xml.date,xml.hours,xml.minutes,xml.seconds);
	return this.formatDateTime(d);
};
BranchItIp.prototype.parseUrlParams = function(url){
	var o = null;
	if(!url) return null;
	var arr = url.split("?");
	if(arr.length > 1){
		var params = arr[1];
		//deal with string of the form: 'url("bla bla")'
		params = params.replace(")","");
		params = params.replace("(","");
		params = params.replace('"',"");
		o = dojo.queryToObject(params);
	}
	return o;
};
BranchItIp.prototype.getProperty = function(key){
	if(!key) return null;
	var value = this.serverProps[key];
	if(typeof value == 'undefined'){
//		console.log("main::: property " + key + " does not exists fetching value using synchronous call!")
		value = this.jsonServlet.doSynchronousPost("GetProperty",{key: key});
		if(value){
			this.serverProps[key] = value;
		}
		
	}
//	else{
//		console.log("main::: property " + key + " is " + value);
//	}
	return value;
};

BranchItIp.prototype.getImageUrl = function(imageFile){
	return "imageservice?id=" + imageFile.imageFileId;
};

BranchItIp.prototype.endsWith = function (str, end, ignoreCase) {
    if (ignoreCase) {
        str = str.toLowerCase();
        end = end.toLowerCase();
    }
    if ((str.length - end.length) < 0) {
        return false;
    }
    return str.lastIndexOf(end) == (str.length - end.length);
};
BranchItIp.prototype.includeCss = function(href){
     var head = dojo.query("head")[0];
     var styles = dojo.query("head link");
     var exists = false;
     for(var i = 0 ; i < styles.length ; i++){
    	 if(this.endsWith(styles[i].href,href)){
    		 exists = true;
    	 }
     }
     if(!exists){
    	 var element = document.createElement('link');
         element.type = 'text/css';
         element.rel = 'stylesheet';
         element.href = href;
         
         head.appendChild(element);
//         console.log("Adding a new CCS style sheet");
     }
};

BranchItIp.prototype.createTooltipLabel = function(labelText, tooltipText){
	var label = dojo.create("label",{innerHTML: labelText, "class": "tooltipLabel"});
	dojo.connect(label,"onmouseover",function(){
		dijit.showTooltip(tooltipText,label,null);
	});
	dojo.connect(label,"onmouseout",function(){
		dijit.hideTooltip(label);
	});
	
	return label;
};

BranchItIp.prototype.setDomStyle = function(dom,map){
	if(!dom || !map) return;
	for(var k in map){
		dom.style[k] = map[k];
	}
};

BranchItIp.prototype.authenticateUser = function(){
//	console.log("branchitup.authenticateUser");
//	console.log(useraccount);
	if(!useraccount){
		this.openDialog({title: "Login", content: "To complete these operation you must login first!"});
		throw "Unauthorised Exception";
//		window.location = "login.html";
	}
	else{
		
	}
};

BranchItIp.prototype.createImg = function(imageFile,width){
	var img;
	if(imageFile){
		var url = this.getImageUrl(imageFile);
		var ratio = 0;
		if(imageFile.width > 0){
			ratio = (width / imageFile.width);
		}
		var w = width;
		var h = imageFile.height * ratio;
		img = dojo.create("img",{src: url, style:"width: " + w + "px; height: " + h +"px;"});
	}
	else{
		img = dojo.create("img",{onload: function(){
			if(width){
				var ratio = 0;
				if(this.clientWidth > 0){
					ratio = (width / this.clientWidth);
				}
				else{ //IE
					var r = this.getBoundingClientRect();
					if(r){
						ratio = (width / (r.right - r.left));
					}
					
				}
				this.style.height = (parseInt(this.clientHeight * ratio) + "px");
				this.style.width = (width + "px");
			}
		}});
	}
	return img;
};

var branchitup = new BranchItIp({});
branchitup.startup();

var dialogManager;
dojo.addOnLoad(function(){
//	branchitup = new BranchItIp({}); //on load because we are initializing a dojo Dialog
//	branchitup.startup();
	dialogManager = new DialogManager({});
});
