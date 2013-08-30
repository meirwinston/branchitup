<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag import="com.branchitup.service.ModelService"%>
<%@tag description="BranchItUp header" pageEncoding="UTF-8"%>
<%@attribute name="id"%>
<style>		 
		@import "resources/css/book-showcase.css";
</style>

<table id="bookshowcase" class="bookShowcase">
	<tr>
		<td colspan="3" id="bookShowcase.header" name="header"></td>
	</tr>
	<tr>
		<td name="navigate"><img id="bookShowcase.navigatePrev" src="resources/images/go-left-button_20x45.png"/></td>
		<td>
			<h4>Most Downloaded...</h4>
			<div name="imagePanel"><img id="bookShowcase.coverImage" name="coverImage" src="resources/images/no-image-branchitup_200x200.png"></div>
			<p id="bookShowcase.bookDescription" name="bookDescription"></p> 
		</td>
		<td name="navigate"><img id="bookShowcase.navigateNext" src="resources/images/go-right-button_20x45.png" /></td>
	</tr>
	<tr>
		<td></td>
		<td><button jsId="readMoreButton" data-dojo-type="dijit.form.Button" data-dojo-props="label: 'Read More...', type: 'button'"></button></td>
		<td></td>
	</tr>
</table>
<script src="resources/js/branchitup/book-showcase.js"></script>