<%@page import="com.branchitup.json.JacksonUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="st" uri="/WEB-INF/tld/simple-tags.tld" %>
<!DOCTYPE html> <!-- HTML5 syntax -->
<html>
  <head>
    <meta charset="UTF-8"> <!-- HTML5 syntax -->
    <style>		 
    	@import "resources/js/dojo-release-1.8.1/dijit/themes/tundra/tundra.css";
		@import "resources/css/main.css";
	</style>
	<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
  	<title>Branch It Up</title>
    <script src="resources/js/branchitup/main.js"></script>
    <script src="resources/js/branchitup/google-analytics.js"></script>
  </head>
  <body class="tundra">
  	<st:header id="branchitup_header" />
	<table id="pagenotavailable.toolbar" class="toolbar">
		<tbody>
		<tr>
			<td id="home.paginationCell"></td>
			<td id="toolbar.passage" ></td>
			<td id="toolbar.searchBox" class="toolbarRightCell"></td>
			<td id="toolbar.searchButton" class="toolbarRightCell"></td>
		</tr>
		</tbody>
	</table>
	<div id="centerPane">
		<h1>Page Not Uvailable</h1>
	</div>
  </body>
</html>