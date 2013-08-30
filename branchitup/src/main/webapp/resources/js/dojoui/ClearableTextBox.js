/*version 1.4*/
// Provide our new class
dojo.provide("dojoui.ClearableTextBox");

// Request dependencies
//dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.ValidationTextBox");

// Declare our new class
// Declare our new class
dojo.declare("dojoui.ClearableTextBox",[dijit.form.ValidationTextBox],{

  // The "Delete" word
  deleteText: "Clear",
  clearValue: "",
  
  // Fire the change event for every text change
  intermediateChanges: true,
  
  // PostCreate method
  // Fires *after* nodes are created, before rendered to screen
  postCreate: function() {
    // Do what the previous does with this method
    this.inherited(arguments);
    // Add widget class to the domNode
    dojo.addClass(this.domNode,"clearableTextBox");
    // Create the "X" link
    this.clearLink = dojo.create("a",{
      className: "clearButton",
      innerHTML: this.deleteText
    },this.domNode,"first");
    // Fix the width
    var startWidth = dojo.style(this.domNode,"width"),
      pad = dojo.style(this.domNode,"paddingRight");
    dojo.style(this.domNode,"width",(startWidth - pad) + "px");
     // Add click event to focus node
    this.connect(this.clearLink,"onclick",function(){
      // Clear the value
      this.attr("value",this.clearValue);
      // Focus on the node, not the link
      this.textbox.focus();
    });
    // Add intermediate change for self so that "X" hides when no value
    this.connect(this,"onChange","checkValue");
    // Check value right away, hide link if necessary
    this.checkValue(this.attr("value"));
  },
  
  checkValue: function(value) {
    dojo[(value != this.clearValue && value != undefined ? "remove" : "add") + "Class"](this.clearLink,"dijitHidden");
  }
});