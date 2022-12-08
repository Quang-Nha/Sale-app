<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User   : Admin
  Date   : 28/11/2022
  Time   : 5:39 SA
  Author : LeNha
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<h1 class="text-center text-danger">ĐĂNG NHẬP</h1>

<%-- nếu có param username tức là đã có lỗi xảy ra do security xử lý đăng nhập forward lại trang này
    nên có param username từ input của form, hiển thị lỗi --%>
<c:if test="${param.username != null}">
    <div class="alert alert-danger">
        Đã có lỗi xảy ra, vui lòng quay lại sau!
    </div>
</c:if>

<%-- nếu có param accessDenied tức không có quyền truy cập gì đó và trả về trang này với param đó --%>
<c:if test="${param.accessDenied != null}">
    <div class="alert alert-danger">
        Bạn không có quyền truy cập với tài khoản này
        <br>
        Đăng nhập bằng tài khoản khác đủ quyền truy cập
    </div>
</c:if>

<c:url value="/login" var="action"/>

<%-- form đăng nhập --%>
<form method="post" action="${action}">
    <div class="form-text">
        <label for="username">Username</label>
        <%-- nếu có lỗi sẽ forward lại trang này nên sẽ có các param input này đã gửi đi
            set lại giá trị cho input bằng chính giá trị nó đã gửi đi, ko có param thì giá trị là rỗng --%>
        <input type="text" id="username" required="required" name="username" class="form-control" autofocus
               value="${param.username}">
    </div>
    <div class="form-text">
        <label for="password">Password</label>
        <input type="password" id="password" required="required" name="password" class="form-control"
               value="${param.password}" >
    </div>
    <br>
    <div class="form-text">
        <input type="submit" value="Đăng nhập" class="form-control btn btn-danger">
    </div>
</form>
