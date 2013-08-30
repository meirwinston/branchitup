if(!dojo._hasResource["dojoui.Test"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.Test"] = true;
dojo.provide("dojoui.Test");

dojo.require("dijit.form.ComboBox");
dojo.require("dojo.data.ItemFileReadStore");

dojo.declare("dojoui.Test", dijit.form.ComboBox, {
	postCreate: function(){
		this.inherited(arguments);
	},
	setItems: function(items,identifier){ //NOT TESTED
		var store = new dojo.data.ItemFileReadStore( {
			data : {
				identifier : identifier,
				items : items
			}
		});
		this.attr("store",store);
	}
});
}