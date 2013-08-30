if(!dojo._hasResource["dojoui.Tree"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.Tree"] = true;
dojo.provide("dojoui.Tree");

dojo.require("dijit.Tree");
dojo.require("dojo.data.ItemFileWriteStore");
dojo.require("dijit.tree.TreeStoreModel");
dojo.require("dijit.tree.ForestStoreModel");

dojo.declare("dojoui.Tree", dijit.Tree, {
	persist: false,
	model: null,
	labelAttr: 'label',
	typeAttr: 'type',
	childrenAttr: ['children'],
	identifier: "id",
	getTreeNode: function(nodeId){
		//in dojo 1.4 the value is an array of one element
		var treeNode = this._itemNodesMap[nodeId];
		if(typeof treeNode != 'undefined'){
			return treeNode[0];
		}
		return null;
	},
//	getLabelStyle: function(item, opened){ //item: dojo.data.Item, opened: Boolean
//		console.log("here is a getLabelStyle");
//		if(item && continentStore.getValue(item,"type") == "continent"){
//			return {color: "red"};
//		}else{
//			return {color: "green"};
//		}
//		this.inherited(arguments);
//	},
//	getIconStyle: function(item, opened){//(item: dojo.data.Item, opened: Boolean)
//		return {
//			backgroundImage: "url('resources/images/businessman16x16.png')",
//			height: "16px",
//			width: "16px",
//			backgroundPosition: "right"
//		};
//	},
	postCreate: function(){
		if(this.model == null){
			var treeData = null;
			
			if(typeof this.items != 'undefined'){
				treeData = {data: {label: 'name',identifier: this.identifier,items: this.items}};
			}
			
			this.store = new dojo.data.ItemFileWriteStore(treeData);
			this.model = new dijit.tree.TreeStoreModel({
				store: this.store,
//			    query: {_filterId:'1'},
		        rootId: "root",
		        rootLabel: "Root",
		        childrenAttrs: ["children"],
		        onDelete: function(e){}
			});
			
//			this.model = dijit.tree.ForestStoreModel({
//				store: this.store,
////				query: {_filterId:'1'},
//		        rootId: "root",
//		        rootLabel: "Root",
//		        childrenAttrs: ["children"],
//		        onDelete: function(e){}
//			});
			
			
		}
		this.inherited(arguments);
	},
	_deleteRecursive: function(){
		
	},
	_filterExpression: null,
	_filterField: null,
	_filter: function(node){ //does not realy filter yet but only removing nodes
		var children = node.getChildren();
		if(typeof children != 'undefined' && children.length > 0){
			for(var i = 0 ; i < children.length ; i++){
				this._filter(children[i]);
			}
		}
		if(node.item[this._filterField] == this._filterExpression){
//			console.log("found node to delete: " + node);
			this.deleteItem(node.item);
		}
	},
	filter: function(field,expression){
		this._filterField = field
		this._filterExpression = expression;
		var node = this.rootNode;
	
		this._filter(node);
	},
	onDeleteTreeNode: function(treeNode){
		
	},
	deleteItem: function(item){
		var treeNode = this._itemNodesMap[item[this.identifier]];
		this.onDeleteTreeNode(treeNode);
		
		this.model.store.deleteItem(item);
		//here the tree node is deleted
	},
	deleteItemById: function(itemId){
		var treeNode = this._itemNodesMap[itemId];
		if(treeNode){
			if(treeNode.constructor == Array && treeNode.length > 0){
				treeNode = treeNode[0];
			}
			this.onDeleteTreeNode(treeNode);
			this.model.store.deleteItem(treeNode.item);
			//here the tree node is deleted
		}
		return treeNode;
	},
	newItem: function(item,parentItem){ //parent item not parent node
		this.model.newItem(item,parentItem);
	},
	_populateTree: function(items,parent){ //recursive
		var newItem;
		for(var j in items){
			var children = items[j].children;
			delete items[j].children;
			newItem = this.model.newItem(items[j],parent);
			if(typeof children != 'undefined' && children != null){
				this._populateTree(children,newItem);
			}
		}
	},
	setItems: function(items){
//		this.model.store.deleteItem(this.model.root);
//		var self = this;
//		$.each(items,function(index,value){
//			self._populateTree(value.children,self.model.newItem(value));
//		});
		//-------------------------------------
//		var treeData = null;
//		this.items = items;
//		
//		if(typeof this.items != 'undefined'){
//			treeData = {data: {label: 'name',identifier: this.identifier,items: this.items}};
//		}
//		
//		this.store = new dojo.data.ItemFileWriteStore(treeData);
//		this.model = new dijit.tree.TreeStoreModel({
//			store: this.store,
//	        rootId: "root",
//	        rootLabel: "Root",
//	        childrenAttrs: ["children"],
//	        onDelete: function(e){}
//		});
//		console.log("set items complete");
	},
	getSelectedNode: function(){
		return this.selectedNode;
	},
	connectOnExpandClick: function(f){
		dojo.connect(this, "_onExpandoClick", function(event){
			f(event);
		});
	},
	
	connectOnExpand: function(f){
		dojo.connect(this, "_onExpandoClick", function(event){
			//message.node.item,message.node.item.nid,message.node.item.action,message.node.item.label
			if(event.node.isExpanded){ //expand
				console.log("expand"+event);
			}
		});
	},
	connectOnCollapse: function(f){
		dojo.connect(this, "_onExpandoClick", function(event){
			//message.node.item,message.node.item.nid,message.node.item.action,message.node.item.label
			if(!event.node.isExpanded){ //collapse
				console.log("collapse"+event);
			}
		});
	},
	connectOnClick: function(f){
		dojo.connect(this, "_onClick", f);
//		dojo.connect(this, "_onClick", function(e){
//          
//			var domElement = e.target;
//          // find widget
//          var nodeWidget = dijit.getEnclosingWidget(domElement);
//          // don't have onclick on leaf nodes or nodes that have actions
//          if(!nodeWidget || !nodeWidget.isTreeNode || nodeWidget.isExpandable){
//              return;
//          }
//          f(nodeWidget); //nodeWidget is [Widget dijit._TreeNode, dijit__TreeNode_1]
//      });

	},
//	connectOnDoubleClick: function(f){ //DOESN'T WORK
//		dojo.connect(this, "_onDblClick", function(e){
//          var domElement = e.target;
//          // find widget
//          var nodeWidget = dijit.getEnclosingWidget(domElement);
//          // don't have onclick on leaf nodes or nodes that have actions
//          if(!nodeWidget || !nodeWidget.isTreeNode || nodeWidget.isExpandable){
//              return;
//          }
//          f(nodeWidget); //nodeWidget is [Widget dijit._TreeNode, dijit__TreeNode_1]
//      });
//	},
	connectOnExpandIconClick: function(f){
		dojo.connect(this, "_onClick", function(e){
         f(e);
      });
	},
	connectOnNodeOpen: function(f){
		dojo.connect(this, "onOpen", function(item,treeNode){
			f(item,treeNode);
		});
	},
	connectOnNodeClose: function(f){
		dojo.connect(this, "onClose", function(item,treeNode){
			f(item,treeNode);
		});
	},
	connectOnIconClick: function(f){
		dojo.connect(this, "onClick", function(item,treeNode){
			f(item,treeNode);
		});
	},
	expandRoot: function(){
		this._expandNode(this.rootNode);
	},
	collapseRoot: function(){
		this._collapseNode(this.rootNode);
	},
	_expandTreeRecursively: function(treeNode){
		this._expandNode(treeNode);
		
		var children = treeNode.getChildren();
		for(var i = 0 ; i < children.length ; i++){
			this._expandTreeRecursively(children[i]);
		}
	},
	expandTree: function(){
		var root = this.rootNode;
		this._expandTreeRecursively(root);
	},
	_collapseTreeRecursively: function(treeNode){
		var children = treeNode.getChildren();
		for(var i = 0 ; i < children.length ; i++){
			this._collapseTreeRecursively(children[i]);
		}
		this._collapseNode(treeNode);
	},
	collapseTree: function(){
		var root = this.rootNode;
		this._collapseTreeRecursively(root);
	},
//		addTooltip:function(nodeId,html){
//			if(_tooltipMap[nodeId] == null){
//				var node = this._itemNodesMap[nodeId];
//				if(typeof node != 'undefined' && node != null){
//					node.labelNode.id = (nodeId + "__labelNode__");
//					_tooltipMap[nodeId] = new Tooltip({label: html,connectId: [node.labelNode.id]});
//				}
//			}
//			else{
//				_tooltipMap[nodeId].setLabel(html);
//			}
//		},
	setNodeStyle: function(nodeId,style){ //e.g. {color: "green", fontSize: "15px"}
		//TreeNode
//			params: [object Object]
//			item: [object Object]
//			tree: [Widget dijit.Tree, dijit_Tree_0]
//			isExpandable: true
//			label: NMSLab
//			indent: 0
//			id: dijit__TreeNode_0
		//domNode: [object HTMLDivElement]
		//rowNode: [object HTMLDivElement]
		//  expandoNode: [object HTMLImageElement]
		//  expandoNodeText: [object HTMLSpanElement]
		//  contentNode: [object HTMLSpanElement]
		//  iconNode: [object HTMLImageElement]
		//  labelNode: [object HTMLSpanElement]
		//  containerNode: [object HTMLDivElement]
		//  _iconClass: dijitFolderOpened
		//  _labelClass: undefined
		//  _created: true
		var node = this._itemNodesMap[nodeId]; //you must have id attribute included in the items
		if(typeof node != 'undefined' && node != null){
			for(var k in style){
				node.labelNode.style[k] = style[k];
			}
		}
		
		for(var k in this._itemNodesMap){
//				printObject(this._itemNodesMap[k].item);
			break;
		}
	}
	
});

}