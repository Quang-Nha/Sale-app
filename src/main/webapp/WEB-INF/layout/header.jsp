<%--
  Created by IntelliJ IDEA.
  User   : Admin
  Date   : 26/11/2022
  Time   : 4:26 SA
  Author : LeNha
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<nav class="navbar navbar-expand-md bg-dark navbar-dark"
     style="padding: 0 2% 0 2%;position: fixed; top: 0;right: 0;left: 0;z-index: 2">
    <!-- Brand -->
    <a class="navbar-brand font-weight-bold text-light" href="<c:url value="/"/>"
       style="padding-left: 15px; font-size: 28px">FUNix</a>

    <!-- Toggler/collapsibe Button -->
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
        <span class="navbar-toggler-icon"></span>
    </button>

    <!-- phần danh mục sản phẩm -->
    <div style="font-size: 16px;"
         class="collapse navbar-collapse font-weight-bold " id="collapsibleNavbar"
    >
        <div style="width: 80%">
            <div class="row">
                <div class="col-md-9" style="display:flex;justify-content: left; align-items: center;">
                    <ul class="navbar-nav">
                        <%--@elvariable id="categories" type="java.util.List"--%>
                        <c:forEach var="c" items="${categories}">
                            <c:url var="catPath" value="/">
                                <%--thêm param vào url--%>
                                <c:param name="cateId" value="${c.id}"/>
                            </c:url>
                            <%-- nếu param cateId = id của category đang duyệt thì cho nó màu nền xanh chữ trắng --%>
                            <li class="nav-item"  ${param.cateId == c.id ? "style='background-color:#FFE0B2;color:white'" : ""}>
                                <a class="nav-link text-info" href="${catPath}">${c.name}</a>
                            </li>
                        </c:forEach>

                        <li class="nav-item" style=";display:flex;justify-content: left; align-items: center;">
                            <a href="<c:url value="/cart"/> " class="nav-link text-success">
                                <i class="fa-solid fa-cart-plus fa-xl"></i>
                                <%-- hiển thị số sản phẩm có trong giỏ hàng --%>
                                <div class="badge badge-danger" id="cartCounter">${numProductsInCart}</div>
                            </a>
                        </li>

                        <%-- nếu tài khoản có quyền admin thì mới xem được trang thống kê --%>
                        <sec:authorize access="hasRole('ROLE_ADMIN')">
                            <li class="nav-item">
                                <a class="nav-link text-warning" href="<c:url value="/admin/category-stats"/> ">
                                    Thống kê
                                </a>
                            </li>
                        </sec:authorize>
                        <%-- nếu tài khoản có quyền admin thì mới xem được trang thống kê --%>
                        <sec:authorize access="hasRole('ROLE_ADMIN')">
                            <li class="nav-item">
                                <a class="nav-link" href="<c:url value="/admin/addProduct"/> ">
                                    <i class="fa-sharp fa-solid fa-plus text-warning"></i>
                                    <i class="fa-solid fa-mobile-screen-button text-warning fa-xl"></i>
                                </a>
                            </li>
                        </sec:authorize>
                    </ul>
                </div>

                <div class="col-md-3">
                    <%-- thanh tìm kiếm --%>
                    <nav class="navbar navbar-expand-sm bg-dark navbar-dark">
                        <form method="get" style="margin: 0; padding: 0;"
                              action="<c:url value="/"/>">
                            <div class="row">
                                <div class="col-md-8" style="padding: 0">
                                    <input class="form-control mr-sm-2" name="search" type="search" placeholder="Search"
                                           value="${param.search == null? "" : param.search}">
                                </div>
                                <div class="col-md-4">
                                    <button class="btn btn-success font-weight-bold" type="submit">Search</button>
                                </div>
                            </div>
                        </form>
                    </nav>
                </div>
            </div>
        </div>
        <div style="width: 20%">
            <ul class="navbar-nav table-info"
                style="display:flex;justify-content: center; align-items: center;border-radius: 50px;
                margin: 6px 0; float: right">
                <%-- lấy tên đăng nhập từ hệ thống security --%>
                <c:set var="username" value="${pageContext.request.userPrincipal.name}"/>

                <c:choose>
                    <%-- nếu tên không tồn tại thì chưa đăng nhập hiển thị link đăng nhập, đăng ký
                        ngược lại đẵ đăng nhập thì hiển thị link về trang chủ kèm tên tài khoản,
                        và logout có link đến trang tiến hành logout(/logout) của hệ thống, ko cần tạo controller của nó --%>
                    <c:when test="${username == null}">
                        <li class="nav-item">
                            <a href="<c:url value="/login"/> " class="nav-link text-danger"
                               style="display:flex;justify-content: center; align-items: center;">
                                <i class="fa fa-sign-in fa-xl"></i> Đăng nhập
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="<c:url value="/register"/> " class="nav-link text-danger"
                               style="display:flex;justify-content: center; align-items: center;">
                                <i class="fa fa-file-signature fa-xl"></i> Đăng ký
                            </a>
                        </li>

                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a href="<c:url value="/"/> " class="nav-link text-success"
                               style="display:flex;justify-content: center; align-items: center">
                                <img width="24" height="24" class="rounded-circle border border-white"
                                     src="${sessionScope.currentUser.avatar}" alt="avatar">
                                    ${fn:toUpperCase(username)}
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="<c:url value="/logout"/> " class="nav-link text-danger"
                               style="display:flex;justify-content: center; align-items: center;">
                                <i class="fa fa-sign-out fa-xl"></i> Logout
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>