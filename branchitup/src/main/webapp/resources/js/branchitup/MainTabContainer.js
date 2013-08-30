dojo.require("dojoui.TabContainer");

function MainTabContainer(args){
	this.args = args;
	this.homeTab = null;
	this.sheetEditorTab = null;
	this.sheetsBrowserTab = null;
	this.genresBrowserTab = null;
	this.openBookEditor = null;
	this.userSheetsBrowserTab = null;
	this.userBooksBrowserTab = null;
	this.reviewsBrowserTab = null;
	this.publishBookTab = null;
	this.sheetViewerTab = null;
	this.userProfileTab = null;
	this.genreEditorTab = null;
	this.bookReaderTab = null;
	
	this.multiTabMap = {};
	
	this.tabContainer = new dojoui.TabContainer({id: generateId(),style: "height: 700px; width: 100%;"},"centerPane");
	this.domNode = this.tabContainer.domNode;
	
	this.openHomeTab({
		
	});
	
	this.tabContainer.startup();
}
MainTabContainer.prototype.initTab = function(tabFieldName,constructor,args){
	var self = this;
	if(self[tabFieldName] == null){
		//call constructor using reflection, we might loose context using this call, 
		//the 'this' keyword inside the constructor might refer to window
//		self[tabFieldName] = constructor(args); 
		
		//this call we create a new object by reflection without loosing the context
		self[tabFieldName] = new constructor(args);
		
		var closeHandle = null; 
		var showHandle = null; 
		
		closeHandle = dojo.connect(self[tabFieldName].borderContainer,"onClose", function(){
			dojo.disconnect(closeHandle);
			dojo.disconnect(showHandle);
			self[tabFieldName].borderContainer.attr("onShowTime",null);
			self[tabFieldName] = null;
			
			//-- find the last tab chronologically
			var children = self.tabContainer.getChildren();
			
			var lastTab = null;
			for(var i in children){
				if(children[i].onShowTime){
					if(!lastTab) {
						lastTab = children[i];
					}
					else {
						if(children[i].onShowTime > lastTab.onShowTime){
							lastTab = children[i];
						}
					}
				}
			}
			if(lastTab){
				self.tabContainer.select(lastTab);
			}
		});
		showHandle = dojo.connect(self[tabFieldName].borderContainer,"onShow", function(){
			self[tabFieldName].borderContainer.attr("onShowTime",new Date());
		});
		self.tabContainer.addTab(self[tabFieldName].borderContainer);
	}
	else{
		self[tabFieldName].setArgs(args);
	}
	self.tabContainer.select(self[tabFieldName].borderContainer);
};

//tab that can be opened multiple times with different content - like sheet editor
MainTabContainer.prototype.initMultiTab = function(tabKey,constructor,args){ 
	var self = this;
	
	if(!self.multiTabMap[tabKey]){
		//call constructor using reflection, we might loose context using this call, 
		//the 'this' keyword inside the constructor might refer to window
//		self[tabFieldName] = constructor(args); 
		
		//this call we create a new object by reflection without loosing the context
		self.multiTabMap[tabKey] = new constructor(args);
		
		var closeHandle = null; 
		var showHandle = null; 
		
		closeHandle = dojo.connect(self.multiTabMap[tabKey].borderContainer,"onClose", function(){
			dojo.disconnect(closeHandle);
			dojo.disconnect(showHandle);
			self.multiTabMap[tabKey].borderContainer.attr("onShowTime",null);
			delete self.multiTabMap[tabKey];
			
			//-- find the last tab chronologically
			var children = self.tabContainer.getChildren();
			
			var lastTab = null;
			for(var i in children){
				if(children[i].onShowTime){
					if(!lastTab) {
						lastTab = children[i];
					}
					else {
						if(children[i].onShowTime > lastTab.onShowTime){
							lastTab = children[i];
						}
					}
				}
			}
			if(lastTab){
				self.tabContainer.select(lastTab);
			}
		});
		showHandle = dojo.connect(self.multiTabMap[tabKey].borderContainer,"onShow", function(){
			self.multiTabMap[tabKey].borderContainer.attr("onShowTime",new Date());
		});
		self.tabContainer.addTab(self.multiTabMap[tabKey].borderContainer);
	}
	else{
		self.multiTabMap[tabKey].setArgs(args);
	}
	self.tabContainer.select(self.multiTabMap[tabKey].borderContainer);

};

MainTabContainer.prototype.openSheetViewerTab = function(args){
	var self = this;
	branchitup.authenticateUser();
	importJS([
	          "resources/js/branchitup/widgets/SheetViewer.js"
	], function(){
		self.initTab("sheetViewerTab",SheetViewer,args);
	});
};

