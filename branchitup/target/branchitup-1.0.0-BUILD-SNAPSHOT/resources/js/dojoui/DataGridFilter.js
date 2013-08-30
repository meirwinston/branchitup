/* Dojo v1.4 */
dojo.provide ( "dojoui.DataGridFilter" );
dojo.require ( "dojo.data.ItemFileReadStore" );
dojo.require ( "dijit.ToolbarSeparator" );
dojo.require ( "dojoui.Button" );
dojo.require ( "dojoui.CheckBox" );
dojo.require ( "dojoui.ValidationTextBox" );
dojo.require ( "dojoui.FilteringSelect" );
	
dojo.declare ( "dojoui.DataGridFilter", null, {
	grid: null,
	filterInput: null,
	filterButton: null,
	appliedFilter: null,
	columnSelect: null,
	disabledState: false,
	handles: null,
	resetStoreMethods: [ "setData", "setItems" ],

	constructor: function ( args )
	{
		this.handles = [];
		if ( args )
		{
			this.params = args;
			dojo.mixin ( this, args );
		}		
		
		for ( var m in this.resetStoreMethods )
		{
			var method = this.resetStoreMethods[m];
			if ( this.grid[method] )
			{
				this.handles.push ( dojo.connect ( this.grid, method, this, "filterData" ) );
			}
		}
	},
	
	destroy: function(){
		for(var i=0;i<this.handles.length;i++){
			dojo.disconnect(this.handles[i]);
		}
	},

	setDisabled: function ( disabled )
	{
		this.disabledState = disabled;
		if ( this.filterInput )
		{
			this.filterInput.setDisabled ( disabled );
			this.filterButton.setDisabled ( disabled );
			this.columnSelect.setDisabled ( disabled );
		}
	},
	
	addToToolbar: function( toolbar ){
		var self = this;
		this.filterInput = new dojoui.ValidationTextBox({
			id: generateId(), 
			maxLength: 20,
			value: "*",
			style: "width: 100px",
			_valueBeforeKeyDown: null,
			onKeyDown: function(e){
				if(self.filterButton.getValue()){
					self.filterButton.setValue(false);
					self.unfilterData();
				}
				
				_valueBeforeKeyDown = this.getValue();
			},
			onKeyUp: function(e){
				if(this.getValue().length <= 0){
					self.filterButton.setDisabled(true);
				}
				else{
					self.filterButton.setDisabled(false);
					if(this.getValue().length != _valueBeforeKeyDown.length){
						if(self.filterButton.checked){
							self.filterData();
						}
					}
				}
			}
		});
		
		this.filterButton = new dojoui.CheckBox({
			id: generateId(), 
			label: "Filter",
			title: "Filter",
			name: "filterButton",
			checked: false,
			onChange: function(e){
				if(e){
					self.filterData();
				}
				else{
					self.unfilterData();
				}
			}
		},toolbar);
		
		//--
		
		var source = new Array();

		var struct = this.grid.attr ( "structure" );
		var structure = struct;
		if ( !dojo.isArray ( structure ) )
		{
			structure = [ struct ];
		}
		for ( var i = 0; i < structure.length; i++ )
		{
			var columns = structure[i].cells;
			for(var k in columns){
				var b = {
							value: columns[k].filterField || columns[k].field,
							name: columns[k].name
						};
			
				source.push(b);
			}
		} 
		
		//----------------------
		var id =  generateId();
		this.columnSelect = new dojoui.FilteringSelect({
			id: id,
			jsId: id,
			widgetId: id,
			style:"width: 150px",
			required: false,
			style:"text-align:left;", 
			onChange: dojo.hitch ( this, "filterData" ),
			store: new dojo.data.ItemFileReadStore({data: {
				identifier: "value", 
				items: source
			}})
		});
		
		this.columnSelect.selectByIndex(0);
		//--
		toolbar.addChild(this.columnSelect);
		toolbar.addChild(this.filterInput);
		toolbar.addChild(this.filterButton); //?_?
		var b = new dojoui.Button({id: generateId(), label: "Filter", style: "cursor: default;",onClick: function(e){
			self.filterButton.attr("checked",!self.filterButton.attr("checked"));
		}});
		toolbar.addChild(b);
		
		this.setDisabled( this.disabledState );
	}, //end of filter module	
	
	filterData: function(){
		var filter = {};
		if(this.filterButton.attr("checked")){
			var key = this.columnSelect.getItem().value[0];
			filter[key] = this.filterInput.getValue();
			if($.trim(filter[key]) == "" || $.trim(filter[key]) == "*"){
				this.unfilterData();
			}
			else{
				this.appliedFilter = filter;
				this.grid.filter(filter);
			}
		}
	},
	
	unfilterData: function(){
		var a = {};
		this.appliedFilter = a;
		this.grid.filter(a);
	}	
});
