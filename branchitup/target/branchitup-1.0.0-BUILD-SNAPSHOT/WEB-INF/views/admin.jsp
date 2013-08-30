<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="st" uri="/WEB-INF/tld/simple-tags.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Admin</title>
<script src="resources/js/branchitup/google-analytics.js"></script>
</head>
<body>
<form action="/__deleteUser" method="get">
Delete User By Account ID
<input id="userAccountId" type="text" />
<input name="challenge" type="hidden" value="${challenge}" />
<input type="submit" value="Submit" />
</form>
<br />

<form action="/branchitup/sendIntroMail" method="post">
Send Introduction Mail
<input name="email" type="text" style="width: 500px;" />
<input name="challenge" type="hidden" value="${challenge}" />
<input type="submit" value="Submit" />
</form>


</body>
</html>