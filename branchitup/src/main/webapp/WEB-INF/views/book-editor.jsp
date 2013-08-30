<%@page import="com.branchitup.json.JacksonUtils"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="func" uri="/WEB-INF/tld/functions.tld" %>
<%@ taglib prefix="st" uri="/WEB-INF/tld/simple-tags.tld" %>
<!DOCTYPE html> <!-- HTML5 syntax -->
<html>
  <head>
  	<meta charset="UTF-8"> <!-- HTML5 syntax -->
    <style>		 
    	@import "resources/js/dojo-release-1.8.1/dijit/themes/tundra/tundra.css";
			@import "resources/css/main.css";
			@import "resources/css/book-editor.css";
	</style>
	<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
  	<title><c:if test="${book.title != null}">${book.title}</c:if><c:if test="${book.title == null}">Create New Book...</c:if></title>
  	<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
    <script src="resources/js/branchitup/main.js"></script>
    <script type="text/javascript">
    	var params = ${params};
    	dojo.require("dijit.form.ValidationTextBox");
    	dojo.require("dijit.form.Form");
    	dojo.require("dijit.form.Textarea");
    	dojo.addOnLoad(function(){
    		//bookTitleInput.set("value","${book.title}");
    		titleInput.set("value",${func:serialize(book.title)});
    		bookSummaryInput.set("value",${func:serialize(book.bookSummary)});
    		addSheets(${book.sheetList});
    		<c:if test="${not empty book.bookId}">
    			bookId = ${book.bookId};
    		</c:if>
    		
    	});
    </script>
    <script src="resources/js/branchitup/google-analytics.js"></script>
  </head>
  <body class="tundra bookeditor">
  <st:header id="branchitup_header" page="bookeditor" />
  <div id="bookeditor.centerPane">
		<h2><c:if test="${book != null}">Update Book</c:if><c:if test="${book == null}">Compile Book</c:if></h2>
		<form jsId="bookForm" data-dojo-type="dijit.form.Form" data-dojo-props="method: 'get'">
			<span class="blueHeading3">Book Title:&nbsp;</span>
			<span>Name your book here: </span>
			<br/>
			<span id="bookeditor.titleInputContainer"></span><small>(maximum 50 characters)</small>
			<br/>
			<br/>
			<span class="blueHeading3">Book Summary:&nbsp;</span>
			<span>Provide a brief summary of the book here <small>(maximum 1000 characters)</small></span>
			<br/>
			<span id="bookeditor.bookSummaryInputContainer"></span>
			<br/>
			<br/>
			<img src="resources/images/book_20x20.png" /><span id="bookeditor.title" class="blueHeading3" style="padding-left: 15px;"></span>
			<span id="bookeditor.sheetsContainer"></span>
			<br/>
			<span class="blueHeading3">Add Sheets:</span><span>&nbsp;&nbsp;Start typing the name of the sheet you wish to add to this book. Select sheet from the dropdown menu and press enter to add it to the book. Your compilation of sheets will appear above. To change the order of the sheets as they appear in the book, drag items to the desired location.</span>
			<c:choose>
		  	<c:when test="${userSheetsCount != null && userSheetsCount <= 0}">
		  		<span><br><b style="color: red;">You have no sheets in your work desk! </b>You must first <a href="sheeteditor">create a sheet</a> and save it. Then return to this Compile Book page and add your desired sheets.</span>
		  	</c:when>
		  	<c:otherwise>
		  		<span><br><a href="javascript:openWorkdeskBrowser()">Browse Your Work Desk</a></span>
		  	</c:otherwise>
	  	</c:choose>
			<br/>
			
			<span id="bookeditor.sheetsDropdownContainer"></span>
			<input id="bookeditor.visibilityInput" type="checkbox" checked="checked">Show results from my work desk only
			
			<br/><br/>
			<!--  -->
			<img id="bookeditor.submitButtonImg" onclick="submitBook()" src="resources/images/buttons/save-book-button_115x25.png" onmouseover="this.src = 'resources/images/buttons/save-book-button-hover_115x25.png'" onmouseout="this.src = 'resources/images/buttons/save-book-button_115x25.png'">
		</form>
	</div>
	<script src="resources/js/branchitup/widgets/ItemsDropdown.js" type="text/javascript"></script>
	<script src="resources/js/branchitup/widgets/ImageUploader.js" type="text/javascript"></script>
  <script src="resources/js/branchitup/book-editor.js" ></script>
  </body>
</html>