<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>		 
	@import "resources/js/dojo-release-1.8.1/dijit/themes/tundra/tundra.css";
	@import "resources/css/login.css";
</style>
<style type="text/css">
.loginLayoutTable{
	
}

.loginLayoutTable td{
	
}
</style>
<meta charset="UTF-8">
<title>Login to Branch It Up</title>
<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
<script src="resources/js/branchitup/main.js"></script>
<script type="text/javascript">
	dojo.require("dijit.form.ValidationTextBox");
	dojo.require("dijit.form.TextBox");
	dojo.require("dijit.form.HorizontalSlider");
	dojo.require("dijit.form.Form");
	dojo.require("dojoui.Button");
	dojo.require("dojox.form.PasswordValidator");
</script>
</head>
<body class="tundra" >

<table name="layoutTable" class="loginLayoutTable">
<thead>
	<tr>
	<td colspan="2" name="headerCell"><img alt="yBlob" src="resources/images/y-blob-logo123x44.png"><span name="welcomeMessage">Welcome to yBlob!</span></td>
	</tr>
	<tr>
	<td colspan="2" style="border-bottom: 1px solid #5f6d81;"></td>
	</tr>
</thead>
<tbody>
	<tr><td colspan="2" style="color: red; font-weight: bold;">THIS IS A TEST PAGE</td></tr>
	<tr>
	<td style="width: 50%;">
		<form jsId="signupForm" method="get" data-dojo-type="dijit.form.Form" data-dojo-props="onSubmit: singupUser, method: 'get', action: 'branchitup_realm'">
		<input type="hidden" name="action" value="signup">
		<table>
			<tr>
			<td colspan="2" name="title">Sign Up</td>
			</tr>
			<tr><td colspan="2"><img src="resources/images/gradientLine400x1.png" /></td></tr>
			<tr>
			<td>User Name*:</td>
			<td>
			<input name="userName" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="name: 'userName',trim: true, placeHolder: 'type in user name', maxLength: 32, minLength: 6" />
			</td>
			</tr>
			<tr><td id="login.userNameErrorNode" colspan="2" style="color: red;"></td></tr>
			<tr><td colspan="2"><img src="resources/images/gradientLine400x1.png" /></td></tr>
			<tr>
			<td>First Name*:</td>
			<td>
			<input jsId="firstNameInput" name="firstName" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="name: 'firstName', trim: true, placeHolder: 'First Name', maxLength: 32, propercase: true, regExp: '[a-zA-Z -]+'" />
			</td>
			</tr>
			<tr><td id="login.firstNameErrorNode" colspan="2" style="color: red;"></td></tr>
			<tr><td colspan="2"><img src="resources/images/gradientLine400x1.png" /></td></tr>
			<tr>
			<td>Last Name*:</td>
			<td>
			<input jsId="lastNameInput" name="lastName" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="name: 'lastName', trim: true, placeHolder: 'Last Name', maxLength: 50, propercase: true, regExp: '[a-zA-Z ]+'" />
			</td>
			</tr>
			<tr><td id="login.lastNameErrorNode" colspan="2" style="color: red;"></td></tr>
			<tr><td colspan="2"><img src="resources/images/gradientLine400x1.png" /></td></tr>
			<tr>
			<td>Password*:</td>
			<td>
			<input jsId="passwordInput" type="password" name="password" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="name: 'password', trim: true, placeHolder: 'password', type: 'password', maxLength: 32" />
			</td>
			</tr>
			<tr><td id="login.passwordErrorNode" colspan="2" style="color: red;"></td></tr>
			<tr><td colspan="2"><img src="resources/images/gradientLine400x1.png" /></td></tr>
			<tr>
			<td>Retype Password*:</td>
			<td>
			<input jsId="passwordConfirmInput" type="password" name="passwordConfirm" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="name: 'passwordConfirm', trim: true, placeHolder: 'retype your password', type: 'password', maxLength: 50, minSize: 20" />
			</td>
			</tr>
			<tr><td id="login.repasswordErrorNode" colspan="2" style="color: red;"></td></tr>
			<tr><td colspan="2"><img src="resources/images/gradientLine400x1.png" /></td></tr>
			<tr>
			<td>Email Address*:</td>
			<td>
			<input jsId="emailAddressInput" name="email" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="name: 'email', trim: true, placeholder: 'your email address', required: 'true', regExp: '[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}', invalidMessage: 'Invalid Email Address.'" />
			</td>
			</tr>
			<tr><td id="login.emailErrorNode" colspan="2" style="color: red;"></td></tr>
			<tr><td colspan="2"><img src="resources/images/gradientLine400x1.png" /></td></tr>
			<tr>
			<td>Gender</td>
			<td>
			<table style="padding: 0px; border-spacing: 0px;">
				<tr>
					<td><img src="resources/images/female12x24.png" /></td>
					<td style="vertical-align: middle;"><input id="genderInput" type="hidden" name="gender" ><span jsId="genderSlider" data-dojo-type="dijit.form.HorizontalSlider" data-dojo-props="minimum: 0, maximum: 1, value: 0, intermediateChanges: true, style: 'width: 180px;', discreteValues: 5" /></td>
					<td><img src="resources/images/male8x24.png" /></td>
				</tr>
			</table>
			</td>
			</tr>
			<tr><td colspan="2"><img src="resources/images/gradientLine400x1.png" /></td></tr>
			<tr><td id="messageCell" colspan="2" style="display: none; color: red;"></td></tr>
			<tr>
			<td></td>
			<td>
			<button disabled="disabled" name="signup" data-dojo-type="dojoui.Button" data-dojo-props="label: 'Sign Up', type: 'button', onClick: singupUser"></button>
			</td>
			</tr>
		</table>
		</form>
	</td>
	<td style="50%">
		<form method="POST" data-dojo-type="dijit.form.Form"  data-dojo-props="action: '/j_spring_security_check'">
		<table>
			<tr>
			<td colspan="2" name="title">Sign In</td>
			</tr>
			<tr><td colspan="2"><img src="resources/images/gradientLine400x1.png" /></td></tr>
			<tr>
			<td>User Name: </td>
			<td>
			<input type="text" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="trim: true, placeHolder: 'type in your username', name: 'j_username', id: 'j_username'" />
			</td>
			</tr>
			<tr><td colspan="2"><img src="resources/images/gradientLine400x1.png" /></td></tr>
			<tr>
			<td>Password: </td>
			<td>
			<input type="password" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="trim: true, placeHolder: 'type in your password', name: 'j_password', id: 'j_password', type: 'password'" />
			</td>
			</tr>
			<tr><td colspan="2"><img src="resources/images/gradientLine400x1.png" /></td></tr>
			<tr>
			<td></td>
			<td>
			<button type="submit" name="login" data-dojo-type="dojoui.Button" data-dojo-props="label: 'Sign In', type: 'submit'"></button>
			</td>
			</tr>
		</table>
		</form>
	</td>
	</tr>
</tbody>
<tfoot>
	<tr>
	<td colspan="2" style="border-bottom: 2px solid #5f6d81;"></td>
	</tr>
</tfoot>
</table>
<script type="text/javascript" src="resources/js/branchitup/login.js"></script>
</body>
</html>

<!-- 
<form action="j_security_check" method="get">
		<table cellpadding="0" cellspacing="0" border="1" style="width: 100%;">
			<tbody>
			<tr>
				<td>Email:&nbsp;<input type="text" name="j_username" id="j_username" value="meir" style="border: 1px solid blue;"></input></td>
        		<td>Password:&nbsp;<input type="password" name="j_password" id="j_password" value="meir"></input></td>
        		<td><input id="loginButton" type="submit" name="login" id="login" >Login</input></td>
			</tr>
			</tbody>
		</table>
	</form>
 -->