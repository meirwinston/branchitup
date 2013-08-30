/* Dojo v1.4 */
if(!dojo._hasResource["dojoui.ExpandoPane"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.ExpandoPane"] = true;
dojo.provide("dojoui.ExpandoPane");

dojo.require ( "dojox.layout.ExpandoPane" );
dojo.declare("dojoui.ExpandoPane", dojox.layout.ExpandoPane, {
	show: function(){
		if ( !this._showing )
		{
			this.toggle();
		}	
	},
	
	hide: function(){
		if ( this._showing )
		{
			this.toggle();
		}	
	},
	
	isShowing: function()
	{
		return this._showing;
	},
	
	_showEnd: function(){
		this.inherited ( arguments );
		setTimeout(dojo.hitch(this, function(){ this.onShow(); this.onToggle();} ),500);
	},
	
	_hideEnd: function(){
		this.inherited ( arguments );
		setTimeout(dojo.hitch(this, function(){ this.onHide(); this.onToggle();} ),500);
	},
	
	onShow: function(){
	},
	
	onHide: function(){		
	},
	
	onToggle: function(){		
	}
});
}