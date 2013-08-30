//dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.Menu");
dojo.require("dijit.MenuItem");
dojo.require("dijit.MenuSeparator");

function ItemsDropdown(args){ //equivalent to "var ItemsDropdown = function(args)"
	var self = this;
	this.args = args;
	
	this.menuContainer = dojo.create("div");
	this.menuContainerOuter = dojo.create("div",{'class': "dijitPopup dijitMenuPopup"});
	branchitup.setDomStyle(this.menuContainerOuter,{zIndex: "1000", position: "absolute", top: "0px", left: "200px"});
//	this.textbox = new dijit.form.TextBox({
//		id: generateId(),
//		style: "width: 300px;",
//		outerDropdown: self,
//		onKeyUp: self.onKeyUp,
//		placeHolder: args ? args.placeHolder : null
//	});
	
	var inputArgs = {
			id: generateId(),
			style: "width: 300px;",
			outerDropdown: self,
			onKeyUp: self.onKeyUp,
			placeHolder: args ? args.placeHolder : null	
	};
	if(args && args.inputArgs){
		for(var k in args.inputArgs){
			inputArgs[k] = args.inputArgs[k]; 
		}
	}
	this.textbox = new dijit.form.ValidationTextBox(inputArgs);
	

//	dojo.connect(this.textbox,"onKeyUp",this.onKeyUp);
	this.domNode = dojo.create("span",{"class": "tundra"});
	this.domNode.appendChild(this.textbox.domNode);
	this.menuContainerOuter.appendChild(this.menuContainer);
	document.body.appendChild(this.menuContainerOuter);
	
	this.menu = new dijit.Menu({},this.menuContainer);
	
//	this.onChange = function(item){console.log("ItemsDropdown.onChange");};
//	dojo.connect(this.textbox,"onBlur",function(){
//	if(!_blockBlurEvent){
//		console.log("blur textbox");
//		self.onBlur(arguments);
//	}
//});
//dojo.connect(this.textbox,"onFocus",function(){
//	_blockBlurEvent = true;
//	console.log("textbox focuss");
//});
//	dojo.connect(this.menu,"onChange",this.onMenuChange);
	
//	dojo.connect(this.menu,"onBlur",function(){
//		if(!_blockBlurEvent && !self.isFocused()){
//			console.log("blur menu");
//			self.onBlur(arguments);
//		}
//	});
	
//	dojo.connect(this.menu,"_onItemFocus",function(e){console.log("on item focus"); console.log(e.item);});
};
ItemsDropdown.prototype.isFocused = function(){
//	console.log("idFocused::::"+_menu.focused + ", " +  _textbox.focused);
	return (this.menu.focused || this.textbox.focused);
};

//^^ causing "this.getParent() is null" exception
ItemsDropdown.prototype.closeMenu = function(){
	var children = this.menu.getChildren();
	for(var i = 0 ; i < children.length ; i++){
		this.menu.removeChild(children[i]);
	}
};
ItemsDropdown.prototype.onBlur = function(){
//	console.log("onBlur:::::::::"+self.isFocused());
	this.closeMenu();
};

ItemsDropdown.prototype.getValue = function(){
	return this.textbox.value;
};

ItemsDropdown.prototype.getItem = function(){
	return this.textbox.get("item");
};

ItemsDropdown.prototype._repositionMenu = function(){
	var position = dojo.position(this.textbox.domNode, true);
	this.menuContainerOuter.style.top = (position.y + position.h + "px");
	this.menuContainerOuter.style.left = (position.x + "px");
};

ItemsDropdown.prototype.onMenuChange = function(item){
	if(item.onSelect){
		item.onSelect(item);
		
//		console.log("ItemsDropdown.onMenuChange");
//		console.log(this);
		if(this.args && this.args.onChange){
			this.args.onChange(item);
		}
	}
	this.closeMenu();
	
//	console.log("RR");
//	console.log(this.textbox);
//	this.textbox.reset();
	
};

