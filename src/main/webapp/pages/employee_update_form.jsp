<%@ page import="org.example.app.entity.Employee" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Update Employee</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <script src="${pageContext.request.contextPath}/js/hideMessage.js"></script>
</head>
<body>
<c:if test="${not empty errors}">
    <div class="error-msg">${errors}</div>
</c:if>
<div class="container">
    <h1>Update Employee</h1>

    <form action="update" method="post" class="form-style">
        <input type="hidden" name="id" value="${employee.id}">
        <input type="hidden" name="employeeId" value="${employee.employeeId}">
        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" name="firstName" value="${employee.firstName}" required>
        <c:if test="${not empty validationFormErrors['firstName']}">
            <div class="error-msg-validate">${validationFormErrors['firstName']}</div>
        </c:if>
        <br><br>
        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" name="lastName" value="${employee.lastName}" required>
        <c:if test="${not empty validationFormErrors['lastName']}">
            <div class="error-msg-validate">${validationFormErrors['lastName']}</div>
        </c:if>
        <br><br>
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" value="${employee.email}" required>
        <c:if test="${not empty validationFormErrors['email']}">
            <div class="error-msg-validate">${validationFormErrors['email']}</div>
        </c:if>
        <br><br>
        <label for="phone">Phone:</label>
        <input type="text" id="phone" name="phone" value="${employee.phone}" required>
        <c:if test="${not empty validationFormErrors['phone']}">
            <div class="error-msg-validate">${validationFormErrors['phone']}</div>
        </c:if>
        <br><br>
        <label for="position">Position:</label>
        <input type="text" id="position" name="position" value="${employee.position}" required>
        <c:if test="${not empty validationFormErrors['position']}">
            <div class="error-msg-validate">${validationFormErrors['position']}</div>
        </c:if>
        <br><br>
        <label for="isWork">Is Working:</label>
        <c:choose>
            <c:when test="${employee.isWork}">
                <select id="isWork" name="isWork">
                    <option value="true" selected>Yes</option>
                    <option value="false">No</option>
                </select>
            </c:when>
            <c:otherwise>
                <select id="isWork" name="isWork">
                    <option value="true">Yes</option>
                    <option value="false" selected>No</option>
                </select>
            </c:otherwise>
        </c:choose>
        <c:if test="${not empty validationFormErrors['isWork']}">
            <div class="error-msg-validate">${validationFormErrors['isWork']}</div>
        </c:if>
        <br><br>
        <button type="submit" class="button button-submit">Update</button>
    </form>
    <a href="/" class="button button-return">Back to List</a>
</div>
</body>
</html>
