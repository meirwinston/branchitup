/**
 * a presentation of a book, image, summary, ratings
 * @param args
 */
function VPublicationItem(args){
	this.args = args;
	var _self = this;
//	console.log("VPublicationItem ");
//	console.log(dijit);
	
	if(this.args.publication){
		this.render();
	}
	else if(this.args.publicationId){
//		console.log(":::::2::");
//		console.log(this.args);
		
		branchitup.jsonServlet.doGet("GetPublicationSample",{bookId: this.args.publicationId},function(response){
			_self.args.publication = response.publication;
			delete _self.args.publicationId;
			_self.render();
		});
	}
//	else{
//		this.args.publication = {
//			title: "No Title",
//			bookSummary: "No Summary",
//			coverImageSource: "",
//			ratingAverage: 0.7,
//			ratingCount: 120
//		};
//		this.render();
//	}
}

VPublicationItem.prototype.createContributorsDom = function(){
	var t = dojo.create("table",{name: "publicationItem.userRolesTable", cellpadding: "0px", cellspacing: "0px", border: "0px"});
//	console.log("VPublicationItem.createContributorsDom");
//	console.log(this.args);
//	return;
	var row, cell;
	
	row = t.insertRow(t.rows.length);
	cell = row.insertCell(row.cells.length);
	cell.setAttribute("name","selector");
	
	cell = row.insertCell(row.cells.length);
	cell.colSpan = 2;
	cell.setAttribute("name","head");
	cell.innerHTML = "Contributors";
	
	var contributorsMap = this.args.contributors.contributorsByRole;
	
	for(var k in contributorsMap){
//		console.log("VPublicationItem.prototype.createBookRolsDom:k: " + k);
		var publishers = contributorsMap[k];
		
		row = t.insertRow(t.rows.length);
		
		cell = row.insertCell(row.cells.length);
		cell.setAttribute("name","selector");
		
		cell = row.insertCell(row.cells.length);
		if(publishers.length > 1){
			cell.innerHTML = (k + "S");
		}
		else{
			cell.innerHTML = (k);
		}
		
		cell.setAttribute("name","label");
		
		cell = row.insertCell(row.cells.length);
		
		for(var k in publishers){
			cell.innerHTML += (publishers[k].firstName + " " + publishers[k].lastName);
			cell.innerHTML += ", ";
		}
	}
	row = this.layoutTable.insertRow(this.layoutTable.rows.length);
	cell = row.insertCell(row.cells.length);
	cell.colSpan = 3;
	cell.style.paddingTop = "10px";
	cell.appendChild(t);
};

VPublicationItem.prototype.fetchPublicationContributors = function(callback){
//	console.log(this.args.publication.bookId);
//	if(bookId != 656) return;
	var bookId = this.args.publication ? this.args.publication.bookId  : this.args.publicationId; 
	var self = this;
	
//	console.log("fetchPublicationContributors");
//	console.log({bookId: bookId});
	branchitup.jsonServlet.doGet("GetPublicationContributors",{bookId: bookId},function(response){
//		console.log("VPublicationItem.GetPublicationContributors:");
//		console.log(response);
		self.args.contributors = response;
		if(callback){
			callback(response);
		}
	});
};
VPublicationItem.prototype.downloadPdfBook = function(){ 
	if(this.args.publication.pdfAttachmentId){
		branchitup.jsonServlet.downloadAttachement(this.args.publication.pdfAttachmentId);
	}
	else{
		dialogManager.openDialog({
			title: "PDF is not available",
			content: "PDF is not available for this book!",
			acknowledge: true
		});
	}
};
VPublicationItem.prototype.downloadEpubBook = function(){ 
	if(this.args.publication.epubAttachmentId){
		branchitup.jsonServlet.downloadAttachement(this.args.publication.epubAttachmentId);
	}
	else{
		dialogManager.openDialog({
			title: "EPUB is not available",
			content: "EPUB file format is not available for this book",
			acknowledge: true
		});
	}
};

