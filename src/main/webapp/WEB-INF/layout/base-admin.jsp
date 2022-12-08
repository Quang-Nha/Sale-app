<%--
  Created by IntelliJ IDEA.
  User   : Admin
  Date   : 04/12/2022
  Time   : 1:41 SA
  Author : LeNha
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title><tiles:insertAttribute name="title"/></title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta charset="UTF-8">

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet">
        <link rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.1/dist/jquery.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.4/moment.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="<c:url value="/resources/js/stats.js"/>"></script>
    </head>
    <body class="d-flex flex-column h-100" style="margin: 0 10% 0 10%;">
        <br>
        <h1 class="text-center text-info">TRANG THỐNG KÊ - BÁO CÁO</h1>
        <br>

        <div class="container">
            <div class="row">
                <div class="col-md-4 col-xs-12 bg-light">
                    <%-- LEFT --%>
                    <tiles:insertAttribute name="left"/>
                </div>
                <div class="col-md-8 col-xs-12">
                    <%-- CONTENT --%>
                    <tiles:insertAttribute name="content"/>
                </div>
            </div>
        </div>

        <div class="footer mt-auto container">
            <div class="row">
            <%-- FOOTER --%>
            <tiles:insertAttribute name="footer"/>
            </div>
        </div>

    </body>
</html>