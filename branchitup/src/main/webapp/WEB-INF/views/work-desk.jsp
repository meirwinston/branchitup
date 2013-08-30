<%@page import="com.branchitup.json.JacksonUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="st" uri="/WEB-INF/tld/simple-tags.tld" %>
<%@ taglib prefix="func" uri="/WEB-INF/tld/functions.tld" %>
<!DOCTYPE html> <!-- HTML5 syntax -->
<html>
  <head>
    <meta charset="UTF-8"> <!-- HTML5 syntax -->
    <style>		 
    	@import "resources/js/dojo-release-1.8.1/dijit/themes/tundra/tundra.css";
		@import "resources/css/main.css";
		@import "resources/css/work-desk.css";
	</style>
	<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
  	<title>Work Desk</title>
  	<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
    <script src="resources/js/branchitup/main.js"></script>
    <script src="resources/js/branchitup/google-analytics.js"></script>
  </head>
  <body class="tundra workdesk">
  	<header><st:header id="branchitup_header" page="workdesk" /></header>
  	<!-- <table id="workdesk.toolbar" class="toolbar">
		<tbody>
		<tr>
			<td id="workdesk.paginationCell"></td>
			<td id="toolbar.passage" ></td>
			<td id="toolbar.searchBox" class="toolbarRightCell"></td>
			<td id="toolbar.searchButton" class="toolbarRightCell">
				<button>Search</button>
			</td>
		</tr>
		</tbody>
	</table> -->
  	<div id="workdesk.centerPane">
  		<h2 id="workdesk.pageTitle">Your Work Desk</h2>
  		<div id="workDesk.navigationPane" class="sectionDividor">
  			<label class='blueHeading4'>Click To Go To:</label><br/><br/>
  			<img src="resources/images/sheet_21x20.png" /><a href="#sheetsSection" class='blueHeading4'>Your Sheets</a>
  			<img src="resources/images/book_20x20.png" /><a href="#booksSection" class='blueHeading4'>Your Books</a>
  			<img src="resources/images/published-book_20x20.png" /><a href="#publicationsSection" class='blueHeading4'>Your Publications</a>
  			
  		</div>
  		<div id="workDesk.sheetsHeader">
  			<a name="sheetsSection" class='blueHeading4'>Your Sheets </a><label class='blueHeading4' id="workdesk.folderLabel"></label>
  		</div>
  		<div id="workDesk.sheetsPane" class="sectionDividor">
  		NO ITEMS ARE AVAILABLE&nbsp;<a href='javascript:void openSheetEditor()'>Create new Sheet</a>
  		</div>
  		
  		<div id="workDesk.booksHeader">
  			<a name="booksSection" class='blueHeading4'>Your Books </a>
  		</div>
  		<div id="workDesk.booksPane" class="sectionDividor"></div>
  		
  		<div id="workDesk.publicationsHeader">
  			<a name="publicationsSection" class='blueHeading4'>Your Publications </a>
  		</div>
  		<div id="workDesk.publicationsPane" class="sectionDividor"></div>
  	</div>
    <script src="resources/js/branchitup/widgets/ItemsDropdown.js"></script>
	  <script src="resources/js/branchitup/work-desk.js" ></script>
	  <script>
	  	dojo.addOnLoad(function(){
	  		//setSheets(${func:serialize(sheets)});
	  		//setBooks(${func:serialize(books)});
	  		setFolders(${folders});
	  		blogs = ${blogs};
	  		console.log("HRERERE blogs");
	  		console.log(blogs);
	  	});
	  </script>
  </body>
</html>