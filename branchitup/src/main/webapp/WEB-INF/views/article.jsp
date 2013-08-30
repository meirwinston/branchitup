<%@page import="com.branchitup.json.JacksonUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
		@import "resources/css/article.css"
	</style>
	<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
  	<title>${sheet.name}</title>
  	
  	<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
    <script src="resources/js/branchitup/main.js"></script>
    <script src="resources/js/branchitup/google-analytics.js"></script>
  </head>
  <body class="tundra">
  	<header><st:header id="branchitup_header" page="genre" /></header>
		<div id="centerPane">1
		<table border="0" id="layoutTable">
		<tr>
		<td> </td>
		<td><h1 class="blogTitle">${sheet.blog.title}</h1>
		<h3 class="blogSubtitle">${sheet.blog.subtitle}</h3>
		
		<h3 class="articleTitle">${sheet.name}</h3>
		<h6 class="byline">By <a title="${sheet.ownerAccount.firstName}&nbsp;${sheet.ownerAccount.lastName}" href="userprofile?userAccountId=${sheet.ownerAccountId}">${sheet.ownerAccount.firstName}&nbsp;${sheet.ownerAccount.lastName}</a></h6>
		<h6 class="byline">Published ${func:formatDate(sheet.publishedOn)}</h6>
		</td>
		<tr>
		<td></td>
		<td class="articleBody">${sheet.content}</td>	
		</tr>
		<tr>
			<td></td>
			<td><label id="commentsTitleLabel">Comments</label><div id='article.commentFormPane'></div><div id='article.commentsPane'></div>
			<div id='article.paginationPane'></div></td>
		</tr>
		</table>
		<div>
			<footer>
			<div id="disclaimer">${sheet.blog.footer}</div>
			</footer>
		</div>  
		</div>
		<script src="resources/js/branchitup/widgets/ItemsDropdown.js"></script>
		<script src="resources/js/branchitup/article.js"></script>
		<script>
    	articleId = ${sheet.sheetId};
    </script>
  </body>
</html>