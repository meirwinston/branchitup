if(!dojo._hasResource["dojoui.TabContainer"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.TabContainer"] = true;
dojo.provide("dojoui.TabContainer");

dojo.require("dijit.layout.ContentPane");
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.TabContainer");
dojo.declare("dojoui.TabContainer", dijit.layout.TabContainer, {
	getAllTabs: function(){
		return this.getChildren();
	},
	select: function(child){
		this.selectChild(child);
	},
	setSelectedTabTitle: function(title){
		this.selectedChildWidget.attr("title",title);
		this.selectedChildWidget.controlButton.containerNode.innerHTML = title;
	},
	createBorderTab: function(n, content){
		var cnf =
		{
			title: n.title, 
			id: n.id, 
			jsId:n.id, 
			widgetId:n.id,
			design: "headline",
			gutters: 'false', 
			style: "width: 100%; height: 100%;"
		};
		
		if(typeof n.icon != 'undefined'){
			cnf.title = ("<img src='" + n.icon + "' border=0 />" + cnf.title);
		}
		
		if(typeof n.onClose != 'undefined'){
			cnf.onClose = n.onClose;
		}
		var bc = new dijit.layout.BorderContainer(cnf);
		this.addChild(bc);
		
		return bc;
	},
	createTab: function(n,content){ //content can be domNode
		var cnf = {
			id: n.id,
			jsId:n.id, 
			widgetId:n.id,
	        title: n.title,
//			    content: "",
	       // onClose: function(e){console.log("tab is closing")},
	        closable: true,
	        refreshOnShow:false,
	        parseOnLoad: true
//			    href: "http://www.google.com",
//			    preload: true
	    };
		
		if(typeof n.icon != 'undefined'){
			cnf.title = ("<img src='" + n.icon + "' border=0 />" + cnf.title);
		}
		
		if(typeof n.onClose != 'undefined'){
			cnf.onClose = n.onClose;
		}
		if(typeof n.closable != 'undefined'){
			cnf.closable = n.closable;
		}
		if(typeof n.refreshOnShow != 'undefined'){
			cnf.refreshOnShow = n.refreshOnShow;
		}
		if(typeof n.parseOnLoad != 'undefined'){
			cnf.parseOnLoad = n.parseOnLoad;
		}
		if(typeof n.refreshOnShow != 'undefined'){
			cnf.refreshOnShow = n.refreshOnShow;
		}
		if(typeof n.href != 'undefined'){
			cnf.href = n.href;
			cnf.preload = true;
		}
		var cp = new dijit.layout.ContentPane(cnf);
		//dojo.connect(cp, "onClose", function(event){console.log("tab is closing!")});

		this.addChild(cp);
		return cp;
	},
	hasChild: function(child){
		var children = this.getChildren();
		for(var i in children){
			if(children[i] == child){
				return true;
			}
		}
		return false;
	},
	addTab: function(tab){ //content can be domNode
		this.addChild(tab);
	},
	removeTab: function(tab){
		this.removeChild(tab);
	},
	getSelected: function(){
		return this.selectedChildWidget;
	},
	connectOnTransition: function(f){
		return dojo.connect(this.widget,"_transition", function(newPage, oldPage){
		    f(newPage, oldPage);
		});
	},
	connectOnSelectTab: function(f){
		return dojo.connect(this, "selectChild", f);
	},
	connectOnAddTab: function(f){
		return dojo.connect(this, "addChild", f);
	},
	connectOnRemoveTab: function(f){
		return dojo.connect(this, "removeChild", f);
	}
});
}