ItemsDropdown.prototype.addMenuItem = function(item){
//	console.log("ItemsDropdown.prototype.addMenuItem");
//	console.log(item);
	var self = this;
	item.onClick = function(){self.onMenuChange(item);};
	var menuItem = new dijit.MenuItem(item);
	this.menu.addChild(menuItem);
};

ItemsDropdown.prototype.doSelectAction = function(){
//	console.log("ItemsDropdown.doSelectAction");
//	console.log(this.textbox.get("item"));
//	console.log(this.menu);
//	console.log(this.menu.focusedChild);
//	console.log(this.menu.getChildren());
//	this.outerDropdown.textbox.set("item",null);
	this.onTextChange();
	this.closeMenu();
};

ItemsDropdown.prototype.onKeyUp = function(e){ //this = TextBox
//	if(this.getValue() == ""){
//		this.outerDropdown.closeMenu();
//		return;
//	}
	//8 -> backspace
	if(e.keyCode == 40){ //arrow down
		if(this.outerDropdown.menu){
			_blockBlurEvent = true;
			this.outerDropdown.menu.focus();
			_blockBlurEvent = false;
		}
		return;
	}
	else if(e.keyCode == 27){ //ESC
//		console.log("close:::");
		this.outerDropdown.closeMenu();
		return;
	}
	else if(e.keyCode == 13){ //ENTER
//		console.log("ItemsDropdown.onTextChange.");
//		console.log(this.outerDropdown);
		this.outerDropdown.onTextChange();
		this.outerDropdown.closeMenu();
		return;
	}
	else if(e.keyCode != 8 && (e.keyCode < 65 || e.keyCode > 90) && (e.keyCode < 48 || e.keyCode > 57) ){
		return;
	}
	this.outerDropdown.textbox.set("item",null);
	if(this.outerDropdown.select){
		this.outerDropdown.select();
	}
};
//-

ItemsDropdown.prototype.onTextChange = function(){
//	console.log("ItemsDropdown.onTextChange");
//	console.log(arguments);
	
	if(this.args.onTextChange){
		this.args.onTextChange(this.textbox.get("value"));
	}
};

ItemsDropdown.prototype.menuItemSelected = function(item){
//	this.textbox.set("displayedValue",item.name + " (" + item.genreId + ")");
//	this.textbox.set("item",item);
	this.setItem(item);
};

ItemsDropdown.prototype.setItem = function(item){
//	console.log("--->ItemsDropdown.setItem");
//	console.log(item);
//	this.textbox.set("displayedValue",item.name + " (" + item.id + ")");
	this.textbox.set("displayedValue",item.name);
	this.textbox.set("item",item);
};

ItemsDropdown.prototype.setDisabled = function(b){
	this.textbox.set("disabled",b);
};

ItemsDropdown.prototype.populate = function(items){
	var self = this;
	for(var i in items){
		var item = items[i];
//		console.log("----->ItemsDropdown.populate ");
//		console.log(item);
		if(item.type == "GENRE"){
			self.addMenuItem({item: item,label: "<img style='float: left; padding-right: 5px;' src='resources/images/genre_19x20.png' />"+item.name+"&nbsp;(id: " + item.id + ")", onSelect:function(){self.menuItemSelected(this.item);}});
		}
		else if(item.type == "BOOK"){
			self.addMenuItem({item: item,label: "<img style='float: left; padding-right: 5px;' src='resources/images/book_20x20.png' />"+item.name+"&nbsp;(id: " + item.id + ")", onSelect:function(){self.menuItemSelected(this.item);}});
		}
		else if(item.type == "SHEET"){
			self.addMenuItem({item: item,label: "<img style='float: left; padding-right: 5px;' src='resources/images/sheet_21x20.png' />"+item.name+"&nbsp;(id: " + item.id + ")", onSelect:function(){self.menuItemSelected(this.item);}});
		}
		else if(item.type == "ARTICLE"){
			self.addMenuItem({item: item,label: "<img style='float: left; padding-right: 5px;' src='resources/images/sheet_21x20.png' />"+item.name+"&nbsp;(id: " + item.id + ")", onSelect:function(){self.menuItemSelected(this.item);}});
		}
		else if(item.type == "USER"){
			self.addMenuItem({item: item,label: "<img style='float: left; padding-right: 5px;' src='resources/images/user_21x20.png' />"+item.name+"&nbsp;(id: " + item.id + ")", onSelect:function(){self.menuItemSelected(this.item);}});
		}
//		if(item.imageFileId){
//			self.addMenuItem({item: item,label: "<img style='float: left; padding-right: 5px;' width=20% height=20% src='imageservice?id=" + item.imageFileId + "&thumbnail=true' />"+item.name+"&nbsp;(" + item.recordId + ")", onSelect:function(){self.menuItemSelected(this.item);}});
//		}
//		else{
//			self.addMenuItem({item: item,label: item.name+"&nbsp;(" + item.recordId + ")", onSelect:function(){self.menuItemSelected(this.item);}});
//		}
		if(i < items.length - 1){
			self.menu.addChild(new dijit.MenuSeparator());
		}
	}
};

