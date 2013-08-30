<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<style>		 
	@import "resources/js/dojo-release-1.8.1/dijit/themes/tundra/tundra.css";
	@import "resources/css/restore-password.css";
</style>
<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
<meta charset="UTF-8">
<title>Forgot Password</title>
<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
<script src="resources/js/branchitup/main.js"></script>
<script src="resources/js/branchitup/google-analytics.js"></script>
</head>
<body class="tundra restorepassword" >
<!-- submit-button_88x30.png forgot-password-label_294x24.png  -->
<div name="logo"><a href="home"><img id="branchitupLogo" src="resources/images/branchitup-logo_190x50.png" /></a></div>
<table name="layoutTable" class="loginLayoutTable">
<tbody>
<tr>
	<td align="center" name="title"><img src="resources/images/forgot-password-label_294x24.png" /></td>
</tr>
<tr>
	<td name="descriptionCell">Enter your email address below: <small>(the same email address that is registered in your account)</small></td>
</tr>
<tr>
	<td name="emailAddressCell"><input jsId="emailInput" type="text" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="trim: true, placeHolder: 'Email', name: 'email', required: 'true', regExp: '[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}', invalidMessage: 'Invalid Email Address.'" /></td>
</tr>
<tr>
	<td id="restorepassword.messagesCell"></td>
</tr>
<tr>
	<td name="submitCell"><img class="enabledButton" id="restorepassword.submitButton" name="submitButton" onclick="submit()" src="resources/images/buttons/submit-button_115x25.png" onmouseover="this.src = 'resources/images/buttons/submit-button-hover_115x25.png'" onmouseout="this.src = 'resources/images/buttons/submit-button_115x25.png'"/></td>
</tr>
</tbody>
</table>
<script type="text/javascript" src="resources/js/branchitup/restore-password.js"></script>
</body>
</html>
