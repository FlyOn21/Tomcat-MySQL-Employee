<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Employee List</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<h1>Employee List</h1>
<a href="new" class="button create-button">Create New Employee</a>
<c:if test="${success-msg}">
    <div class="success-msg">${success-msg}</div>
<div class="table">
    <div class="header-row">
        <div class="header-cell">First Name</div>
        <div class="header-cell">Last Name</div>
        <div class="header-cell">Email</div>
        <div class="header-cell">Phone</div>
        <div class="header-cell">Position</div>
        <div class="header-cell">Is Working</div>
        <div class="header-cell">Actions</div>
    </div>

    <c:if test="${empty listEmployee}">
        <div class="data-row">
            <div class="data-not-found" colspan="7">No data available.</div>
        </div>
    </c:if>

    <c:forEach var="employee" items="${listEmployee}">
        <div class="data-row">
            <div class="data-cell">${employee.firstName}</div>
            <div class="data-cell">${employee.lastName}</div>
            <div class="data-cell">${employee.email}</div>
            <div class="data-cell">${employee.phone}</div>
            <div class="data-cell">${employee.position}</div>
            <div class="data-cell">${employee.isWork ? 'Yes' : 'No'}</div>
            <div class="data-cell actions">
                <a href="edit?id=${employee.employeeId}" class="button edit-button">Edit</a>
                <form action="delete" method="POST" onsubmit="return confirm('Are you sure?')" style="display: inline;">
                    <input type="hidden" name="employeeId" value="${employee.employeeId}" />
                    <button type="submit" class="button delete-button">Delete</button>
                </form>
            </div>
        </div>
    </c:forEach>
</div>

</body>
</html>