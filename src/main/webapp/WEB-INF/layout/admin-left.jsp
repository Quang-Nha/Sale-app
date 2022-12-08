<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User   : Admin
  Date   : 04/12/2022
  Time   : 2:20 SA
  Author : LeNha
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- A vertical navbar -->
<nav class="navbar">

    <!-- Links -->
    <ul class="navbar-nav w-100 font-weight-bold">
        <%-- nếu đang có 1 model gửi từ controller của trang nào thì cho link đến trang đó màu nền xanh chữ xanh da trời--%>
        <li class="nav-item ${cateStats == null ? "" : 'bg-success font-weight-bold'}" style="padding: 0 0 0 15px;">
            <a class="nav-link ${cateStats == null ? "" : 'text-info'}"
               href="<c:url value="/admin/category-stats"/>">Thống kê sản phẩm theo danh mục</a>
        </li>
        <li class="nav-item ${productStats == null ? "" : 'bg-success font-weight-bold'}" style="padding: 0 0 0 15px">
            <a class="nav-link ${productStats == null ? "" : 'text-info'}"
               href="<c:url value="/admin/product-stats"/> ">Thống kê doanh thu theo từng sản phẩm</a>
        </li>
        <li class="nav-item ${productMonthStats == null ? "" : 'bg-success font-weight-bold'}"
            style="padding: 0 0 0 15px">
            <a class="nav-link ${productMonthStats == null ? "" : 'text-info'}"
               href="<c:url value="/admin/product-month-stats"/> ">Thống kê doanh thu theo thời gian(tháng)</a>
        </li>
        <li class="nav-item ${productDayStats == null ? "" : 'bg-success font-weight-bold'}"
            style="padding: 0 0 0 15px">
            <a class="nav-link ${productDayStats == null ? "" : 'text-info'}"
               href="<c:url value="/admin/product-day-stats"/> ">Thống kê doanh thu theo thời gian(ngày)</a>
        </li>
        <li class="nav-item" style="padding: 0 0 0 15px">
            <a class="nav-link text-danger" href="<c:url value="/"/> ">Quay lại trang chủ</a>
        </li>
    </ul>

</nav>
