function AdminPanel(args){
	var _self = this;
	var _borderContainer;
	var _leftPane;
	var _centerPane;
	var _bottomPane;
	var _topPane;
	var _organizationTree;
	var _adminToolbar;
	var _reloadButton;
	var _deleteButton;
	var _addUserButton;
	var _addUnitButton;
	var _userForm;
	var _unitForm;
	
//	function createUserForm(){
//		
//		var container = new dijit.layout.ContentPane({
//		    id : generateId(),
//			region : 'center',
//			content : "this is a user form",
//			style : "border: 1px solid #DDDDFF;"
//	    }); 
//		
//		return container;
//	}
	
	function addUser(){
		var o = _userForm.toJson();
		var selectedTreeItem = _organizationTree.getSelectedItem();
		if(selectedTreeItem != null){
			o.dn = "cn=" + o.uid + "," + selectedTreeItem.dn[0];
		}
		
		branchitup.jsonServlet.doPost("com.branchitup.web.servlet.command.AddInetOrgPerson",o,function(response){
			_organizationTree.addLdapItem(response);
		});
	}
	
	function addUnit(){
		var o = _unitForm.toJson();
		var selectedTreeItem = _organizationTree.getSelectedItem();
		if(selectedTreeItem != null){
			o.dn = "ou=" + o.ou + "," + selectedTreeItem.dn[0];
		}
		
		branchitup.jsonServlet.doPost("com.branchitup.web.servlet.command.AddOrganizationalUnit",o,function(response){
			_organizationTree.addLdapItem(response);
		});
	}
	
	function deleteEntity(){
//		_organizationTree.deleteItem("cn=scram,ou=HR,o=branchitup,dc=branchitup,dc=com");
//		return;
//		
		var selectedTreeItem = _organizationTree.getSelectedItem();
		if(selectedTreeItem != null){
			branchitup.jsonServlet.doPost("com.branchitup.web.servlet.command.DeleteDirectoryEntity",
			{dn: selectedTreeItem.dn[0]},
			function(response){
				_organizationTree.deleteItem(response.dn);
			});
		}
	}
	
	function buildAdminToolbar() {
		_adminToolbar = new dijit.Toolbar( {
			id : generateId(),
			style : "height: 26px;"
		});

		// --
		_reloadButton = new dojoui.Button(
		{
			id : generateId(),
			label : "<img src=\"resources/images/refresh20x20.png\" />&nbsp;&nbsp;<a style='color: blue; text-decoration: underline;'>Reload</a>",
			onClick : function(e) {
				_self.reload();
			}
		});
		_adminToolbar.addChild(_reloadButton);
		_adminToolbar.addChild(new dijit.ToolbarSeparator());
		// --
		_addUserButton = new dojoui.Button(
		{
			id : generateId(),
			label : "<img src=\"resources/images/businessman_add20x20.png\" />&nbsp;&nbsp;<a style='color: blue; text-decoration: underline;'>Add User</a>",
			onClick : function(e) {
				addUser();
			}
		});
		_adminToolbar.addChild(_addUserButton);
		_adminToolbar.addChild(new dijit.ToolbarSeparator());
		
		// --
		_addUnitButton = new dojoui.Button(
		{
			id : generateId(),
			label : "<img src=\"resources/images/cubes20x20.png\" />&nbsp;&nbsp;<a style='color: blue; text-decoration: underline;'>Add Unit</a>",
			onClick : function(e) {
				addUnit();
			}
		});
		_adminToolbar.addChild(_addUnitButton);
		_adminToolbar.addChild(new dijit.ToolbarSeparator());
		
		
		// --
		_deleteButton = new dojoui.Button(
		{
			id : generateId(),
			label : "<img src=\"resources/images/delete20x20.png\" />&nbsp;&nbsp;<a style='color: blue; text-decoration: underline;'>Delete</a>",
			onClick : function(e) {
				deleteEntity();
			}
		});
		_adminToolbar.addChild(_deleteButton);
		_adminToolbar.addChild(new dijit.ToolbarSeparator());
	}
	
	this.getWidget = function(){
		return _borderContainer;
	};
	
	this.reload = function(){
		branchitup.jsonServlet.doPost("com.branchitup.web.servlet.command.GetDirectoryTree",{},function(response){
			_organizationTree.setLdapItems(response.ldapItems);
			_organizationTree.expandTree();
		});
	};
	
	function treeNodeSelected(node){
		_userForm.populate(node.item);
		
//		console.log("organizationdddd");
//		console.log(node.item.type[0]);
//		if(node.item.type[0] == "ou" || node.item.type[0] == "o"){
//			console.log("organization");
//			if(_centerPane.attr("formType") != "unit"){
//				document.body.appendChild(_userForm.getWidget().domNode);
//				_centerPane.attr("content",_unitForm.getWidget());
//				_centerPane.attr("formType","unit");
//			}
//			_unitForm.populate(node.item);
//		}
//		else if(node.item.type[0] == "cn"){
//			console.log("common name");
//			document.body.appendChild(_unitForm.getWidget().domNode);
//			if(_centerPane.attr("formType") != "user"){
//				_centerPane.attr("content",_userForm.getWidget());
//				_centerPane.attr("formType","user");
//			}
//			_userForm.populate(node.item);
//		}
		
	}
	
	function initLayout(){
		buildAdminToolbar();
		_organizationTree = new OrganizationTree({
			nodeClicked: treeNodeSelected
		});
		_userForm = new UserForm({});
		_unitForm = new UnitForm({});
		
		_centerPane = new dijit.layout.ContentPane({
		    id : generateId(),
			region : 'center',
			content : _userForm.getWidget(),
//			content : _unitForm.getWidget(),
			style : "border: 1px solid #DDDDFF;"
	    });
		
		_leftPane = new dijit.layout.ContentPane({
		    id : generateId(),
			region : 'left',
			content : _organizationTree.getDomNode(),
			style : "border: 1px solid #DDDDFF; width: 300px; padding: 4px;",
			splitter : true
	    });
		
		_topPane = new dijit.layout.ContentPane({
		    id : generateId(),
			region : 'top',
			content : _adminToolbar,
			style : "border: 1px solid #DDDDFF;"
	    });
		
		_bottomPane = new dijit.layout.ContentPane({
		    id : generateId(),
			region : 'bottom',
			content : "bottom",
			style : "border: 1px solid #DDDDFF;"
	    });
		
		_borderContainer = new dijit.layout.BorderContainer({
			id: generateId(), 
			design: "sidebar",//sidebar,headline
			gutters: 'false',
			onShow: function(e){
			}
		},args.containerId);
		_borderContainer.startup();
		
		_borderContainer.addChild(_topPane);
		_borderContainer.addChild(_centerPane);
		_borderContainer.addChild(_bottomPane);
		_borderContainer.addChild(_leftPane);
		
		_self.reload();
		
	}
	
	function init(){
		dojo.require("dijit.layout.ContentPane");
		dojo.require("dijit.layout.BorderContainer");
		dojo.require("dijit.Toolbar");
		dojo.require("dojoui.Button");
		dojo.require("dojoui.ValidationTextBox");
		
		importJS(["dojoui/ValidationTextBox.js",
		          "branchitup/admin/OrganizationTree.js",
		          "branchitup/admin/UserForm.js","branchitup/admin/UnitForm.js"],initLayout);
		
	}
	init();
}