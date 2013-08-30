if(!dojo._hasResource["dojoui.RecurrenceDialog"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.RecurrenceDialog"] = true;
dojo.provide("dojoui.RecurrenceDialog");

dojo.require("dijit.Dialog");
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dijit.form.Button");
dojo.require("dijit.form.RadioButton");
dojo.require("dijit.form.TimeTextBox");
dojo.require("dijit.form.FilteringSelect");
dojo.require("dojo.data.ItemFileReadStore");
dojo.require("dijit.form.NumberSpinner");

dojo.declare("dojoui.RecurrenceDialog", dijit.Dialog, {
	_self: this,
	title: "Recurrence Pattern",
	onOKAction: null,
	onCancelAction: null,
	parseOnLoad: true,
	runtimeInput: null,
	recurrencePatternInput: null,
	_daysOfWeekInput: null,
	_dayInMonthInput: null,
	_recureEveryContainer: null,
	_runtimeInputContainer: null,
	retentionInput: null,
	style: "width: 500px; height: 200px;",
	okButton: null,
	cancelButton: null,
	centerContentPane: null,
//	bottomContentPane: null,
	content: new dijit.layout.ContentPane({region: 'center'}),
	show: function(){
		this.inherited(arguments);
		var s = "width: " + (parseInt(this.domNode.clientWidth)-20) + "px; height: " + (parseInt(this.domNode.clientHeight)-40) + "px;"
		this.hide();
		this.content.attr("style",s);
		
		this.inherited(arguments);
		
//		console.log("show ");
//		console.log(this.runtimeInput);
//		this.runtimeInput.domNode.blur();
		
	},
	toJson: function(){
		var o = {
			time: null,
			recurrencePattern: null,
			recurEvery: 0,
			retention: this.retentionInput.attr("value")
		};
		if(this.recurrencePatternInput[0].checked){
			o.recurrencePattern = this.recurrencePatternInput[0].attr("value");
		}
		else if(this.recurrencePatternInput[1].checked){
			o.recurrencePattern = this.recurrencePatternInput[1].attr("value");
			o.recurEvery = this._daysOfWeekInput.attr("value");
		}
		else if(this.recurrencePatternInput[2].checked){
			o.recurrencePattern = this.recurrencePatternInput[2].attr("value");
			o.recurEvery = this._dayInMonthInput.attr("value");
		}
		o.time = this._dateToTimestampFormat(this.runtimeInput.attr("value"));
		
		return o;
	},
	close: function(){
		this.onCancel();
	},
	_dateToTimestampFormat: function(date){
		var s = "";
//		s += date.getFullYear();
//		s += "-";
//		s += (date.getMonth() < 10 ? ("0" + (date.getMonth()+1)) : (date.getMonth()+1));
//		s += "-";
//		s += (date.getDate() < 10 ? ("0" + date.getDate()) : date.getDate());
//		s += " ";
		s += (date.getHours() < 10 ? ("0" + date.getHours()) : date.getHours());
		s += ":";
		s += (date.getMinutes() < 10 ? ("0" + date.getMinutes()) : date.getMinutes());
		s += ":";
		s += (date.getSeconds() < 10 ? ("0" + date.getSeconds()) : date.getSeconds());
//		s += ".000";
		return s;
	},
	_initButtons: function(){
		var self = this;
		
		this.cancelButton = new dijit.form.Button({
			label: "Cancel",
			onClick: function(e){
				if(self.onCancelAction != null){
					self.onCancelAction();
				}
				self.close();
			}
		});
		
		this.okButton = new dijit.form.Button({
			label: "OK",
			tabIndex: "0",
			onClick: function(e){
				if(self.onOKAction != null){
					var json = self.toJson();
					self.onOKAction(json);
				}
			}
		});
	},
	_initRetentionInput: function(){
		this.retentionInput = new dijit.form.NumberSpinner({ 
			constraints:{ max:300, min:1 },
			value: 1
		}, "somInput");
	},
	_initRuntimeInput: function(){
		this.runtimeInput = new dijit.form.TimeTextBox({
			value : new Date(), 
			tabIndex: "1",
			selected: true
		});
	},
	_initDaysOfWeekInput: function(){
		this._daysOfWeekInput = new dijit.form.FilteringSelect( {
			store : new dojo.data.ItemFileReadStore( {
				data : {
					identifier : "value",
					items : [ 
					    {
					    	name : "Sunday",
					    	value : 1
						}, {
							name : "Monday",
							value : 2
						},
						{
							name : "Tuesday",
							value : 3
						},
						{
							name : "Wednesday",
							value : 4
						},
						{
							name : "Thursday",
							value : 5
						},
						{
							name : "Friday",
							value : 6
						},
						{
							name : "Saturday",
							value : 7
						}
					]
				}
			}),
			value: 1,
			onChange : function(e) {}
		});
	},
	_initDayInMonthInput: function(){
		var items = [];
		for(var i = 1 ; i < 32 ; i++){
			items.push({name: ("" + i), value: i}); //in java first day of month is 1
		}
		this._dayInMonthInput = new dijit.form.FilteringSelect( {
			store : new dojo.data.ItemFileReadStore( {
				data : {
					identifier : "value",
					items : items
				}
			}),
			value: 1,
			onChange : function(e) {}
		});
	},
	_initRecurrencePattern: function(){
		this.recurrencePatternInput = new Array(3);
		var self = this;
		
		this.recurrencePatternInput[0] = new dijit.form.RadioButton({
			value: "DAILY",
			name: self.id,
			onChange: function(newValue){
				self.updateForm();
			}
		});
		
		this.recurrencePatternInput[1] = new dijit.form.RadioButton({
			value: "WEEKLY",
			name: self.id,
			onChange: function(newValue){
				if(newValue){
//					console.log("on change: " + newValue);
					self.updateForm();
				}
			}
		});
		
		this.recurrencePatternInput[2] = new dijit.form.RadioButton({
			value: "MONTHLY",
			name: self.id,
			checked: true,
			onChange: function(newValue){
				if(newValue){
//					console.log("on change: " + newValue);
					self.updateForm();
				}
			}
		});
		
	},
	_createLayoutTable: function(){
		var t = dojo.create("table");
		t.cellPadding = "0";
		t.cellSpacing = "0";
		t.border = "0";
		t.style.width = "100%";
		t.style.height = "100%";

		var tr, cell, rowIndex = 0;
		
		tr = t.insertRow(rowIndex++);
		cell = tr.insertCell(0);
		cell.textContent = "Run Time";
		cell = tr.insertCell(1);
//		cell.appendChild(this.runtimeInput.domNode);
		this._runtimeInputContainer = cell;
		
		tr = t.insertRow(rowIndex++);
		cell = tr.insertCell(0);
		cell.colSpan = 2;
		cell.innerHTML = "&nbsp;";
//		cell.style.hight = "10px";
		
		tr = t.insertRow(rowIndex++);
		cell = tr.insertCell(0);
		cell.textContent = "Recurrence Pattern";
		cell = tr.insertCell(1);
		
		var tt = dojo.create("table");
		var rr,cc;
		cell.appendChild(tt);
		
		tt.cellPadding = "0";
		tt.cellSpacing = "0";
		tt.border = "0";
		tt.style.width = "100%";
		rr = tt.insertRow(0);
		
		cc = rr.insertCell(0);
		cc.style.width = "10px";
		cc.appendChild(this.recurrencePatternInput[0].domNode);
		
		cc = rr.insertCell(1);
		cc.textContent = "Daily";
		
		cc = rr.insertCell(2);
		cc.style.width = "10px";
		cc.appendChild(this.recurrencePatternInput[1].domNode);
		
		cc = rr.insertCell(3);
		cc.textContent = "Weekly";
		
		cc = rr.insertCell(4);
		cc.style.width = "10px";
		cc.appendChild(this.recurrencePatternInput[2].domNode);
		
		cc = rr.insertCell(5);
		cc.textContent = "Monthly";
		
		
		
		tr = t.insertRow(rowIndex++);
		cell = tr.insertCell(0);
		cell.colSpan = 2;
		cell.innerHTML = "&nbsp;";
//		cell.style.hight = "10px";
		
		tr = t.insertRow(rowIndex++);
		cell = tr.insertCell(0);
		cell.textContent = "Recur Every";
		cell = tr.insertCell(1);
//		cell.appendChild(this.recureEveryInput.domNode);
		this._recureEveryContainer = cell;
		
		tr = t.insertRow(rowIndex++);
		cell = tr.insertCell(0);
		cell.colSpan = 2;
		cell.innerHTML = "&nbsp;";
//		cell.style.hight = "10px";
		
		tr = t.insertRow(rowIndex++);
		cell = tr.insertCell(0);
		cell.textContent = "Retention";
		cell = tr.insertCell(1);
		cell.appendChild(this.retentionInput.domNode);
//		this._recureEveryContainer = cell;
		
		tr = t.insertRow(rowIndex++);
		cell = tr.insertCell(0);
		cell.colSpan = 2;
		cell.innerHTML = "&nbsp;";
//		cell.style.hight = "10px";
		
		tr = t.insertRow(rowIndex++);
		cell = tr.insertCell(0);
		cell.colSpan = 2;
		cell.appendChild(this.okButton.domNode);
		cell.appendChild(this.cancelButton.domNode);
		
		return t;
	},
	updateForm: function(){
		
		if(this.recurrencePatternInput[0].attr("value")){
			this._dayInMonthInput.attr("disabled",true);
			this._daysOfWeekInput.attr("disabled",true);
		}
		else if(this.recurrencePatternInput[1].attr("value")){
			this._dayInMonthInput.attr("disabled",false);
			this._daysOfWeekInput.attr("disabled",false);
			
			this._recureEveryContainer.innerHTML = "";
			this._recureEveryContainer.appendChild(this._daysOfWeekInput.domNode);
			
		}
		else if(this.recurrencePatternInput[2].attr("value")){
			this._dayInMonthInput.attr("disabled",false);
			this._daysOfWeekInput.attr("disabled",false);
			
			this._recureEveryContainer.innerHTML = "";
			this._recureEveryContainer.appendChild(this._dayInMonthInput.domNode);
		}
		if(this._runtimeInputContainer.childNodes.length == 0){
			this._runtimeInputContainer.appendChild(this.runtimeInput.domNode);
		}
//		this.runtimeInput.attr("disabled",false);
	},
	postCreate: function(){
		this.inherited(arguments);
		this._initButtons();
		this._initRecurrencePattern();
		this._initDaysOfWeekInput();
		this._initDayInMonthInput();
		this._initRuntimeInput();
		this._initRetentionInput();
		this.content.attr("content",this._createLayoutTable());
		
		this.updateForm();
	}
});

}