VPublicationItem.prototype.openReviewsTab = function(e){
//	console.log("openReviewsTab: ");
//	console.log(this);
//	console.log(e.target.attributes.bookId); //object (value: name);
//	console.log(e.target.attributes.getNamedItem("bookId"));
//	console.log(e);
	var self = this;
//	console.log(this.args);
	branchitup.mainTabContainer.openReviewsBrowserTab({
		publication: self.args.publication
	});
};

//VPublicationItem.prototype.openParentReviewsTab = function(){
//	var self = this;
//	console.log("VPublicationItem.openParentReviewsTab: " + self.args.publication.parentId);
////	console.log(this);
//	var publicationId = self.args.publication.parentId;
////	this.args = null;
////	console.log(this.args);
////	console.log(this.args.publication);
////	console.log(this.args.publication.parentId);
//	branchitup.mainTabContainer.openReviewsBrowserTab({
//		publicationId: publicationId 
//	});
//};

VPublicationItem.prototype.createGenreTitle = function(record){
	var t = dojo.create("table",{"class": "layoutTable"});
	t.style.marginLeft = "auto";
	t.style.marginRight = "auto";
	t.setAttribute("name","bookImageHeader");
	var tr,td;
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	
	td.setAttribute("name","topLeftCorner");
	
	td = tr.insertCell(tr.cells.length);
	td.setAttribute("name","topLabel");
	if(record.genreTitles.length > 0){
		var s = record.genreTitles[0].name;
		if(s.length > 20){
			s = (s.substring(0,18) + "...");
			td.setAttribute("title",record.genreTitles[0].name);
		}
		td.appendChild(document.createTextNode(s));
	}
	
	td = tr.insertCell(tr.cells.length);
	td.setAttribute("name","topRightCorner");
	
	return t;
};

VPublicationItem.prototype.createVersionTitle = function(record){
	var t = dojo.create("table",{"class": "layoutTable"});
	t.style.marginLeft = "auto";
	t.style.marginRight = "auto";
	//t.setAttribute("name","bookImageFooter");
	var tr,td;
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.setAttribute("name","bottomLeftCorner");
	
	td = tr.insertCell(tr.cells.length);
	td.setAttribute("name","bottomLabel");
	
	var s = record.version;
	if(s.length > 20){
		s = (s.substring(0,18) + "...");
		td.setAttribute("title", record.version);
	}
	td.appendChild(document.createTextNode(s));
	
	td = tr.insertCell(tr.cells.length);
	td.setAttribute("name","bottomRightCorner");
	
	return t;
};

