<%--
  Created by IntelliJ IDEA.
  User   : Admin
  Date   : 08/12/2022
  Time   : 6:01 SA
  Author : LeNha
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1 class="text-center table-success">THÊM SẢN PHẨM</h1>
<c:if test="${errMsg != null}">
    <div class="alert alert-danger">${errMsg}</div>
</c:if>

<%-- lấy dữ liệu dạng file từ người dùng gửi cho server gán cho  modelAttribute product --%>
<form:form method="post" modelAttribute="product" action="" enctype="multipart/form-data">
    <div class="form-text">
        <label for="name">Tên</label>
        <form:input type="text" path="name" id="name" cssClass="form-control" autofocus="autofocus" required="required"/>
            <%-- hiển thị lỗi cho riêng in put có path="productName" --%>
        <form:errors path="name" cssClass="alert alert-danger" element="div"/>
    </div>
    <br>
    <div class="form-text">
        <label for="des">Mô tả</label>
        <form:input type="text" path="description" id="des" cssClass="form-control" required="required"/>
        <form:errors path="description" cssClass="alert alert-danger" element="div"/>
    </div>
    <br>
    <div class="form-text">
        <label for="price">Giá</label>
        <form:input type="text" path="price" id="price" cssClass="form-control" required="required"/>
        <form:errors path="price" cssClass="alert alert-danger" element="div"/>
    </div>
    <br>
    <div class="form-text">
        <label for="type">Chủng loại</label>
        <form:select path="category" class="form-control" type="text" id="type" required="required">
            <c:forEach var="cate" items="${categories}">
                <%-- nếu đã gửi form 1 lần tức param của select này ko null (param.category != null)
                    và giá trị param = giá trị option này tức là đã gửi lựa chọn/option này đi thì
                    cho mục chọn sản phẩm/select này mặc định lựa chọn /option này --%>
                <option value="${cate.id}" ${param.category != null && cate.name.equals(param.category) ? "selected='selected'" : ""}>
                        ${cate.name}
                </option>
            </c:forEach>
        </form:select>
        <form:errors path="category" cssClass="alert alert-danger" element="div"/>
    </div>
    <br>
    <div class="form-text">
        <label for="file">Ảnh sản phẩm</label>
        <form:input type="file" path="file" id="file" cssClass="form-control" required="required"/>
        <form:errors path="file" cssClass="alert alert-danger" element="div"/>
    </div>
    <br>
    <div class="form-text">
        <input type="submit" value="Thêm sản phẩm" class="btn btn-danger">
    </div>
</form:form>
