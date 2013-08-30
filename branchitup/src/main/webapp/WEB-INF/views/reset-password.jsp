<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<style>		 
	@import "resources/js/dojo-release-1.8.1/dijit/themes/tundra/tundra.css";
	@import "resources/css/reset-password.css";
</style>
<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
<meta charset="UTF-8">
<title>Submit New Password</title>
<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
<script src="resources/js/branchitup/main.js"></script>
<script src="resources/js/branchitup/google-analytics.js"></script>
</head>
<body class="tundra resetpassword" >

<!-- submit-button_88x30.png forgot-password-label_294x24.png  -->
<div name="logo"><a href="home"><img id="branchitupLogo" src="resources/images/branchitup-logo_190x50.png" /></a></div>
<table name="layoutTable" class="loginLayoutTable">
<tbody>
<tr>
	<td align="center" class="title"><img src="resources/images/reset-your-password-label_300x30.png" /></td>
</tr>
<tr>
	<td class="description">Enter your new password below:</td>
</tr>
<tr>
	<td id="newPasswordCell"></td>
</tr>
<tr>
	<td class="description">Retype password:</td>
</tr>
<tr>
	<td id="confirmPasswordCell"></td>
</tr>
<tr>
	<td id="resetpassword.messagesCell"></td>
</tr>
<tr>
	<td name="submitCell"><img class="enabledButton" id="resetpassword.submitButton" name="submitButton" onclick="submit()" src="resources/images/buttons/submit-button_115x25.png" onmouseover="this.src = 'resources/images/buttons/submit-button-hover_115x25.png'" onmouseout="this.src = 'resources/images/buttons/submit-button_115x25.png'"/></td>
</tr>
</tbody>
</table>
<input type="hidden" id="tokenInput" value="1234">
<script type="text/javascript" src="resources/js/branchitup/reset-password.js"></script>
</body>
</html>