MainTabContainer.prototype.openGenresBrowserTab = function(args){
	var self = this;
	importJS([
	          "resources/js/branchitup/widgets/GenresBrowser.js"
	], function(){
		self.initTab("genresBrowserTab",GenresBrowser,args);
//		if(self.genresBrowserTab == null){
//			self.genresBrowserTab = new GenresBrowser(args);
//			dojo.connect(self.genresBrowserTab.getWidget(),"onClose", function(){
//				self.genresBrowserTab = null;
//			});
//			self.tabContainer.addTab(self.genresBrowserTab.getWidget());
//		}
//		else{
//			self.genresBrowserTab.setArgs(args);
//		}
//		self.tabContainer.select(self.genresBrowserTab.getWidget());
	});
};
MainTabContainer.prototype.openGenreEditorTab = function(args){
	var self = this;
	branchitup.authenticateUser();
	importJS([
	          "resources/js/branchitup/widgets/GenreEditor.js"
	], function(){
		self.initTab("genreEditorTab",GenreEditor,args);
	});
};

MainTabContainer.prototype.openBookReaderTab = function(args){
	var self = this;
	branchitup.authenticateUser();
	importJS([
	          "resources/js/branchitup/widgets/BookReader.js"
	], function(){
		self.initTab("bookReaderTab",BookReader,args);
	});
};

MainTabContainer.prototype.openUserBooksBrowserTab_ = function(args){
	console.log("MainTabContainer.openUserBooksBrowserTab");
	var self = this;
	branchitup.authenticateUser();
	
//	return;
	importJS([
	          "resources/js/branchitup/widgets/UserBooksBrowser.js"
	], function(){
		self.initTab("userBooksBrowserTab",UserBooksBrowser,args);
	});
};
MainTabContainer.prototype.openUserBooksBrowserTab = function(args){
	
	var self = this;
	branchitup.authenticateUser(); 
	importJS([
	          "resources/js/branchitup/widgets/UserBooksBrowser.js"
	], function(){
		self.initTab("userBooksBrowserTab",UserBooksBrowser,args);
//		if(self.userBooksBrowserTab == null){
//			self.userBooksBrowserTab = new UserBooksBrowser(args);
//			var handle = null;
//			handle = dojo.connect(self.userBooksBrowserTab.borderContainer,"onClose", function(){
//				dojo.disconnect(handle);
//				self.userBooksBrowserTab = null;
//			});
//			self.tabContainer.addTab(self.userBooksBrowserTab.borderContainer);
//		}
//		else{
//			self.openBookEditor.setArgs(args);
//		}
//		self.tabContainer.select(self.userBooksBrowserTab.borderContainer);
	});
};

MainTabContainer.prototype.openHomeTab = function(args){
	var self = this;
//	dojo.require("branchitup.widgets.Home",true);
	importJS([
	    "resources/js/branchitup/widgets/Home.js",
		"resources/js/branchitup/widgets/PublicationItem.js",
		 "resources/js/branchitup/widgets/ItemsDropdown.js"
	], function(){
		self.initTab("homeTab",Home,args);
//		if(self.homeTab == null){
//			self.homeTab = new Home(args);
//			var handle = null;
//			handle = dojo.connect(self.homeTab.borderContainer,"onClose", function(){
//				dojo.disconnect(handle);
//				self.homeTab = null;
//			});
//			
//			self.tabContainer.addTab(self.homeTab.borderContainer);
//		}
//		else{
//			self.homeTab.setArgs(args);
//		}
//		self.tabContainer.select(self.homeTab.borderContainer);
	});
};
MainTabContainer.prototype.openUserProfileTab = function(args){
	var self = this;
	branchitup.authenticateUser();
	importJS([
	   "resources/js/branchitup/widgets/UserProfile.js"
	], function(){
		self.initTab("userProfile",UserProfile,args);
	});
};

MainTabContainer.prototype.openOpenBookEditorTab = function(args){
	var self = this;
	branchitup.authenticateUser();
	importJS([
	          "resources/js/branchitup/widgets/OpenBookEditor.js"
	], function(){
		self.initTab("openBookEditor",OpenBookEditor,args);
//		if(self.openBookEditor == null){
//			self.openBookEditor = new OpenBookEditor(args);
//			//console.log("MainTabContainer.openOpenBookEditorTab set reference connecting " + self.openBookEditor.borderContainer);
//			var handle = null;
//			handle = dojo.connect(self.openBookEditor.borderContainer,"onClose", function(){
//				dojo.disconnect(handle);
//				self.openBookEditor = null;
//				//console.log("MainTabContainer.openOpenBookEditorTab set reference to null");
//			});
//			self.tabContainer.addTab(self.openBookEditor.borderContainer);
//		} else{
//			self.openBookEditor.setArgs(args);
//		}
//		self.tabContainer.select(self.openBookEditor.borderContainer);
	});
};