ItemsDropdown.prototype.select = function(){
	var self = this;
	branchitup.jsonServlet.doGet("SelectMatchingItems",{phrase: self.textbox.get("displayedValue"), offset:0, maxResults: 4},function(response){
		self.closeMenu();
		self._repositionMenu();
		self.populate(response.items);
	});
};


//------------------------GENRES DROPDOWN--------------------

var GenresDropdown = function(args){ //constructor
	ItemsDropdown.call(this); //constructor stealing
//	this.constructor.prototype.args = args;
	this.args = args;
};
GenresDropdown.prototype = new ItemsDropdown();
GenresDropdown.prototype.constructor = GenresDropdown;

GenresDropdown.prototype.onTextChange = function(){
//	console.log("GenresDropdown.onTextChange");
//	console.log(this.textbox.get("value"));
	//--
	if(this.args.onTextChange){
		this.args.onTextChange(this.textbox.get("value"));
	}
};
GenresDropdown.prototype.menuItemSelected = function(item){
//	this.textbox.set("displayedValue",item.name + " (" + item.genreId + ")");
//	this.textbox.set("item",item);
	this.setGenre(item);
};
GenresDropdown.prototype.setGenre = function(item){
	this.textbox.set("displayedValue",item.name);
	this.textbox.set("item",item);
};
GenresDropdown.prototype.select = function(){
//	console.log("GenresDropdown:select:1:");
	var self = this;
	branchitup.jsonServlet.doGet("SelectMatchingItems",{phrase: self.textbox.get("displayedValue"), types: ["GENRE"], offset: 0, maxResults: 4},function(response){
		self.closeMenu();
		self._repositionMenu();
//		console.log("GenresDropdown:select:1:response");
//		console.log(response);
		
		self.populate(response.items);
	});
};

