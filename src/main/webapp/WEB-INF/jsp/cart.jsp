<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User   : Admin
  Date   : 02/12/2022
  Time   : 5:48 SA
  Author : LeNha
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<br>
<h1 class="text-center text-danger">GIỎ HÀNG</h1>

<%-- xem model cartStatus có thông tin hoàn thành thanh toán không thì hiển thị --%>
<c:if test="${cartStatus == 'paySuccess'}">
    <h4 class="text-danger" id="cartStatus">Thanh toán thành công</h4>
</c:if>

<%-- nếu model cartStatus không có thông tin hoàn thành thanh toán
    và chưa có thông tin giỏ hàng thì thông báo không có sản phẩm nào trong giỏ --%>
<c:if test="${cartStatus != 'paySuccess' && carts == null}">
    <h4 class="text-danger" id="cartStatus">Không có sản phẩm nào trong giỏ</h4>
</c:if>

<%-- nếu có thông tin giỏ hàng thì hiển thị danh sách sản phẩm trong giỏ --%>
<c:if test="${carts != null}">
    <table class="table">
        <tr>
            <th>Mã sản phẩm</th>
            <th>Tên sản phẩm</th>
            <th>Đơn giá</th>
            <th>Số lượng</th>
            <th></th>
        </tr>
        <c:forEach var="c" items="${carts}">
            <%-- gán id cho hang là produc + id sản phẩm để có thể lấy id và xóa khi gọi hàm xóa --%>
            <tr id="product${c.productId}">
                <td>${c.productId}</td>
                <td>${c.productName}</td>
                <td>${c.price}.000.000 </td>
                <td>
                    <div class="form-group">
                        <input type="number"
                               <%-- mỗi khi sửa số lượng và nhấn chuột khỏi vị trí này
                                    thì hàm update số lượng được gọi, truyền cho hàm giá trị
                                     của chính phần tử này và id của sản phẩm đang duyệt --%>
                               onblur="updateCart(this, ${c.productId})"
                               value="${c.quantity}" class="form-control">
                    </div>
                </td>
                <td>
                    <input type="button"
                           <%-- gọi hàm xóa sản phẩm, truyền id sp cần xóa --%>
                           onclick="deleteCart(${c.productId})"
                           value="Xóa" class="btn btn-danger">
                </td>
            </tr>
        </c:forEach>
    </table>
    <div class="text-info text-center">
        <h4>
            Tổng tiền hóa đơn: <span id="amountCart">${cartStats.amount}.000.000 VND</span>
        </h4>
    </div>

    <%-- nếu chưa đăng nhập thì không hiện nút thanh toán mà hiện nút chuyển đến trang đăng nhập--%>
    <c:if test="${pageContext.request.userPrincipal.name != null && carts.size() > 0}">
        <input type="button" value="Thanh toán" class="btn btn-danger font-weight-bold float-right"
               onclick="pay()">
    </c:if>
    <c:if test="${pageContext.request.userPrincipal.name == null && carts.size() > 0}">
        <h4 class="text-danger text-right">Đăng nhập để thanh toán</h4>
        <a href="${pageContext.request.contextPath}/login" class="btn btn-success text-white font-weight-bold float-right">
            Đăng nhập
        </a>
    </c:if>

    <br>
    <br>
</c:if>