MainTabContainer.prototype.openSheetEditorTab = function(args){
//	dojo.require("branchitup.user.SheetEditor",true);
	var self = this;
	branchitup.authenticateUser();
	importJS([
//	          "resources/js/branchitup/widgets/ImageList.js",
			"resources/js/branchitup/widgets/ImagesPane.js",
	          "resources/js/branchitup/widgets/SheetEditor.js"
	], function(){
//		if(self.sheetEditorTab == null){
//			self.sheetEditorTab = new SheetEditor(args);
//			dojo.connect(self.sheetEditorTab.getWidget(),"onClose", function(){
//				self.sheetEditorTab = null;
//			});
//			self.tabContainer.addTab(self.sheetEditorTab.getWidget());
//		}
//		else{
//			self.sheetEditorTab.setArgs(args);
//		}
//		self.tabContainer.select(self.sheetEditorTab.getWidget());
//		console.log("MainTabContainer:init multi tab");
		self.initMultiTab(args.sheetId,SheetEditor,args);
		
//		var sheetEditorTab = new SheetEditor(args);
//		self.tabContainer.addTab(sheetEditorTab.borderContainer);
//		self.tabContainer.select(sheetEditorTab.borderContainer);
	});
};

//MainTabContainer.prototype.openTab = function(tabFieldKey,objPrototype,args){
//	var self = this;
//	if(self[tabFieldKey] == null){
//		self[tabFieldKey] = Object.create(objPrototype,args);
//		var handle = null;
//		handle = dojo.connect(self[tabFieldKey].borderContainer,"onClose", function(){
//			dojo.disconnect(handle);
//			self[tabFieldKey] = null;
//		});
//		self.tabContainer.addTab(self[tabFieldKey].borderContainer);
//	}
//	else{
//		self[tabFieldKey].setArgs(args);
//	}
//	self.tabContainer.select(self[tabFieldKey].borderContainer);
//};

//(importsArr,tabFieldKey,objPrototype,args)
//MainTabContainer.prototype.openPublishBookTab = function(args){
//	var self = this;
//	importJS(["resources/js/branchitup/widgets/PublishBookEditor.js"],function(){
//		self.openTab("publishBookTab",PublishBookEditor.prototype,args);
//	});
//};
MainTabContainer.prototype.openSheetsBrowserTab = function(args){
	var self = this;
	branchitup.authenticateUser();
	importJS([
	          "resources/js/branchitup/widgets/SheetsBrowser.js"
	], function(){
		self.initTab("sheetsBrowserTab",SheetsBrowser,args);
//		if(self.sheetsBrowserTab == null){
//			self.sheetsBrowserTab = new SheetsBrowser(args);
//			var handle = null;
//			handle = dojo.connect(self.sheetsBrowserTab.getWidget(),"onClose", function(){
//				dojo.disconnect(handle);
//				self.sheetsBrowserTab = null;
//			});
//			
//			self.tabContainer.addTab(self.sheetsBrowserTab.widget);
//		}
//		else{
//			self.sheetsBrowserTab.setArgs(args);
//		}
//		self.tabContainer.select(self.sheetsBrowserTab.widget);
	});
};

MainTabContainer.prototype.openPublishBookTab = function(args){
	var self = this;
	branchitup.authenticateUser();
	importJS([
	          "resources/js/branchitup/widgets/PublishBookEditor.js"
	], function(){
		self.initTab("publishBookTab",PublishBookEditor,args);
//		if(self.publishBookTab == null){
//			self.publishBookTab = new PublishBookEditor(args);
//			var handle = null;
//			handle = dojo.connect(self.publishBookTab.getWidget(),"onClose", function(){
//				dojo.disconnect(handle);
//				self.publishBookTab = null;
//			});
//			self.tabContainer.addTab(self.publishBookTab.getWidget());
//		}
//		else{
//			self.publishBookTab.setArgs(args);
//		}
//		self.tabContainer.select(self.publishBookTab.getWidget());
	});
};

MainTabContainer.prototype.openReviewsBrowserTab = function(args){
//	dojo.require("branchitup.user.GenresBrowser",true); //does not work on IE7,check maybe IE8
	var self = this;
	importJS([
	         "resources/js/branchitup/widgets/ReviewsBrowser.js"
	], function(){
		self.initTab("reviewsBrowserTab",ReviewsBrowser,args);
//		if(self.reviewsBrowserTab == null){
//			self.reviewsBrowserTab = new ReviewsBrowser(args);
//			var handle = null;
//			handle = dojo.connect(self.reviewsBrowserTab.borderContainer,"onClose", function(){
//				dojo.disconnect(handle);
//				self.reviewsBrowserTab = null;
//			});
//			self.tabContainer.addTab(self.reviewsBrowserTab.borderContainer);
//		}
//		self.tabContainer.select(self.reviewsBrowserTab.borderContainer);
	});
};

MainTabContainer.prototype.openUserSheetsBrowserTab = function(args){
	var self = this;
	branchitup.authenticateUser();
	importJS([
	          "resources/js/branchitup/widgets/UserSheetsBrowser.js"
	], function(){
		self.initTab("userSheetsBrowserTab",UserSheetsBrowser,args);
//		if(self.userSheetsBrowserTab == null){
//			self.userSheetsBrowserTab = new UserSheetsBrowser(args);
//			self.tabContainer.addTab(self.userSheetsBrowserTab.borderContainer);
//		}
//		self.tabContainer.select(self.userSheetsBrowserTab.borderContainer);
	});
};