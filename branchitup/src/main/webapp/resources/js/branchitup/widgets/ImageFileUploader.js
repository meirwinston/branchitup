dojo.require("dojo.parser");
dojo.require("dojo.io.iframe");
function ImageFileUploader(args){
	
	function ioIframeGetJson(x){
	    var td = dojo.io.iframe.send({
	        url: "service/FileUpload?params={\"method\":\"html5\",\"album\":\"Default\"}",
	        form: "frmIO",
	        method: "post",
	        content: {fnx:x},
	        timeoutSeconds: 5,
	        preventCache: true,
	        handleAs: "json",
	        handle: function(res, ioArgs){
            if(res.status=='Good')
            {
//	                dojo.byId('status').innerHTML=res.status;
//	                dojo.byId('textdisplay').value=res.textdata;
            	console.log("success");
            }
            else
                console.log("error status " + res.status);
            },                            
	        error: function (res,ioArgs) {
	        	console.log(res);
	        	console.log(ioArgs);
	        }
	    });
	}
	
	this.form = dojo.create("form",{
		method: "post",
		enctype: "multipart/form-data",
		name: "uploadForm"
	});
	this.fileInput = dojo.create("input",{
		id: "___fileInput___",
		type: "file",
		name: "___fileInput___",
		onchange: function(){
//			console.log();
//			this.parentNode.submit();
			ioIframeGetJson();
		}
	});
	this.form.appendChild(this.fileInput);
	
	this.domNode = dojo.create("span",{});
	this.domNode.appendChild(this.form);
}