VPublicationItem.prototype.formatPublisherRoles = function(publisherRoleList){
	//publisher roles
	var s = "";
	for(var i in publisherRoleList){
		s += Constants.Publication.BookRole[publisherRoleList[i]].publisherRoleLabel;
		if(i < publisherRoleList.length-1){
			s += " • ";
		}
	}
	return s;
};
VPublicationItem.prototype.toggleRoleBubble = function(o){
	if(o.domNode.isOpen){
		_close();
	}
	else{
		_open();
	}
	this.bubble = null;
	
	function _open(){
		this.bubble = new dijit.TooltipDialog({
			content: "<img src='resources/images/wait-animated_31x31.gif' />",
			closable: true,
			onClick: _close,
			onCancel: _close
		});
		this.bubble.startup();
		dijit.popup.open({popup: bubble, around: o.domNode});
		o.domNode.isOpen = true;
		var rolesFormatted = o.widget.formatPublisherRoles(o.widget.args.publication.publisherRoleList);
		branchitup.jsonServlet.doGet("GetPublisherComment",{bookId: o.widget.args.publication.bookId},function(response){
//			console.log("VPublicationItem.GetPublisherComment:");
//			console.log(this.bubble);
			var s = "";
			if(rolesFormatted.length > 0){
				s += "<b>Publisher: </b>" + 
				rolesFormatted + "<br>";
			}
			if(response && response.comment){
				s += "<b>Comment</b>: " + response.comment;
			}
			if(s.length > 0){
				this.bubble.set("content",s);
			}
			else{
				_close();
			}
		});
	}
	
	function _close(){
		o.domNode.isOpen = false;
		dijit.popup.close(this.bubble);
		
	}
	
//	
//	this.appendChild(bubble);
	
	/*
	 * title: messages["title"],
			execute: dojo.hitch(this, "setValue"),
			onOpen: function(){
				_this._onOpenDialog();
				dijit.TooltipDialog.prototype.onOpen.apply(this, arguments);
			},
			onCancel: function(){
				setTimeout(dojo.hitch(_this, "_onCloseDialog"),0);
			}
			
			-----
			
			
			this._promDialog.set("content", dojo.string.substitute(
					this._strings["saveMessageSuccess"], {"0": dojo.date.locale.format(new Date(), {selector: "time"})}));
				dijit.popup.open({popup: this._promDialog, around: this.button.domNode});
				this._promDialogTimeout = setTimeout(dojo.hitch(this, function(){
					clearTimeout(this._promDialogTimeout);
					this._promDialogTimeout = null;
					dijit.popup.close(this._promDialog);
				}), 3000);
		this._isWorking = false;
	 */
};
VPublicationItem.prototype.render = function(){
	var record = this.args.publication;
	var self = this;
	var d;
//	console.log("VPublicationItem render ");
//	console.log(record);
	var itemDom = dojo.create("span",{name: "vpublicationitem"});
	d = dojo.create("div",{"class": "publicationTitlePH"});
	d.appendChild(dojo.create("a",{"class":"anchorBold", innerHTML: record.title, href: ("publishedbook?bookId=" + record.bookId)}));
	itemDom.appendChild(d);
	
	var imageDom = dojo.create("div",{name: "bookImage", onclick: function(){ 
			window.location.href = ("publishedbook?bookId=" + record.bookId);
		}, style: "cursor: pointer;"
	});
	
	if(record.coverImageUrl){
		imageDom.style.backgroundImage = ("url(" + record.coverImageUrl + ")");
	}
	else{
//		imageDom.style.backgroundImage = ("url(resources/images/no-image_200x201.png)");
		imageDom.style.backgroundImage = ("url(resources/images/no-image-branchitup_200x200.png)");
		
	}
		
	imageDom.appendChild(this.createGenreTitle(record));
	d = dojo.create("div",{name: "bookImageFooter"}); 
	d.appendChild(this.createVersionTitle(record));
	imageDom.appendChild(d);
	itemDom.appendChild(imageDom);
	
//	if(record.parentId){
//		itemDom.appendChild(dojo.create("img",{
//			src:"resources/images/branch_14x15.png", 
//			style: "margin-right: 3px; cursor: pointer;",
//			onclick: function(){
//				self.toggleRoleBubble({domNode: this, widget: self});
//			}
//		}));	
//	}
	itemDom.appendChild(dojo.create("label",{"class": "blueText", innerHTML: "By "}));
	itemDom.appendChild(dojo.create("a",{href: "userprofile?userAccountId=" + record.publisherAccountId, innerHTML: record.publisherFirstName + " " + record.publisherLastName}));
	
	if(record.parentId){
		itemDom.appendChild(dojo.create("img",{
			src:"resources/images/branch_14x15.png", 
			style: "margin-left: 5px; cursor: pointer;",
			onclick: function(){
				self.toggleRoleBubble({domNode: this, widget: self});
			}
		}));
		
		//--
		var formattedRoles = this.formatPublisherRoles(record.publisherRoleList);
		if(formattedRoles.length > 0){
			itemDom.appendChild(dojo.create("label",{style: "padding-left: 5px; color: #aaaaaa", innerHTML: formattedRoles}));
		}
		
		//--
	}
	
	if(record.parentId){
		itemDom.appendChild(dojo.create("br"));
		d = dojo.create("span",{"class": "blueText", innerHTML: "Originated From: "});
		d.appendChild(dojo.create("span",{name: "parentTitle", "class": "link" ,onclick: "window.location.href='publishedbook?bookId=" + record.parentId + "'", innerHTML: record.parentTitle}));
		itemDom.appendChild(d);
	}
	
	itemDom.appendChild(dojo.create("br"));
	d = dojo.create("label",{"class": "blueText",innerHTML: "Published on: " + branchitup.formatMillisToDate(record.createdOn)});
	itemDom.appendChild(d);
	
	itemDom.appendChild(dojo.create("br"));
	d = dojo.create("label",{"class": "blueText", innerHTML: "Language: " + record.bookLanguage});
	itemDom.appendChild(d);
	
	itemDom.appendChild(dojo.create("br"));
	d = dojo.create("label",{"class": "blueText", innerHTML: "BranchItUp ID: " + record.bookId});
	itemDom.appendChild(d);
	
	if(record.downloadsCount > 0){
		itemDom.appendChild(dojo.create("br"));
		d = dojo.create("label",{"class": "blueText", innerHTML: "Downloads:&nbsp;" + record.downloadsCount});
		itemDom.appendChild(d);
	}
	
	d = dojo.create("div", {name: "bookRatings"});
	d.appendChild(this.createRatings(record.ratingAverage,record.ratingCount));
	itemDom.appendChild(d);
	
	d = dojo.create("p",{name: "bookDescription", innerHTML: record.bookSummary});
	itemDom.appendChild(d);
	
	d = dojo.create("span", {name: "bookDeficiencyList", innerHTML: "Book Requires: "});
	var deficiencyList = record.deficiencyList;
	if(deficiencyList){
		for(var i in deficiencyList){
			var item = Constants.Publication.BookRole[deficiencyList[i]];
			var img = dojo.create("img",{
				title: item.deficiencyLabel,
				src: item.symbolIcon,
				style: "height: 15px; width: auto;"
			});
			img.style.marginLeft = "5px";
			d.appendChild(img);
		}			
	}
	itemDom.appendChild(d);

	itemDom.appendChild(dojo.create("br"));
	
	d = dojo.create("span", {name: "attachmentsLabel", innerHTML: "Download Book:"});
	d.appendChild(dojo.create("img",{'class': "attachmentIcon", title: "Download PDF Format", src:"resources/images/pdf20x20.png", onclick: dojo.hitch(this,this.downloadPdfBook)}));
	if(record.epubAttachmentId){
		d.appendChild(dojo.create("img",{style:"margin-left: 5px;", 'class': "attachmentIcon", title: "Download EPUB Format", src:"resources/images/epub_logo_20x20.png", onclick: dojo.hitch(this,this.downloadEpubBook)}));
	}
	
	itemDom.appendChild(d);
	
	itemDom.appendChild(dojo.create("br"));
	d = dojo.create("span",{name: "downloadsCount"});
	itemDom.appendChild(d);
	itemDom.appendChild(dojo.create("br"));
	if(record.branchable){
		d = dojo.create("img",{onclick: dojo.hitch(this,this.showBranchConfirm), 
			src:"resources/images/buttons/branch-this-book-button_170x25.png",
			name: "branchItImg", 
			onmouseover: "this.src = 'resources/images/buttons/branch-this-book-button-hover_170x25.png'", 
			onmouseout: "this.src = 'resources/images/buttons/branch-this-book-button_170x25.png'"	
		});
		var div = dojo.create("div",{name: "branchitButtonContainer"});
		div.appendChild(d);
		itemDom.appendChild(div);	
	}
	this.domNode = itemDom;
};

