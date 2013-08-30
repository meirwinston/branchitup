<%@tag description="Main Toolbar" pageEncoding="UTF-8"%>
<%@attribute name="id"%>

<style type="text/css">
        @import "resources/js/dojo-release-1.3.0/dijit/themes/tundra/tundra.css";
        @import "resources/js/dojo-release-1.3.0/dojo/resources/dojo.css"
</style>
<style>
.myIcon {
	background-position: 0px 0px;
	width: 20px;
	height: 20px;
	
}

</style>
<script type="text/javascript">
dojo.require("dijit.form.Button");
dojo.require("dijit.Toolbar");
dojo.require("dijit.Menu");

function MenuBar(){
	var _menuNames = ['${id}.queryMenu','${id}.actionsMenu','${id}.moreActionsMenu'];
	var _menuIsOpen = false;

    function hideAllMenus(){
    	for(var i = 0 ; i < _menuNames.length ; i++){
    		var menu = document.getElementById(_menuNames[i]);
    		menu.style.display = 'none';
    	}
    }

    function mouseOverMenu(menuIndex){
    	if(_menuIsOpen){
    		hideAllMenus();
    		var menuDiv = document.getElementById(_menuNames[menuIndex]);
    		var status = menuDiv.style.display;
    		if(status == 'none'){
    			menuDiv.style.display = '';
    			
    		}
    		else{
    			menuDiv.style.display = 'none';
    		}
    	}
    }

    this.menuClicked = function(menuIndex){
    	_menuIsOpen = !_menuIsOpen;
    	mouseOverMenu(menuIndex);
    	if(!_menuIsOpen){
    		hideAllMenus();
    	}
    	
    }

    this.menuItemClicked = function(menuIndex,itemIndex){
    	_menuIsOpen = !_menuIsOpen;
    	if(!_menuIsOpen){
    		hideAllMenus();
    	}
    	if(itemIndex == 0){
    		createInventoryQueryForm();
    	}
    }
    
}
var tb_menuBar;
    dojo.addOnLoad( function() {
    	
    	tb_menuBar = new MenuBar();
    	dojo.connect(dojo.byId("${id}.query"), "onclick", function(){tb_menuBar.menuClicked(0)});
    	dojo.connect(dojo.byId("${id}.actions"), "onclick", function(){tb_menuBar.menuClicked(1)});
    	dojo.connect(dojo.byId("${id}.moreActions"), "onclick", function(){tb_menuBar.menuClicked(2)});
    	dojo.connect(dojo.byId("${id}.dbTools"), "onclick", aaa);
    	dojo.connect(dojo.byId("${id}.help"), "onclick", aaa);
    });

    
</script>
<script type="text/javascript">




</script>

<div class="tundra">
<div id="${id}" dojoType="dijit.Toolbar">
	<div dojoType="dijit.form.Button" id="${id}.query" showLabel="true">Query</div>
	<div dojoType="dijit.form.Button" id="${id}.actions" showLabel="true">Actions</div>
	<div dojoType="dijit.form.Button" id="${id}.moreActions" showLabel="true">More Actions</div>
	<div dojoType="dijit.form.Button" id="${id}.dbTools" showLabel="true">DB Tools</div>
	<div dojoType="dijit.form.Button" id="${id}.help" showLabel="true">Help</div>
</div>

<div dojoType="dijit.Menu" id="${id}.queryMenu" contextMenuForWindow="true" style="display: none; position: absolute; z-index: 1;">
	<div dojoType="dijit.MenuItem" iconClass="myIcon" onClick="tb_menuBar.menuItemClicked(0,0)"><img src="resources/images/unknown20x20.png" />&nbsp;&nbsp;&nbsp;&nbsp;New Query</div>
	<div dojoType="dijit.MenuItem" iconClass="myIcon" onClick="alert('Somthing');"><img src="resources/images/unknown20x20.png" />&nbsp;&nbsp;&nbsp;&nbsp;New History Query</div>
	<div dojoType="dijit.MenuItem" iconClass="myIcon" onClick="alert('Somthing');"><img src="resources/images/unknown20x20.png" />&nbsp;&nbsp;&nbsp;&nbsp;New Equipment Query</div>
	<div dojoType="dijit.MenuItem" iconClass="myIcon" onClick="alert('Somthing');"><img src="resources/images/unknown20x20.png" />&nbsp;&nbsp;&nbsp;&nbsp;Open Query</div>
