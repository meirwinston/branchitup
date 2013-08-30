<%@page import="com.branchitup.json.JacksonUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="st" uri="/WEB-INF/tld/simple-tags.tld" %>
<!DOCTYPE html> <!-- HTML5 syntax -->
<html>
  <head>
    <meta charset="UTF-8"> <!-- HTML5 syntax -->
    <if-modified-since></if-modified-since>
    <style>		 
    	@import "resources/js/dojo-release-1.8.1/dijit/themes/tundra/tundra.css";
    	/* @import "resources/css/dojo-theme/tundra/tundra.css"; */
			@import "resources/css/main.css";
			@import "resources/css/home.css";
	</style>
		<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
		<link rel="image_src" href="resources/images/branch_47x50.png" />
  	<title>Write A Book Online</title>
  	<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
    <script src="resources/js/branchitup/main.js"></script>
    <script src="resources/js/branchitup/widgets/VPublicationItem.js"></script>
    <script type="text/javascript">var params = ${params};</script>
    <script src="resources/js/branchitup/google-analytics.js"></script>
  </head>
  <body class="tundra">
  	<st:header id="branchitup_header" page="home"/>
		<table name="layoutTable">
			<tr>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td id="__" style="vertical-align: top;">
				<div style="text-align: center;"><c:if test="${userAccount != null}"><img style="cursor: pointer;" src="resources/images/banner-publish-today_800x90.png" onclick="showPromotionDialog()" /></c:if></div>
					<!-- <div style="text-align: center;"><img style="cursor: pointer;" src="resources/images/branchitup-banner_728x79.gif" onclick="showPromotionDialog()" /></div> -->
					<!-- <div style="font-family: arial; color: white; background-image:url('resources/images/banner-background_800x90.jpg'); background-repeat: no-repeat; text-align: center; border: 2px solid grey; font-size: 16px; background-color: #a0b734; ">Branch It Up has now officially been launched on <img src='resources/images/kickstarter-logo-light_171x20.png'></img><br/>
					Please help us get the word out, more users means more people to read YOUR works!<br/>
					Post this URL on your Facebook or Twitter: <a href='http://www.kickstarter.com/projects/1750006060/branch-it-up?ref=email'>http://www.kickstarter.com/projects/1750006060/branch-it-up?ref=email</a></div> -->
					<div id="centerPane"></div>
				</td>
				<td id="rightPane"><st:book-showcase /><c:if test="${userAccount != null}"><br /><st:user-workdesk /></c:if></td>
			</tr>
			<tr>
				<td colspan="2" id="home.paginationCell"></td>
			</tr>
		</table>
		<st:footer />
    <script src="resources/js/branchitup/widgets/ItemsDropdown.js"></script>
		<script src="resources/js/branchitup/home.js" ></script>
  </body>
</html>