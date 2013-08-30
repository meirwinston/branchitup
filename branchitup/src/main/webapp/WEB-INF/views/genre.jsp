<%@page import="com.branchitup.json.JacksonUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="func" uri="/WEB-INF/tld/functions.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="st" uri="/WEB-INF/tld/simple-tags.tld" %>
<!DOCTYPE html> <!-- HTML5 syntax -->
<html>
  <head>
    <meta charset="UTF-8"> <!-- HTML5 syntax -->
    <style>		 
    	@import "resources/js/dojo-release-1.8.1/dijit/themes/tundra/tundra.css";
			@import "resources/css/main.css";
			@import "resources/css/genre.css";
		</style>
	<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
  	<title>${genre.name}</title>
  	<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
    <script src="resources/js/branchitup/main.js"></script>
    <script src="resources/js/branchitup/google-analytics.js"></script>
  </head>
  <body class="tundra genre">
  	<header><st:header id="branchitup_header" page="genre" /></header>
  	<div id="genre.centerPane">
  		<%-- <img name='genreImage' src="imageservice?id=${genre.iconImageFileId}&thumbnail" /> --%>
  		<c:if test="${genre.iconImageUrl != null}">
  			<img name='genreImage' src="${genre.iconImageUrl}" />
  		</c:if>
  		<c:if test="${genre.iconImageUrl == null}">
  			<img name='genreImage' src="resources/images/no-image-branchitup_200x200.png" />
  		</c:if>
  		<span name='genreTitle'>${genre.name}</span><br/>
  		<span name='genreDate'>${func:formatDate(genre.createdOn)}</span>
  		<br/>
  		<span class="blueText">BranchItUp ID: ${genre.genreId}</span>
  		<p>${genre.description}</p>
  	</div>
    <script src="resources/js/branchitup/widgets/ItemsDropdown.js"></script>
	  <script src="resources/js/branchitup/genre.js" ></script>
	  <st:footer />
  </body>
</html>