</div>

<div dojoType="dijit.Menu" id="${id}.actionsMenu" contextMenuForWindow="true" style="display: none; position: absolute; z-index: 1; left: 60px;">
	<div dojoType="dijit.MenuItem" iconClass="myIcon" onClick="alert('Somthing');"><img src="resources/images/element_run20x20.png" />&nbsp;&nbsp;&nbsp;&nbsp;Provision Unit</div>
	<div dojoType="dijit.MenuItem" iconClass="myIcon" onClick="alert('Somthing');"><img src="resources/images/element_into20x20.png" />&nbsp;&nbsp;&nbsp;&nbsp;Allocate As Spare</div>
	<div dojoType="dijit.MenuItem" iconClass="myIcon" onClick="alert('Somthing');"><img src="resources/images/element_previous20x20.png" />&nbsp;&nbsp;&nbsp;&nbsp;Unallocate To On-Site</div>
	<div dojoType="dijit.MenuItem" iconClass="myIcon" onClick="alert('Somthing');"><img src="resources/images/element_previous20x20.png" />&nbsp;&nbsp;&nbsp;&nbsp;Unallocate To Off-Site</div>
	<div dojoType="dijit.MenuItem" iconClass="myIcon" onClick="alert('Somthing');"><img src="resources/images/exit20x20.png" />&nbsp;&nbsp;&nbsp;&nbsp;Ship Item</div>
	<div dojoType="dijit.MenuItem" iconClass="myIcon" onClick="alert('Somthing');"><img src="resources/images/down_plus20x20.png" />&nbsp;&nbsp;&nbsp;&nbsp;Receive Item</div>
	<div dojoType="dijit.MenuItem" iconClass="myIcon" onClick="alert('Somthing');"><img src="resources/images/exchange20x20.png" />&nbsp;&nbsp;&nbsp;&nbsp;Ship For R&R</div>
	<div dojoType="dijit.MenuItem" iconClass="myIcon" onClick="alert('Somthing');"><img src="resources/images/replace20x20.png" />&nbsp;&nbsp;&nbsp;&nbsp;Replace Unit</div>
	<div dojoType="dijit.MenuItem" iconClass="myIcon" onClick="alert('Somthing');"><img src="resources/images/element_delete20x20.png" />&nbsp;&nbsp;&nbsp;&nbsp;Retire Unit</div>
</div>


<div dojoType="dijit.Menu" id="${id}.moreActionsMenu" contextMenuForWindow="true" style="display: none; position: absolute; z-index: 1; left: 120px">
	<div dojoType="dijit.MenuItem" iconClass="myIcon" onClick="alert('Somthing');"><img src="resources/images/note_edit20x20.png" />&nbsp;&nbsp;&nbsp;&nbsp;Create A User Note</div>
	<div dojoType="dijit.MenuItem" iconClass="myIcon" onClick="alert('Somthing');"><img src="resources/images/businessmen20x20.png" />&nbsp;&nbsp;&nbsp;&nbsp;View User Notes</div>
	<div dojoType="dijit.MenuItem" iconClass="myIcon" onClick="alert('Somthing');"><img src="resources/images/document_text20x20.png" />&nbsp;&nbsp;&nbsp;&nbsp;Export To CSV</div>
	<div dojoType="dijit.MenuItem" iconClass="myIcon" onClick="alert('Somthing');"><img src="resources/images/printer20x20.png" />&nbsp;&nbsp;&nbsp;&nbsp;Print</div>
	<div dojoType="dijit.MenuItem" iconClass="myIcon" onClick="alert('Somthing');"><img src="resources/images/element_delete20x20.png" />&nbsp;&nbsp;&nbsp;&nbsp;Modify Warranty End Date</div>
	<div dojoType="dijit.MenuItem" iconClass="myIcon" onClick="alert('Somthing');"><img src="resources/images/delete20x20.png" />&nbsp;&nbsp;&nbsp;&nbsp;Delete Items</div>
</div>


</div>