VPublicationItem.prototype.createBranchConfirmDom = function(){
	t = dojo.create("div");
	var d = dojo.create("img",{src: "resources/images/branch_47x50.png"});
	d.style.float = "left";
	t.appendChild(d);
	t.appendChild(dojo.create("span",{innerHTML: ("You are about to branch: '" + this.args.publication.title + "'.</br><br/>Branching this publication will allow you to illustrate, translate, edit, or otherwise enhance this work. When you are done working on the book, you may publish your version to make it available to all readers.<br/></br>After clicking \"branch\" below, you will find a copy of \"" + this.args.publication.title + "\" and its associated sheets in your workdesk. This is your copy to work on as you like!</br></br>Your branch will automatically be numbered by the BranchItUp numbering system.</br></br></br>Good Luck!</br>")}));

	return t;
};

VPublicationItem.prototype.showBranchConfirm = function(){
//	console.log("VPublicationItem.showBranchConfirm");
//	console.log(this.branchConfirmDom);
//	console.log(branchitup);
	//dojo.hitch(this,this.openParentReviewsTab)
	var self = this;
	dialogManager.openDialog({
		title: "Branch A Book", 
		content: this.createBranchConfirmDom(), 
		style: "width: 500px; height: 400px; vertical-align: top;",
		onOK: dojo.hitch(self,self.branch),
		okLabel: "Branch This Book"
	});
};
VPublicationItem.prototype.branch = function(){
//	console.log("VPublicationItem.branch: ");
//	console.log(this.args);
	
	dialogManager.openDialog({title: "Branching...", content: "Please Wait...<br /><img src='resources/images/animated-wait.gif' />", style: "width: 200px; height: 100px;"});
	branchitup.jsonServlet.doGet("BranchPublishedBook",{bookId: this.args.publication.bookId},function(response){
//		console.log("VPublicationItem.branch:");
//		console.log(response);
		window.location.href = ("bookeditor?bookId=" + response.bookId);
	},
	function(e){
		if(e.javaClass == "UnauthorizedException"){
			dialogManager.openDialog({
				title: "Login before branching",
				content: "Please login before branching a book, if you do not have an account, first sign up and then try again!<br/><br/>Thank You<br/>",
				acknowledge: true,
				style: "height: 200px; width: 400px;"
			});
		}
		else{
			dialogManager.openDialog({
				title: "Problem during branching",
				content: "There was an unexpected problem during the process of branching, please try again!",
				acknowledge: true
			});
		}
		
	});
};