////------------------------PUBLISHED BOOKS DROPDOWN--------------------
//
//var PublishedBooksDropdown = function(args){ //constructor
//	ItemsDropdown.call(this); //constructor stealing
////	this.constructor.prototype.args = args;
//	this.args = args;
//};
//PublishedBooksDropdown.prototype = new ItemsDropdown();
//PublishedBooksDropdown.prototype.constructor = PublishedBooksDropdown;
//
////PublishedBooksDropdown.prototype.onTextChange = function(){
////	console.log("PublishedBooksDropdown.onTextChange");
////	console.log(this.textbox.get("value"));
////	//--
////	if(this.args.onTextChange){
////		this.args.onTextChange(this.textbox.get("value"));
////	}
////};
////
////
////PublishedBooksDropdown.prototype.menuItemSelected = function(item){
//////	this.textbox.set("displayedValue",item.name + " (" + item.genreId + ")");
//////	this.textbox.set("item",item);
//////	this.setGenre(item);
////	console.log("ItemsDropdown.menuItemSelected");
////	console.log(item);
//////	window.location.href = ("/branchitup/publishedbook?bookId= " + item.bookId);
////};
//
////PublishedBooksDropdown.prototype.setGenre = function(item){
////	this.textbox.set("displayedValue",item.name + " (" + item.genreId + ")");
////	this.textbox.set("item",item);
////};
//
//PublishedBooksDropdown.prototype.select = function(){
////	console.log("PublishedBooksDropdown:select:1:");
//	var self = this;
//	branchitup.jsonServlet.doGet("SelectMatchingItems",{phrase: self.textbox.get("displayedValue"), types: ["BOOK"], offset:0, maxResults: 4},function(response){
//		self.closeMenu();
//		self._repositionMenu();
////		console.log("PublishedBooksDropdown:select:1:response");
////		console.log(response);
//		
//		self.populate(response.items);
//	});
//};
//
//
////------------------------SHEETS DROPDOWN--------------------
////here we are declaring a single ItemsDropdown to all SheetsDropdown objects.
////destroying one instance might destroy the supertype as well.
////we need to use constructor stealing to avoid having one instance affect all other instances
//var SheetsDropdown = function(args){
//	ItemsDropdown.call(this); //constructor stealing
////	this.constructor.prototype.args = args; //this one will affect all objects, the creation of new one will change the functionality of onChange if it is sent in args
//	this.args = args;
//	
////	this.userSheetsOnly = true; //indicates whether only user's sheets should be selected,
////	console.log("new SheetsDropdown");
//};
//SheetsDropdown.prototype = new ItemsDropdown();
//SheetsDropdown.prototype.constructor = SheetsDropdown;
////SheetsDropdown.prototype.userSheetsOnly;
//
//SheetsDropdown.prototype.onTextChange = function(){
////	console.log("SheetsDropdown.onTextChange: ");
////	console.log(this.textbox.get("value"));
//	//--
//	if(this.args.onTextChange){
//		this.args.onTextChange(this.textbox.get("value"));
//	}
//};
//
////SheetsDropdown.prototype.setUserSheetOnly = function(f){
////	SheetsDropdown.prototype.userSheetsOnly = f;
//////	this.userSheetsOnly = b //WRONG: calling this method from a descendant object will create a new variable without changing the real one
////};
//
//SheetsDropdown.prototype.setSheet = function(sheet){
//	this.textbox.set("displayedValue",sheet.name + " (" + sheet.sheetId + ")");
//	this.textbox.set("item",sheet);
//};
//
//SheetsDropdown.prototype.menuItemSelected = function(item){
////	this.textbox.set("displayedValue",item.name + " (" + item.sheetId + ")");
////	this.textbox.set("item",item);
//	this.setSheet(item);
//};
////the reason why private variables inside SheetsDropdown might not be visible here
////is because of the context of this function.
////in this case the super class is calling this function which set the context to be
////ItemsDropdown
//SheetsDropdown.prototype.select = function(){
////	console.log("SheetsDropdown select ");
////	console.log(this.args);
//	var self = this;
//	var params = {p: self.textbox.get("displayedValue")};
//	if(this.genreId){
//		params.g = this.genreId;
//	}
//	params.o = self.args.userSheetsOnly();
////	console.log("------SelectSheetSamples-----");
////	console.log(params);
////	return;
//	branchitup.jsonServlet.doGet("SelectSheetSamples",params,function(response){
////		console.log("SheetsDropdown:1:");
//		self.closeMenu();
//		self._repositionMenu();
//		for(var i in response.sheetList){
//			var item = response.sheetList[i];
//			
//			//WITH ICON
////			if(item.genreImageFileId){
////				self.addMenuItem({sheetItem: item,label: "<img style='float: left; padding-right: 5px;' width=20% height=20% src='imageservice?id=" + item.genreImageFileId + "&thumbnail=true' />"+item.name+"&nbsp;(" + item.sheetId + ")<br/><span class='branchitupSubscript'>by "+item.firstName + " " + item.lastName + "</span>", onSelect:function(){self.menuItemSelected(this.sheetItem);}});
////			}
////			else{
////				//self.addMenuItem({sheetItem: item,label: item.name + "&nbsp;(" + item.sheetId + ")<br/><span class='branchitupSubscript'>by "+item.firstName + " " + item.lastName + "</span>", onSelect:function(){self.menuItemSelected(this.sheetItem);}});
////				self.addMenuItem({sheetItem: item,label: item.name + "&nbsp;(" + item.folderName + ")<br/><span class='branchitupSubscript'>by "+item.firstName + " " + item.lastName + "</span>", onSelect:function(){self.menuItemSelected(this.sheetItem);}});
////			}
//			
//			//WITHOUT ICON
//			if(item.owner == useraccount.userName){
//				if(item.folderName){
//					self.addMenuItem({sheetItem: item,label: item.name + "&nbsp;(" + item.folderName + ")", onSelect:function(){self.menuItemSelected(this.sheetItem);}});
//				}
//				else{
//					self.addMenuItem({sheetItem: item,label: item.name + "&nbsp", onSelect:function(){self.menuItemSelected(this.sheetItem);}});
//				}
//			}
//			else{
//				self.addMenuItem({sheetItem: item,label: item.name + "&nbsp<br/><span class='branchitupSubscript'>by "+item.firstName + " " + item.lastName + "</span>", onSelect:function(){self.menuItemSelected(this.sheetItem);}});
//			}
//			
//			
////			console.log("ItemsDropdown::::");
//			if(i < response.sheetList.length - 1){
//				self.menu.addChild(new dijit.MenuSeparator());
//			}
//		}
//	});
//};

