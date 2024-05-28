<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<h1>Error Occurred</h1>
<div class="container container-error">
    <div class="error-msg"><%=exception.getMessage() %></div>
    <a href="/" class="button button-return">Back to List</a>
</div>
</body>
</html>
