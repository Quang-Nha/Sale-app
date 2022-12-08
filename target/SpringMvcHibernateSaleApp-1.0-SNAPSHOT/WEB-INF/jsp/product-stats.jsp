<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User   : Admin
  Date   : 04/12/2022
  Time   : 7:34 SA
  Author : LeNha
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<h1 class="text-center text-danger">THỐNG KÊ DOANH THU THEO SẢN PHẨM</h1>
<br>

<%-- phần lọc dữ liệu cho truy vấn, chỉ lấy trong các khoảng dữ liệu này
    gồm các sản phẩm có tên chứa từ khóa
    ngày bán trong khoảng ngày bắt đầu và ngày kết thúc --%>
<form action="" method="get">
    <div class="form-group">
        <label class="w-100">Từ khóa tìm kiếm
            <%-- lấy chính param nó gửi đi để set giá trị cho chính nó--%>
            <input type="text" name="kw" class="form-control" value="${param.kw}">
        </label>
    </div>
    <div class="form-group">
        <label class="w-100">Từ thời điểm
            <%-- không cho phép chỉnh sửa chỉnh sửa bằng bàn phím --%>
            <input type="date" name="fromDate" onkeydown="return false" class="form-control" value="${param.fromDate}">
        </label>
    </div>
    <div class="form-group">
        <label class="w-100">Đến thời điểm
            <input type="date" name="toDate" onkeydown="return false"  class="form-control" value="${param.toDate}">
        </label>
    </div>
    <input type="submit" value="Báo cáo" class="btn btn-success">
</form>

<%-- phần vẽ biểu đồ --%>
<div>
    <canvas id="myProductStatsChart"></canvas>
</div>

<table class="table table-hover">
    <tr>
        <th>Mã sản phẩm</th>
        <th>Tên sản phẩm</th>
        <th>Doanh thu</th>
    </tr>
    <%-- lặp qua danh sách lấy ra các mảng Object[] nó chứa,
         tại mỗi mảng lấy id, tên sản phẩm và tổng doanh thu của sản phẩm đó để hiển thị --%>
    <c:forEach var="ps" items="${productStats}">
        <tr>
            <td>${ps[0]}</td>
            <td>${ps[1]}</td>
            <%-- fomat dạng kiểu số có dấu . đánh dấu phần nghìn và bỏ phần thập phân --%>
            <td><fmt:formatNumber type="number" maxFractionDigits="0" value="${ps[2]}"/>.000.000 VND</td>
        </tr>
    </c:forEach>
</table>

<script>
    // tạo 2 biến mảng chứa danh sách tên sản phẩm và danh sách doanh thư của sản phẩm tương ứng
    let productLabels = [], productInfo = [];

    // lặp qua model chứa list dữ liệu đã truy vấn
    // thêm các dữ liệu vào 2 mảng tương ứng
    <c:forEach var="ps" items="${productStats}">
    productLabels.push('${ps[1]}');
    productInfo.push(${ps[2]});
    </c:forEach>

    // khi load xong trang mới chạy vẽ biểu đồ
    window.onload = function () {
        // gọi hàm vẽ biểu đồ truyền cho nó id phần tử cần vẽ và 2 mảng chứa dữ liệu cho biểu đồ
        productChart("myProductStatsChart", productLabels, productInfo);
    };
</script>