//------------------------PUBLICATIONS DROPDOWN--------------------

//var PublicationDropdown = function(args){ //constructor
//	this.constructor.prototype.args = args;
////	console.log("PublicationDropdown constructor");
////	console.log(this.constructor.prototype.args);
//};
//PublicationDropdown.prototype = new ItemsDropdown();
//PublicationDropdown.prototype.constructor = PublicationDropdown;
//
//PublicationDropdown.prototype.onTextChange = function(){
//	console.log("PublicationDropdown.onTextChange");
//	console.log(this.textbox.get("value"));
//	//--
//	if(this.args.onTextChange){
//		this.args.onTextChange(this.textbox.get("value"));
//	}
//};
//
//PublicationDropdown.prototype.menuItemSelected = function(item){
//	this.setPublication(item);
//};
//
//PublicationDropdown.prototype.setPublication = function(item){
//	this.textbox.set("displayedValue",item.title + " (" + item.id + ")");
//	this.textbox.set("item",item);
//	
//};
//
////{"publishedBooks":[{"bookId":23153,"title":"The Night Before Christmas"},{"bookId":23057,"title":"Twenty-six Princesses"}]}
//PublicationDropdown.prototype.select = function(){
////	console.log("PublicationDropdown:1:");
//	var self = this;
//	branchitup.jsonServlet.doGet("SelectPublishedBooks",{p: self.textbox.get("displayedValue")},function(response){
//		self.closeMenu();
//		self._repositionMenu();
////		console.log("PublicationDropdown response");
////		console.log(response);
//		for(var i in response.publishedBooks){
//			var item = response.publishedBooks[i];
//			self.addMenuItem({bookItem: item,label: "<img style='float: left; padding-right: 5px;' width=10% height=10% src='imageservice?id=" + item.coverImageFileId + "&thumbnail' />"+item.title+"&nbsp;(" + item.bookId + ")", onSelect:function(){self.menuItemSelected(this.bookItem);}});
//			if(i < response.publishedBooks.length - 1){
//				self.menu.addChild(new dijit.MenuSeparator());
//			}
//		}
//	});
//};

//------------

var ByCategoryDropdown = function(args){ //constructor
	this.args = args;
	
	this.domNode = dojo.create("span");
	this.select = dojo.create("select");
	this.select.appendChild(dojo.create("option",{innerHTML: "ABC"}));
	this.domNode.appendChild(this.select);
};

//ByCategoryDropdown.prototype.render = function(){};
