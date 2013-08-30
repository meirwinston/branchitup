dojo.require("dojoui.Pagination");
//dojo.require("dojoui.UploadButton");

function ImagesContainer(args){
	var _self = this;
	var albumsMap = {"Default": true};
//	var uploaderArgs = {
//		url: "service/FileUpload?params={\"method\":\"html5\",\"album\":\"Default\"}",
//		label: "Upload Image...",
////		style: "width: 60px; height: 22px",
////		force: "html",
////		folder: "DEFAULT",
//		uploadType: "html5",
//		onBegin: function(){
////			this.url = "upload/FileUpload";
//			console.log("***ImageList.initUploader.onBegin::");
//		},
//		onComplete: function(params){ //these params are determined in upload/FileUpload
////			console.log("***ImageList.initUploader.onComplete::"+this.uploadType + ", " + params);
////			console.log(params); 
////			printObject(params)
//			_self.scroll(0);
//			if(params){ //upload/FileUpload generates this object
//				if(this.uploadType == "flash"){
//					var normalizedParams = [];
//					for(var i = 0 ; i < params.length ; i++){
//						var tmp = {};
//						var additionalParams = params[i].additionalParams;
//						delete params[i].additionalParams;
//						for(var k in params[i]){
//							tmp[k] = params[i][k];
//							
//						}
//						for(var k in additionalParams){
//							tmp[k] = additionalParams[k];
//						}
//						normalizedParams.push(tmp);
//					}
//					params = normalizedParams;
//				}
//				
//				for(var i in params){
////						console.log("ImageList::");
////						console.log(params[i]);
//					if(params[i].folderName == "USER_DEFAULT"){
//						self.prependItem(params[i]);
//					}
//				}
//			}
//			else{
//				var a = {
//					title: "Problem On File Upload!",
//					content: "There was a problem uploading the image, please try again in few minutes. If this problem persists, please report a bug!<br/><br/> Thanks, we appologize for the inconvinience.",
//					acknowledge: true
//				};
//				dialogManager.openDialog(a);
//			}
//		}
//	};
//	
//	if(dojo.isIE >= 7){
//		console.log("uploadArgs:" + dojo.isIE);
//		uploaderArgs.url = "service/FileUpload?params={\"method\": \"flash\",\"album\":\"Default\"}";
//		uploaderArgs.uploadType = "flash";
//	}
////	
//	var uploadButton = new dojoui.UploadButton(uploaderArgs);
//	console.log("uploadButton");
//	console.log(uploadButton);
	var closeButton = new dijit.form.Button({
		label: "Close",
		onClick: args.onClose
	});
	
	var uploadButton = new ImageUploader({
		onChange: function(){
//			console.log("ImagesContainer.onchange");
			if(args.onChange){
				args.onChange(arguments);
			}
		},
		onProgress: function(arr){
			if(args.onProgress){
				args.onProgress(arr);
			}
		},
		onComplete: function(){
			populateAlbums();
			_self.scroll(0);
			
//			if(args.onUploadComplete){
//				args.onUploadComplete();
//			}
		}
	});
	
	var albumInput = new dijit.form.ComboBox({
		style: "width: 200px;",
		required: true,
		store : new dojo.data.ItemFileReadStore( {
			data : {
				identifier : "value",
				items : [{value: "Default", name: "Default"}]
			}
		}),
		value: "Default",
		onChange: function(){
//			console.log("album onchange" + this.value);
			//uploadButton.set("url","service/FileUpload?params={\"method\":\"html5\",\"album\":\"" + this.value + "\"}");
			uploadButton.setUploadUrl("service/FileUpload?params={\"method\":\"html5\",\"album\":\"" + this.value + "\"}");
			_self.scroll(0);
		}
	});
	
	var pagination = new dojoui.Pagination({
		id: "imagescontainer.pagination",
		pageSize: 10,
		classOfNode: "pagination",
		onBrowse: function(offset, pageSize){
			_self.scroll(offset);
		},
		classOfSelectedLabel: "paginationSelectedLabel",
		classOfUnselectedLabel: "paginationUnselectedLabel",
		classNextBack: "paginationNextBack"
	});
	pagination.domNode.id = "imagescontainer.pagination";
	
	function populateAlbums(){
		branchitup.jsonServlet.doGet("GetAllUserAlbums",args,function(response){
			var arr = [];
			for(var album in albumsMap){
				arr.push({value: album, name: album});
			}
//			var map = {"Default": true}; //avoid duplicates
//			for(var i in response.albums){
//				if(!map[response.albums[i]]){
//					arr.push({value: response.albums[i], name: response.albums[i]});
//					map[response.albums[i]] = true;
//				}
//			}
			albumInput.set("store",new dojo.data.ItemFileReadStore( {
				data : {
					identifier : "value",
					items : arr
				}
			}));
		});
	}
	
	var imagesDiv = dojo.create("div",{id: "imagescontainer.imagesDiv"});
	var controlsDom = dojo.create("table",{id: "imagescontainer.controlsDom", border: "0"});
	controlsDom.style.width = "100%";
	var tr = controlsDom.insertRow(controlsDom.rows.length); 
	var td = tr.insertCell(tr.cells.length);
	
	this.domNode = dojo.create("div",{id: "imagescontainer"});
	this.domNode.appendChild(imagesDiv);
	this.domNode.appendChild(controlsDom);
	
	td.appendChild(uploadButton.domNode);
	//Style kludge
	td.style.width = "160px";
	uploadButton.domNode.className = "";
	uploadButton.domNode.style.width = "160px";
	uploadButton.domNode.style.height = "22px";
	//
	uploadButton.domNode.childNodes[0].style.width = "";
	uploadButton.domNode.childNodes[0].style.height = "";
	
//	uploadButton.domNode.childNodes[1].style.width = "150px";
////	uploadButton.domNode.childNodes[1].style.position = "";
//	uploadButton.domNode.childNodes[1].style.cursor = "pointer";
//	uploadButton.domNode.childNodes[1].className = "dijitReset dijitInline dijitButtonNode";	
//	uploadButton.domNode.childNodes[2].style.cursor = "pointer";
	
	td = tr.insertCell(tr.cells.length);
	td.appendChild(dojo.create("span",{innerHTML: "Album:"}));
	td.appendChild(albumInput.domNode);
	td.appendChild(closeButton.domNode);
	
	td = tr.insertCell(tr.cells.length);
	td.appendChild(pagination.domNode);
	
	tr = controlsDom.insertRow(controlsDom.rows.length); 
	td = tr.insertCell(tr.cells.length);
	td.colSpan = 3;
	td.innerHTML = "<label style='color: grey;'>Upload an image from your computer: JPG, PNG, GIF or BMP • 10MB file limit</label>";
	
	this.setAlbums = function(albums){
//		console.log("setAlbums");
//		console.log(albums);
		var arr = [];
		for(var i in albums){
			arr.push({value: albums[i], name: albums[i]});
			albumsMap[albums[i]] = true;
		}
		var store = new dojo.data.ItemFileReadStore( {
			data : {
				identifier : "value",
				items : arr
			}
		});
		albumInput.set("store",store);
	};
	
	this.countVisibleImages = function(){
		return imagesDiv.childNodes.length;
	};
	
	this.setItems = function(items){
		if(items && items.length == 0){
			imagesDiv.innerHTML = "<span class='noItemsHeading'>No Items Found</span>";
			return;
		}
		else{
			imagesDiv.innerHTML = "";			
		}
		var d;
		var thumbnailsLoadedCount = 0;
		for(var i in items){
//			d = dojo.create("img",{src: items[i].thumbnailUrl, onclick: args.onInsert});
//			d.setAttribute("url",items[i].url);
//			d.style.cursor = "pointer";
//			imagesDiv.appendChild(d);
			
			d = dojo.create("span",{name: "imageContainer"});
			d.appendChild(dojo.create("img",{src: items[i].thumbnailUrl, onload: function(){
				thumbnailsLoadedCount++;
				if(thumbnailsLoadedCount == items.length){
//					console.log("ALL Thumbnail loaded here 100%");
					if(args.onUploadComplete){
						args.onUploadComplete();
					}
				}
			}}));
			d.appendChild(dojo.create("div",{name: "imageInsertLink", innerHTML: "Insert", onclick: args.onInsert, "class": "blueHeading4", "url": items[i].url}));
			imagesDiv.appendChild(d);
		}
	};
	
	this.scroll = function(offset){
//		var args = {offset: offset, maxResults: pagination.pageSize};
		if(albumInput.get("value") == ""){
			albumInput.set("value","Default");
		}
		var args = {offset: offset, maxResults: pagination.pageSize, album: albumInput.get("value")};
		var self = this;
		branchitup.jsonServlet.doGet("ScrollUserImageFiles",args,function(response){
//			console.log("---->ScrollUserImageFiles.response");
//			console.log(response);
			self.setItems(response.list);
			pagination.set("totalResults",response.count);
			pagination.refresh();
			albumsMap[args.album] = true;
		});
	};
	
	this.startup = function(){
	};
	
	this.scroll(0);
}
