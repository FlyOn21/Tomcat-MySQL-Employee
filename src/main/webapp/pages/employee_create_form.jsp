<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Create Employee</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <script src="${pageContext.request.contextPath}/js/hideMessage.js"></script>
</head>
<body>
<c:if test="${not empty errors}">
    <div class="error-msg">${errors}</div>
</c:if>
<div class="container">
    <h1>Create New Employee</h1>
    <form action="insert" method="post" class="form-style">
        <label for="firstName">First Name:</label>
        <input
                type="text" id="firstName" name="firstName" placeholder="Jone"  value="${not empty previousInput['firstName'] ? previousInput['firstName'] : null}"
               required
        >
        <c:if test="${not empty validationFormErrors['firstName']}">
            <div class="error-msg-validate">${validationFormErrors['firstName']}</div>
        </c:if>
        <br><br>

        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" name="lastName" placeholder="Doy" value="${not empty previousInput['lastName'] ? previousInput['lastName'] : null}" required>
        <c:if test="${not empty validationFormErrors['lastName']}">
            <div class="error-msg-validate">${validationFormErrors['lastName']}</div>
        </c:if>
        <br><br>

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" placeholder="example@ex.com" value="${not empty previousInput['email'] ? previousInput['email'] : null}" required>
        <c:if test="${not empty validationFormErrors['email']}">
            <div class="error-msg-validate">${validationFormErrors['email']}</div>
        </c:if>
        <br><br>

        <label for="phone">Phone:</label>
        <input type="text" id="phone" name="phone" placeholder="+380501234567" value="${not empty previousInput['phone'] ? previousInput['phone'] : null}" required>

        <c:if test="${not empty validationFormErrors['phone']}">
            <div class="error-msg-validate">${validationFormErrors['phone']}</div>
        </c:if>
        <br><br>

        <label for="position">Position:</label>
        <input type="text" id="position" name="position" placeholder="boss" value="${not empty previousInput['position'] ? previousInput['position'] : null}" required>
        <c:if test="${not empty validationFormErrors['position']}">
            <div class="error-msg-validate">${validationFormErrors['position']}</div>
        </c:if>
        <br><br>

        <label for="isWork">Is Working:</label>
        <select id="isWork" name="isWork">
            <option value="true">Yes</option>
            <option value="false">No</option>
        </select>
        <c:if test="${not empty validationFormErrors['isWork']}">
            <div class="error-msg-validate">${validationFormErrors['isWork']}</div>
        </c:if>
        <br><br>

        <button type="submit" class="button button-submit">Submit</button>
    </form>
    <a href="/" class="button button-return">Back to List</a>
</div>
</body>
</html>