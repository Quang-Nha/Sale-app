<%@ page import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User   : Admin
  Date   : 01/12/2022
  Time   : 4:35 SA
  Author : LeNha
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h1 class="text-center text-danger">CHI TIẾT SẢN PHẨM</h1>
<br>
<br>
<div class="row" style="padding: 0 15% 0 15%;">
    <div class="col-md-6">
        <c:choose>
            <c:when test="${product.image != null && product.image.startsWith('http')}">
                <img class="card-img-top" style="height: 350px;object-fit: contain" src="${product.image}" alt="${product.name}"/>
            </c:when>
            <c:otherwise>
                <img class="card-img-top img-fluid" src="<c:url value="/resources/images/avatar.jpg"/>"
                     alt="${product.name}"/>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="col-md-6">
        <h1>${product.name}</h1>
        <h2 class="text-danger">${product.price}.000.000 VND</h2>
        <p>${product.description}</p>
        <div>
            <input type="button" value="Đặt hàng" class="btn bg-danger text-white font-weight-bold"
                   <%-- gọi hàm thêm sản phẩm này vào giỏ hàng --%>
                   onclick="addToCart(${product.id}, '${product.name}', ${product.price})"/>
        </div>
    </div>
</div>
<br>
<br>

<h4 class="text-info" style="padding: 0 15% 0 15%;">Đánh giá sản phẩm</h4><br>

<%-- phần nhận xét sản phẩm
    nếu đã đăng nhập có tên tài khoản mới được nhận xét sản phẩm --%>
<c:if test="${pageContext.request.userPrincipal.name != null}">
    <form style="padding: 0 15% 0 15%;">
        <div class="form-group">
            <textarea class="form-control" id="commentId" placeholder="Nhập đánh giá của bạn..."></textarea>
            <br>
                <%-- gọi hàm addComment của javascript truyền cho nó id sản phẩm để thêm bình luận dạng ajax --%>
            <input type="button" value="Gửi bình luận" class="btn bg-danger fw-bold text-white float-right" onclick="addComment(${product.id})">
        </div>
    </form>
    <br>
</c:if>

<%-- phần danh sách nhận xét --%>
<div id="commentArea" style="padding: 0 15% 0 15%;">
    <c:forEach var="comment" items="${comments}">
        <div class="row">
            <div class="col-md-1">
                <%-- hiển thị ảnh của user đã comment --%>
                <img class="rounded-circle img-fluid" width="80" height="80" src="${comment.user.avatar}" alt=""/>
            </div>
            <div class="col-md-11 my-date">
                <p>${comment.content}</p>
                <i>${comment.createdDate}</i>
            </div>
        </div>
        <br>
    </c:forEach>
    <br>
</div>

<script>
    // chỉ chạy hàm khi đã load hết các thành phần
    window.onload = function () {
        // lấy danh sách các phần tử i có trong class my-date
        let dates = document.querySelectorAll(".my-date > i")
        // lặp qua danh sách
        for (let i = 0; i < dates.length; i++) {
            let d = dates[i]
            // hiển thị lại d giá trị phần tử thành thời gian đã đăng cách đây bao lâu bằng hàm fromNow()
            // lấy thời gian hiện tại - thời gian của d rồi cho d hiển thị lại hiệu số này
            d.innerText = moment(d.innerText).fromNow()
        }
    }
</script>