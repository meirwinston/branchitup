/*Deprecated - embedded inside Main.js*/
function JsonAjax(args){
	var _jsonAjaxRequestId = 0;
	
	function generateRequestId(){
		_jsonAjaxRequestId++;
		return _jsonAjaxRequestId;
	}
	this.doPost = function(command,jsonData,callback){
		var jsonAjaxRequestId = generateRequestId();
		var content = {content : dojo.toJson(jsonData), jsonAjaxRequestId: jsonAjaxRequestId};
//		var aaa = {title: "meir", myarray: {key1: [5,4,3], key2: "momo"}};
		
		dojo.xhrPost({
		    url: ("service?command=" + command),
		    content: content,
		    handleAs: "json",
		    requestId: (jsonAjaxRequestId + ""),
		    error: function(m){console.log("ajax.doPost Error: " + m);},
		    handle: function(response,ioArgs){
				try{
					if(response.type == "exception"){
						console.log("ajax.doPost exception " + response);
//						if(response.title == "AnonymousUser"){
//							window.location = "login.jsp";
//							return;
//						}
//						if(typeof exceptionCallback != 'undefined'){
//							exceptionCallback(response);
//						}
//						else{
//							showExceptionDialog(response);
//						}
					}
					else{
						if(typeof callback != 'undefined'){
//							callback.apply(this,[response]);
							callback(response);
						}
					}
				}
				catch(e){
					console.log(e);
				}
		    } 
		});
	};
	
	this.doSynchronousPost = function(action,jsonData){
		var jsonAjaxRequestId = generateRequestId();
		var content = {content : dojo.toJson(jsonData), jsonAjaxRequestId: jsonAjaxRequestId};
		_isInProcess = true;
		var methodResponse = null;
		dojo.xhrPost({
		    url: (_url + "?command=" + action),
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
					_isInProcess = false;
				}
				catch(e){
					console.log(e);
				}
		    } 
		});
		return methodResponse;
	};
	
	this.downloadAttachement = function(command, o){
		var input = dojo.create("input");
		input.type = "hidden";
		input.name = "content";
		input.value = dojo.toJson(o);
		
		var form = dojo.create("form");
		form.action = "service?command=" + command;
		form.method = "post";
		
		form.appendChild(input);
		
		document.body.appendChild(form);
		form.submit();
		document.body.removeChild(form);
	};
	
//	this.downloadAttachement = function(command, o){
//		console.log("downloadAttachement: " + JSON.stringify(o));
//		var input = $("<input type='hidden' name='content' />").attr("value", JSON.stringify(o));
//		var form = $('<form action="service?command='+ command +'" method="post"></form>').append(input);
//		form.appendTo('body').submit().remove();
//	};
	
	function init(){
		
	}
	init();
}

//function JsonAjax(args){
//	var _jsonAjaxRequestId = 0;
//	this.doPost = function(command,jsonData,callback){
//		$.ajax({
//			type: 'POST',
//			data: {content: dojo.toJson(jsonData)},
//			success: callback,
//			url: 'service?command=' + command
//		});
//	};
//	function generateRequestId(){
//		_jsonAjaxRequestId++;
//		return _jsonAjaxRequestId;
//	}
//	this.doPost_ = function(command,jsonData,callback){
//		var jsonAjaxRequestId = generateRequestId();
//		var content = {content : dojo.toJson(jsonData), jsonAjaxRequestId: jsonAjaxRequestId};
////		var aaa = {title: "meir", myarray: {key1: [5,4,3], key2: "momo"}};
//		
//		dojo.xhrPost({
//		    url: ("service?command=" + command),
//		    content: content,
//		    handleAs: "json",
//		    requestId: (jsonAjaxRequestId + ""),
//		    error: function(m){console.log("ajax.doPost Error: " + m);},
//		    handle: function(response,ioArgs){
//				try{
//					if(response.type == "exception"){
//						console.log("ajax.doPost exception " + response);
////						if(response.title == "AnonymousUser"){
////							window.location = "login.jsp";
////							return;
////						}
////						if(typeof exceptionCallback != 'undefined'){
////							exceptionCallback(response);
////						}
////						else{
////							showExceptionDialog(response);
////						}
//					}
//					else{
//						if(typeof callback != 'undefined'){
////							callback.apply(this,[response]);
//							callback(response);
//						}
//					}
//				}
//				catch(e){
//					console.log(e);
//				}
//		    } 
//		});
//	};
//	
//	this.doGet = function(command,jsonData,callback){
//		$.ajax({
//			type: 'GET',
//			data: {content: JSON.stringify(jsonData)},
//			success: callback,
//			url: 'service?command=' + command
//		});
//	};
//	
////	this.doPostBinary = function(command,jsonData,callback){
////		$.ajax({
////			type: 'POST',
////			data: {content: JSON.stringify(jsonData)},
////			dataType: 'json',
////			success: function(response){
////				window.location = "BytesServlet?action=getAttachment&attachmentId="+jsonData.attachmentId;
////				
////				if(callback){
////					callback(response);
////				}
////			},
////			url: 'service?command=' + command
////		});
////	};
//	
//	this.downloadAttachement = function(command, o){
//		console.log("downloadAttachement: " + JSON.stringify(o));
//		var input = $("<input type='hidden' name='content' />").attr("value", JSON.stringify(o));
//		var form = $('<form action="service?command='+ command +'" method="post"></form>').append(input);
//		form.appendTo('body').submit().remove();
//	};
//	
//	function onError(shr,status,error){
//		console.log("JsonAjax:AJAX Error:");
//		console.log(shr);
//		console.log(status);
//		console.log(error);
//	}
//	
//	function init(){
//		$.ajaxSetup({
//			cache: true,
//			dataType: 'json',
//			error: onError,
//			timeout: 6000, //timeout of 60 seconds
//			url: 'service'
//		});
//	}
//	init();
//}