//VPublicationItem.prototype.createRatings = function(average, count){
//	var dom = dojo.create("span");
//	if(average){
//		var img = dojo.create("img");
//		if(average > 0){
//			img.src = "resources/images/star_yellow20x20.png";
//		}
//		else{
//			img.src = "resources/images/star_grey20x20.png";
//		}
//		
//		dom.appendChild(img);
//		
//		img = dojo.create("img");
//		if(average > 0.2){
//			img.src = "resources/images/star_yellow20x20.png";
//		}
//		else{
//			img.src = "resources/images/star_grey20x20.png";
//		}
//		dom.appendChild(img);
//		
//		img = dojo.create("img");
//		if(average > 0.4){
//			img.src = "resources/images/star_yellow20x20.png";
//		}
//		else{
//			img.src = "resources/images/star_grey20x20.png";
//		}
//		dom.appendChild(img);
//		
//		img = dojo.create("img");
//		if(average > 0.6){
//			img.src = "resources/images/star_yellow20x20.png";
//		}
//		else{
//			img.src = "resources/images/star_grey20x20.png";
//		}
//		dom.appendChild(img);
//		
//		img = dojo.create("img");
//		if(average > 0.8){
//			img.src = "resources/images/star_yellow20x20.png";
//		}
//		else{
//			img.src = "resources/images/star_grey20x20.png";
//		}
//		dom.appendChild(img);
//		//-- rating count
//		var ratingCountSpan = dojo.create("span");
//		ratingCountSpan.innerHTML = ("&nbsp;&nbsp;&nbsp;" + count + " ratings");
//		dom.appendChild(ratingCountSpan);
//	}
//	else{
//		dom.innerHTML = "No ratings.";
//	}
//	return dom;
//};


VPublicationItem.prototype.createRatings = function(average, count){
	var dom = dojo.create("span");
	if(average){
		//--
//		var averageStars = Math.floor(average / 0.2);
		var averageStars = (average * 5);
		for(var i = 0 ; i < 5 ; i++){
			var img = dojo.create("img");
			if(i < averageStars){
				img.src = "resources/images/star_yellow20x20.png";
			}
			else{
				img.src = "resources/images/star_grey20x20.png";
			}
			dom.appendChild(img);
		}
		//--
		//-- rating count
		if(count){
			var ratingCountSpan = dojo.create("span");
			ratingCountSpan.innerHTML = ("&nbsp;&nbsp;&nbsp;" + count + " ratings");
			dom.appendChild(ratingCountSpan);
		}
		
	}
	else{
		dom.innerHTML = "No ratings.";
	}
	return dom;
};
