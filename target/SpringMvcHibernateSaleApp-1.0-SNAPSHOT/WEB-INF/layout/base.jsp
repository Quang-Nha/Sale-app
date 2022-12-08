<%--
  Created by IntelliJ IDEA.
  User   : Admin
  Date   : 26/11/2022
  Time   : 4:26 SA
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

    </head>
    <body class="d-flex flex-column h-100">
        <%-- HEADER --%>
        <tiles:insertAttribute name="header"/>
        <br>
        <br>
        <br>
        <div class="container" style="padding: 0 5% 0 5%;">
            <%-- CONTENT --%>
            <tiles:insertAttribute name="content"/>
        </div>

        <%-- FOOTER --%>
        <tiles:insertAttribute name="footer"/>

        <script src="<c:url value="/resources/js/main.js"/>"></script>
    </body>
</html>
