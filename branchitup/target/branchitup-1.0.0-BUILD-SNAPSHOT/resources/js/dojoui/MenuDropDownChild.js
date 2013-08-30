define(["dijit/popup"], function(popup){

	if(!dojo._hasResource["dojoui.MenuDropDownChild"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
		dojo._hasResource["dojoui.MenuDropDownChild"] = true;
		dojo.provide("dojoui.MenuDropDownChild");
		dojo.require("dijit._Widget");
		dojo.require("dijit.Menu");
		dojo.require("dijit.MenuItem");
		return dojo.declare("dojoui.MenuDropDownChild", dijit._Widget, {
			postCreate: function(){
				var self = this;
				if(!self.parentDom) return;
				
				this.domNode = self.parentDom;
				this.domNode.onclick = dojo.hitch(this,"toggleDropDown");
				this.dropDownContainer = this.parentDom;
				this.dropDown = new dijit.Menu({
				});
				dojo.connect(this.dropDown,"_onBlur",this.closeDropDown);
				if(this.dropDown){
					popup.hide(this.dropDown);
				}
			},
			toggleDropDown: function(){
//				console.log("toggleDropDown " + this.dropDown.isActive + ", " + this.dropDown._opened);
				if(this.dropDown.isActive){
					this.closeDropDown();
				}
				else{
					this.openDropDown();
				}
			},
			closeDropDown: function(){
				popup.close(this.dropDown);
			},
			openDropDown: function(){
//				console.log("openDropDown ");
				var self = this;
				 // make the pop-up appear around my node
			    popup.open({
			        parent: self,
			        popup: self.dropDown,
			        around: self.dropDownContainer,
//			        orient: ["below-centered", "above-centered"], 
//			        orient: ,//orient: {'BR':'TR', 'BL':'TL', 'TR':'BR', 'TL':'BL'},
			        onExecute: function(){
//			        	console.log("onExecute ");
//			        	console.log(this);
			            popup.close(self.dropDown);
			        },
			        onCancel: function(){
//			        	console.log("onCancel ");
			            popup.close(self.dropDown);
			        },
			        onClose: function(){
//			        	console.log("onClose ");
			        }
			    });
			    this.dropDown.focus();
			},
			createMenuItem: function(o){
				var self = this;
				if(!this.dropDown) return;
				
				var item = new dijit.MenuItem({
					label: o.label,
					//style: "display: none;",
					parentMenu: self.dropDown,
					onClick: o.onClick
				});
				this.dropDown.addChild(item);
				return item;
			},
			_setValueAttr: function(newValue){ //inherit this method as a connection to attr("value",newValue)
				this.label.innerHTML = newValue;
			},
			startup: function(){
				if(this._started){ return; }
				this.inherited(arguments);
			},
		});
	}
});
