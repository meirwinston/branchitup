if(!dojo._hasResource["dojoui.Pagination"]){
dojo._hasResource["dojoui.Pagination"]=true;
dojo.provide("dojoui.Pagination");
dojo.require("dijit._Widget");


dojo.declare("dojoui.Pagination",dijit._Widget,{
	_htmlTable: null,
	postCreate: function(){
		this._htmlTable = dojo.create("table");
		if(this.id) this._htmlTable.setAttribute("id",this.id); 
		this._htmlTable.cellSpacing = 0;
		this._htmlTable.cellPadding = 0;
		if(dojo.config.isDebug){
			this._htmlTable.border = "1px";
		}
		this._htmlTable.insertRow(0);
		this._htmlTable.className = this.classOfNode;
		this.calculateIndexes(0,this.totalResults);
		
//		this.render();
		if(this.containerNode){
			this.containerNode.appendChild(this._htmlTable);
		}
		
		this.domNode = this._htmlTable;
	},
	startup: function(){
		this.render();
	},
	_setValueAttr: function(newValue){ //inherit this method as a connection to attr("value",newValue)
		if(newValue){
			this._htmlTable.innerHTML = newValue;
			console.log("Pagination:_setValueAttr:"+newValue);
		}
		
	},
//	set: function(property,value){
//		this.inherited(arguments);
//		this.postCreate();
//	},
//	_set: function(property,value){ //inherit this method as a connection to attr("value",newValue)
//		this.attr(property,value)
//		if(newValue){
//			this._htmlTable.innerHTML = newValue;
//			console.log("Pagination:_set:"+newValue);
//		}
//		this._htmlTable.innerHTML = value;
//		console.log("_set "+property + ", " + value);
//	},
	/**
	* the maximum number of pages to show on the bar
	*/
	MAXIMUM_PAGES: 20,
	actionFunction: "browse",
	currentPageIndex: 0,
	/**
	 * the number of pages shown, (pages)
	 */
	numberOfPagesToDisplay: 5,//10

	/**
	* the current page
	*/
	currentPageNumber: 0,

	/**
	* how many items are being shown in the page when searching
	*/
	pageSize: 10, //10

	/**
	* the index of the first page.
	*/
	firstPageNumber: 0,

	/**
	* the first item index in the table at the specified currentPageNumber.
	* this index is calculated as follow: 
	* firstItemIndex = currentPageNumber*pageSize
	*/
	firstItemIndex: 0,

	/**
	* the index of the last item in the current page
	*/
	lastItemIndex: null,

	/**
	* contains the number of searches found.
	*/
	totalResults:  100,

	/**
	* the number of pages in a current search
	*/
	totalPages: 0,

	/**
	* the number of pages to step forward or backward when clicking 
	* the next block or previous block of pages.
	*/
	blockSize: 5, //10

	/**
	* the text shown on the label of the link for the next page.
	*/
	stepForwardText: "next",

	/**
	* the text shown on the label of the link for the next page.
	*/
	stepBackwordText: "prev",

	/**
	* the tooltip text shown when hovering above the link of the next 
	* block
	*/
	nextBlockToolTip: "Next Block",

	/**
	* the tooltip text shown when hovering above the link of the last 
	* block
	*/
	backBlockToolTip: "Next Block",

	/**
	* the value can be url for an image of a simple text.
	*/
	nextBlockText: "...",

	/**
	* the value can be url for an image of a simple text.
	*/
	lastBlockText: "...",

//	/**
//	* the direction of the control. default is right to left.
//	*/
//	dirAttribute: "ltr",

	/**
	* the url of the image for the ImageButton of last page.
	*/
	backwardImageUrl: null,

	/**
	* the url of the image for the ImageButton of next page.
	*/
	forwordImageUrl: null,

	/**
	* the name of the css class for the look and feel of the current 
	* selected label.
	*/
	classOfSelectedLabel: null,

	/**
	* the css class for the labels that are not selected.
	*/
	classOfUnselectedLabel: null,
	classOfNode: null,
	onBrowse: function(offset, pageSize){
		console.log("onBrowse(" + offset + ", " + pageSize + ")");
	},
	calculateIndexes: function(pageIndex,totalResults)
	{
		if (totalResults < 0) totalResults = 0;
		if (pageIndex < 0) pageIndex = 0;
		
		this.totalResults = totalResults;
		var page = pageIndex;
//		this.currentPageNumber = page;
		this.firstItemIndex = this.currentPageNumber * this.pageSize;
		this.firstPageNumber = page - (page % this.numberOfPagesToDisplay);

		if ((this.firstItemIndex + this.pageSize) >= totalResults)
		{
			this.lastItemIndex = totalResults - 1;
		}
		else
		{
			this.lastItemIndex = this.firstItemIndex + this.pageSize - 1;
		}
		if ((totalResults % this.pageSize) == 0)
		{
			this.totalPages = parseInt(totalResults / this.pageSize);
		}
		else
		{
			this.totalPages = parseInt(totalResults / this.pageSize + 1);
		}
	},

	/**
	* the css of the links of the next page and last page.
	*/
	classNextBack: null,
	setNumberOfPagesToDisplay: function(value)
	{
		if (value > 0 && value <= this.MAXIMUM_PAGES)
		{
			this.numberOfPagesToDisplay = value;
			this.calculateIndexes(this.currentPageNumber, this.totalResults);
		}
	},
	setPageSize: function(value){
		if (value > 0)
		{
			this.pageSize = value;
			this.calculateIndexes(this.currentPageNumber, this.totalResults);
		}
	},
	refresh: function(){
		this.calculateIndexes(this.currentPageNumber,this.totalResults);
		this.render();
	},
	
	//use this method as follows if you add an item:
//	pagination.calculateIndexes(pagination.currentPageNumber,pagination.totalResults+1);
//	pagination.pageToLastPage();
	pageToLastPage: function(){
		this.pageTo(pagination.totalPages-1);
	},
	pageTo: function(pageNumber){
//		console.log("pageTo " + pageNumber);
		this.currentPageNumber = pageNumber;
		this.refresh();
		this.onBrowse(this.firstItemIndex,this.pageSize);
	},
	onPageClick: function(e){
		var widget = e.target.pagination;
//		widget.currentPageNumber = parseInt(e.target.getAttribute("page"));
//		widget.refresh();
//		widget.onBrowse(widget.firstItemIndex,widget.pageSize);
		
		widget.pageTo(parseInt(e.target.getAttribute("page")));
	},
//	onPageClick: function(e){
//		var widget = e.target.pagination;
//		var page = parseInt(e.target.getAttribute("page"));
//		widget.calculateIndexes(page,widget.totalResults);
//		this.currentPageNumber
//		widget.render();
//		widget.onBrowse(widget.firstItemIndex,widget.pageSize);
////		console.log("onPageClick: " + widget.actionFunction + ", " + startIndex + ", " + widget.pageSize + ", " + widget.totalResults);
//	},
	render: function(){
		var row = this._htmlTable.rows[0];
		while(row.cells.length > 0){
			row.deleteCell(0);
		}
		var cell;
		//--
//        var javascriptFunction = "";
		//why did I put this limit?
//        var limitOfPages = Math.ceil(this.totalResults / this.pageSize);
        
        if(this.totalResults <= this.pageSize){
//        	console.log("DONT RENDER PAGINATION, one page or less, totalResults: " + this.totalResults + ", pageSize: " + this.pageSize);
        	return;
        }
//        for (var i = 0; i < this.numberOfPagesToDisplay && i < limitOfPages; i++) {
        var cellsNumber = (this.numberOfPagesToDisplay + this.firstPageNumber) > this.totalPages ? 
        		(this.totalPages % this.numberOfPagesToDisplay) : this.numberOfPagesToDisplay;
        
//		console.log("Pagination.render numberOfPagesToDisplay: " + this.numberOfPagesToDisplay + 
//        		", firstPageNumber: " + this.firstPageNumber + ", totalPages: " + this.totalPages +", cellsNumber: " + cellsNumber);
        for (var i = 0; i < cellsNumber ; i++) {
//        	console.log("Pagination.render.iteration: " + i + ", " + this.firstPageNumber);
        	cell = row.insertCell(row.cells.length);
        	cell.style.paddingRight = "5px";
        	cell.style.paddingLeft = "5px";
			var page = this.firstPageNumber + i;
			cell.innerHTML = (page + 1) + "";

			cell.pagination = this;
			if (page >= 0 && page <= this.totalPages);
			{
				//when hovering on the label the mouse pointer will change 
				//from arrow to a hand
				//don't allow hyper-link on the selected item
				if ((this.firstPageNumber + i) != this.currentPageNumber)
				{
					cell.setAttribute("page",page);
					cell.style.cursor = "pointer";
					cell.className = this.classOfUnselectedLabel;
					dojo.connect(cell,"click",this.onPageClick);
					
				}
				else{
					cell.className = this.classOfSelectedLabel;
				}
			}
		}
      //------------------------------------------------------------------

		//link of previous page
        cell = row.insertCell(0);
		cell.innerHTML = this.stepBackwordText;
		cell.pagination = this;
		cell.className = this.classNextBack;
		
		var prevPage = this.currentPageNumber - 1;
		if (prevPage >= 0)
		{ //do not send negative values to the server
			cell.style.cursor =  "pointer";
			cell.setAttribute("page",prevPage);
			cell.style.cursor = "pointer";
			dojo.connect(cell,"click",this.onPageClick);
		}
		
		//-----------------------------------------------------------------

		//the link for the next page
		cell = row.insertCell(row.cells.length);
		cell.innerHTML = this.stepForwardText;
		cell.pagination = this;
		cell.className = this.classNextBack;
		
		var nextPage = this.currentPageNumber + 1; //point to the next page
//		console.log("Pagination::::" + nextPage + ", " + this.totalPages);
		if (nextPage < this.totalPages)
		{
			cell.style.cursor = "pointer";
			
			cell.setAttribute("page",nextPage);
			cell.style.cursor = "pointer";
			dojo.connect(cell,"click",this.onPageClick);
		}
		
		//-----------------------------------------------------------------

//		//the image linking to the previous page/page
//		cell = row.insertCell(0);
//		cell.pagination = this;
//		
//		cell.style.cursor = "pointer";
//		if (this.domNode.style.direction == "rtl") {
//			cell.innerHTML = this.forwordImageUrl;
//		}
//		else {
//			cell.innerHTML = this.backwardImageUrl;
//		}
//
////		if (this.currentPageNumber - 1 >= 0)
////		{
////			javascriptFunction = createActionFunction(
////					pager.getActionFunction(),
////					(pager.getcurrentPageNumber() - 1) * this.pageSize,
////					this.pageSize
////				);
////			backwardImage.setOnclick(javascriptFunction);
////		}
        
		//-----------------------------------------------------------------

//		//the image linking to the next page
//		cell = row.insertCell(row.cells.length);
//		cell.style.cursor = "pointer";
//
//		if (this.domNode.style.direction == "rtl") {
//			cell.innerHTML = this.backwardImageUrl;
//		}
//		else {
//			cell.innerHTML = this.forwordImageUrl;
//		}
//		if (this.currentPageNumber + 1 <= this.totalPages)
//		{
////			javascriptFunction = createActionFunction(
////					pager.getActionFunction(),
////					(pager.getcurrentPageNumber() + 1) * this.pageSize,
////					this.pageSize
////				);
////			forwardImage.setOnclick(javascriptFunction);
//		}
		
		//-----------------------------------------------------------------

		cell = row.insertCell(0);
		cell.innerHTML = this.lastBlockText;
//		if (this.currentPageNumber - this.blockSize >= 0)
//		{
//			javascriptFunction = createActionFunction(
//					pager.getActionFunction(),
//					(pager.getcurrentPageNumber() - pager.getBlockSize()) * this.pageSize,
//					this.pageSize
//				);
//			lastBlockLabel.setStyle("cursor: pointer");
//			lastBlockLabel.getAttributes().put("onclick", javascriptFunction);
//		}
		
		//-----------------------------------------------------------------

		cell = row.insertCell(row.cells.length);
		cell.innerHTML = this.nextBlockText;
		cell.setAttribute("title",this.nextBlockToolTip);
//		if (this.currentPageNumber + this.blockSize <= this.totalPages)
//		{
//			javascriptFunction = createActionFunction(
//					pager.getActionFunction(),
//					(pager.getcurrentPageNumber() + pager.getBlockSize()) * this.pageSize,
//					this.pageSize
//				);
//			nextBlockLabel.setStyle("cursor: pointer");
//			nextBlockLabel.getAttributes().put("onclick", javascriptFunction);
//		}
	}
});

}