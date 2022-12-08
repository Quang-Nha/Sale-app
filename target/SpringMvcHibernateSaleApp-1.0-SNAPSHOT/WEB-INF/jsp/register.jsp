<%--
  Created by IntelliJ IDEA.
  User   : Admin
  Date   : 28/11/2022
  Time   : 10:46 CH
  Author : LeNha
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1 class="text-center text-danger">ĐĂNG KÝ</h1>

<%-- in ra lỗi thêm account vào csdl nếu có --%>
<c:if test="${errMsg != null}">
    <div class="alert-danger alert">${errMsg}</div>
</c:if>

<%-- form đăng ký --%>
<form:form method="post" action="" modelAttribute="user" enctype="multipart/form-data">
    <div class="form-text">
        <label for="firstName" class="text-black">FirstName</label>
        <form:input type="text" id="firstName" required="required" path="firstName" class="form-control" autofocus="autofocus"
                    placeholder = "Enter firstName"/>
        <br>
    </div>
    <div class="form-text">
        <label for="lastName" class="text-black">LastName</label>
        <form:input type="text" id="lastName" required="required" path="lastName" class="form-control" placeholder = "Enter lastName"/>
        <br>
    </div>
    <div class="form-text">
        <label for="email" class="text-black">Mail</label>
        <form:input type="email" required="required" id="email" path="email" class="form-control" placeholder = "Enter email"/>
        <br>
    </div>
    <div class="form-text">
        <label for="username" class="text-black">Username</label>
        <form:input type="text" id="username" required="required" path="username" class="form-control" placeholder = "Enter username"/>
        <br>
    </div>
    <div class="form-text">
        <label for="password" class="text-black">Password</label>
        <form:input type="password" id="password" required="required" path="password" class="form-control" placeholder = "Enter password"/>
        <br>
    </div>
    <div class="form-text">
        <label for="confirmPassword" class="text-black">Confirm password</label>
        <form:input type="password" id="confirmPassword" required="required" path="confirmPassword" class="form-control" placeholder = "Enter confirmPassword"/>
    </div>
    <form:errors path="confirmPassword" cssClass="alert alert-danger" element="div"/>
    <br>
    <div class="form-text">
        <label for="avatar" class="text-black">Avatar</label>
        <form:input type="file" id="avatar" required="required" path="file" class="form-control"/>
    </div>
    <form:errors path="avatar" cssClass="alert alert-danger" element="div"/>
    <br>
    <div class="form-text">
        <input type="submit" value="Đăng ký" class="font-weight-bold form-control btn btn-danger">
    </div>
</form:form>
