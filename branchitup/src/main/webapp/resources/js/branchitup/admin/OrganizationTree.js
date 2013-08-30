function OrganizationTree(args){
	var _self = this;
	var _tree;
	var _container;
	var _id;
	
	this.getWidget = function(){
		return _tree;
	};
	
	this.getRoot = function(){
		return _tree.rootNode;
	};
	
	this.getDomNode = function(){
		return _container;
	};
	
	this.deleteItem = function(id){
		return _tree.deleteItemById(id);
	};
	
	this.getSelectedItem = function(){
		var item = null;
		var node = _tree.selectedNode;
		if(node != null){
			item = node.item;
		}
		return item;
	};
	
//	function nodeClicked(node){
//		console.log("OrganizationTree:nodeClicked:"+node);
//		console.log(node.item);
//		
//	}
	
	function createTree(items){
		var tree = new dojoui.Tree({
			id: _id, 
			items: items, 
			identifier: "id",
			parseOnLoad: true,
			persist: false,
//			getLabelStyle: function(item, opened){
//				return {color: "green"};
//			},
			getIconStyle: function(item, opened){
				if(!item.type) return null;
				var o = {
					height: "16px",
					width: "16px",
					backgroundPosition: "right"
				};
				switch(item.type[0]){
				case Constants.Ldap.EntityType.USER:
					o.backgroundImage = "url('resources/images/businessman16x16.png')";
					break;
				case Constants.Ldap.EntityType.ORGANIZATIONAL_UNIT:
					o.backgroundImage = "url('resources/images/cubes16x16.png')";
					break;
				case Constants.Ldap.EntityType.ORGANIZATION:
					o.backgroundImage = "url('resources/images/office-building16x16.png')";
					break;
				case Constants.Ldap.EntityType.DOMAIN:
					o.backgroundImage = "url('resources/images/environment16x16.png')";
					break;
				}
				
				return o;
			}
		});
		
//		tree.getLabelStyle = function(item, opened){ //item: dojo.data.Item, opened: Boolean
//			console.log("here is a getLabelStyle");
//			if(item && continentStore.getValue(item,"type") == "continent"){
//				return {color: "red"};
//			}else{
//				return {color: "green"};
//			}
//		};
		
//		tree.getIconStyle = function(item, opened){
//			if(item){
//				console.log("yes item here: " + opened + ", " + _tree.store.getValue(item,"name"));
//				return {
//					backgroundImage: "url('resources/images/businessman16x16.png')",
//					height: "16px",
//					width: "16px"
//				};
//				
//			}
//			else{
//				console.log("no item here: " + item +", " + opened);
//				return null;
//			}
//			
			//--
//			if(item && continentStore.getValue(item,"type") == "continent"){
//				return {
//					backgroundImage: "url('images/flatScreen.gif')",
//					height: "32px",
//					width: "32px"
//				};
//			}else{
//				return null;
//			}
//		};
		
		return tree;
	}
	
	this.setLdapItems = function(items){
		_tree.destroy();
		_tree = createTree(items);
		_tree.startup();
		_container.append(_tree.domNode);
		
		_tree.connectOnClick(args.nodeClicked);
	};
	
	this.addLdapItem = function(item){
		var i = item.dn.indexOf(",");
		if(i != -1){
			var parentDN = $.trim(item.dn.substring(i+1));
			var parentNode = _tree._itemNodesMap[parentDN];
			if(parentNode){
				_tree.newItem(item,parentNode[0].item);
			}
		}
//		var newItem = _tree.model.newItem({id:"cn=bab,ou=Graphics,o=branchitup,dc=branchitup,dc=com",dn:"cn=bab,ou=Graphics,o=branchitup,dc=branchitup,dc=com",name:"BAB",type:"cn"},parentNode[0].item);
//		console.log("the new item is:");
//		console.log(newItem);
	};
	
	
	this.expandTree = function(){
		_tree.expandTree();
	};
	
	function init(){
		dojo.require("dojoui.Tree");
		_id = generateId();
		_container = $("<span style='width: 100%;'></span>");
		$(document.body).append(_container);
		_tree = createTree([{id: 'root',"dn":"com","name":"Not Initialized..."}])
		_tree.startup();
		_container.append(_tree.domNode);
	}
	init();
}