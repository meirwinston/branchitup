function UserWorkdesk(args){
	this.workdeskContent = dojo.byId("userWorkdesk.workdeskContent");
}

UserWorkdesk.prototype.setItems = function(items){
	var item;
	this.workdeskContent.innerHTML = "";
	var t = dojo.create("table", {id: "userworkdesk.itemsTable"});
	this.workdeskContent.appendChild(t);
	
	var td,tr;
	for(var i in items){
		item = items[i];
		tr = t.insertRow(t.rows.length);
		if(item.type == "SHEET"){
			td = tr.insertCell(tr.cells.length);
			td.appendChild(dojo.create("img",{src: "resources/images/sheet_21x20.png"}));
			td = tr.insertCell(tr.cells.length);
			td.appendChild(dojo.create("a",{name: "itemName", innerHTML: item.name, href: "sheeteditor?sheetId=" + item.id}));
		}
		if(item.type == "ARTICLE"){
			td = tr.insertCell(tr.cells.length);
			td.appendChild(dojo.create("img",{src: "resources/images/sheet_21x20.png"}));
			td = tr.insertCell(tr.cells.length);
			td.appendChild(dojo.create("a",{name: "itemName", innerHTML: item.name, href: "article?articleId=" + item.id}));
		}
		else if(item.type == "BOOK"){
			td = tr.insertCell(tr.cells.length);
			td.appendChild(dojo.create("img",{src: "resources/images/book_20x20.png"}));
			td = tr.insertCell(tr.cells.length);
			td.appendChild(dojo.create("a",{name: "itemName", innerHTML: item.name, href: "bookeditor?bookId=" + item.id}));
		}
	}
};

/*
 * public String owner;
	public long offset;
	public int maxResults;
 */
UserWorkdesk.prototype.scroll = function(){
	var _self = this;
//	console.log("UserWorkdesk.scroll");
	var args = {offset: 0, maxResults: 6};
	branchitup.jsonServlet.doGet("ScrollUserItems",args,function(response){
//		console.log("---->ScrollUserItems.scroll");
//		console.log(response);
		_self.setItems(response.items);
	});
};

var userWorkdesk = new UserWorkdesk({});
userWorkdesk.scroll();
