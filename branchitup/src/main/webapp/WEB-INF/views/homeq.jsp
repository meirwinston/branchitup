<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html> <!-- HTML5 syntax -->
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script src="resources/js/jquery/jquery-1.9.1.min.js"></script>
<script src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>
<title>Branch It Up</title>
</head>
<body>

<span id="aa"></span>
--
<span id="bb"></span>
--
<div id="cc"></div>
<script>
$('#aa').text("HHH");
$('#bb').append($("<span>",{id: "myid", text: "mytext"}));
$('#cc').append($("<div>",{html: "my<b>text</b>"}));
</script>

++++

<script>
$(function() {
	var availableTags = [
	"ActionScript",
	"AppleScript",
	"Asp",
	"BASIC",
	"C",
	"C++",
	"Clojure",
	"COBOL",
	"ColdFusion",
	"Erlang",
	"Fortran",
	"Groovy",
	"Haskell",
	"Java",
	"JavaScript",
	"Lisp",
	"Perl",
	"PHP",
	"Python",
	"Ruby",
	"Scala",
	"Scheme"
	];
	$( "#tags" ).autocomplete({
		source: availableTags
		}).keypress(function(e){console.log("key pressed" + e);});
	
	});
</script>

<div class="ui-widget">
<label for="tags">Tags: </label>
<input id="tags" />
</div>
</body>
</html>