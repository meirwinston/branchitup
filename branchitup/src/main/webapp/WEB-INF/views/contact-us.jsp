<%@page import="com.branchitup.json.JacksonUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="func" uri="/WEB-INF/tld/functions.tld" %>
<%@ taglib prefix="st" uri="/WEB-INF/tld/simple-tags.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html> <!-- HTML5 syntax -->
<html>
  <head>
    <meta charset="UTF-8"> <!-- HTML5 syntax -->
    <style>		 
    	@import "resources/js/dojo-release-1.8.1/dijit/themes/tundra/tundra.css";
		@import "resources/css/main.css";
		@import "resources/css/genre-editor.css";
	</style>
	<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
  	<title>Contact Us</title>
  	<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
    <script src="resources/js/branchitup/main.js"></script>
    <script></script>
    <script src="resources/js/branchitup/google-analytics.js"></script>
  </head>
  <body class="tundra contactus">
  	<c:if test="${not noHeader}">
  	<header><st:header id="branchitup_header" /></header>
  	</c:if>
  	
  	<div id="contactus.toolbar"></div>
  	<div id="contactus.centerPane">
  		<h2>Contact Us</h2>
  		<form jsId="contactusForm" data-dojo-type="dijit.form.Form" data-dojo-props="method: 'get'">
  		<table name="layout">
  			<tr>
  				<td class="blueHeading3">Contact:</td>
  				<td>Input your email address</td>
  			</tr>
  			<tr>
  				<td></td>
  				<td id="contactus.emailInputContainer"></td>
  			</tr>
  			<tr>
  				<td class="blueHeading3">Subject:</td>
  				<td>Input the subject <small>((50 characters)</small>: </td>
  			</tr>
  			<tr>
  				<td></td>
  				<td id="contactus.subjectInputContainer"></td>
  			</tr>
  			<tr><td colspan="2" name="space"></td></tr>
  			<tr>
  				<td class="blueHeading3">Comments:</td>
  				<td>Input your comments below: <small>(1024 characters)</small></td>
  			</tr>
  			<tr>
  				<td></td>
  				<td id="contactus.commentsInputContainer"></td>
  			</tr>
  			<tr><td colspan="2" name="space"></td></tr>
  			<tr>
  				<td class="blueHeading3">Challenge:</td>
					<td name="imageCell"><img id="contactus.captchaImg" src="imageservice?target=CAPTCHA" /></td>
				</tr>
				<tr>
					<td></td>
					<td id="contactus.challengeCell"></td>
				</tr>
  			<tr><td colspan="2" name="space"></td></tr>
  			<tr>
  				<td></td> <!-- dont call the method 'submit' safari reserves this operation to itself -->
  				<td><img id="contactus.submitButton" name="submitButton" src="resources/images/buttons/submit-button_115x25.png" onmouseover="this.src = 'resources/images/buttons/submit-button-hover_115x25.png'" onmouseout="this.src = 'resources/images/buttons/submit-button_115x25.png'" onclick="submitComments();" /></td>
  			</tr>
  		</table>
  		<input type="hidden" name="fileItemId" jsId="fileItemIdInput"  data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="name: 'fileItemId', type: 'hidden'" />
  		</form>
  	</div>
	  <script src="resources/js/branchitup/contact-us.js" ></script>
  </body>
</html>