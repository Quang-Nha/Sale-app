<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User   : Admin
  Date   : 04/12/2022
  Time   : 2:26 SA
  Author : LeNha
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h1 class="text-center text-danger">THỐNG KÊ SẢN PHẨM THEO DANH MỤC</h1>

<%-- phần vẽ biểu đồ --%>
<div style="height: 500px; margin: 5% 25% 0 25%" class="align-content-center">
    <canvas id="myCateStatsChart"></canvas>
</div>

<table class="table table-hover">
    <tr>
        <th>Mã danh mục</th>
        <th>Tên danh mục</th>
        <th>Số lượng sản phẩm</th>
    </tr>
    <%-- lặp qua danh sách lấy ra các mảng Object[] nó chứa,
         tại mỗi mảng lấy id, tên danh mục và số lượng sản phẩm trong danh muc đó để hiển thị --%>
    <c:forEach var="cateStat" items="${cateStats}">
        <tr>
            <td>${cateStat[0]}</td>
            <td>${cateStat[1]}</td>
            <td>${cateStat[2]}</td>
        </tr>
    </c:forEach>

</table>

<script>
    // tạo 2 biến mảng chứa danh sách danh mục và danh sách số lượng sản phẩm trong danh mục
    let cateLabels = [], cateInfo = [];

    // lặp qua model chứa list dữ liệu đã truy vấn
    // thêm các dữ liệu vào 2 mảng tương ứng
    <c:forEach var="cateStat" items="${cateStats}">
        cateLabels.push('${cateStat[1]}');
        cateInfo.push(${cateStat[2]});
    </c:forEach>

    // khi load xong trang mới chạy vẽ biểu đồ
    window.onload = function () {
        // gọi hàm vẽ biểu đồ truyền cho nó id phần tử cần vẽ và 2 mảng chứa dữ liệu cho biểu đồ
        cateChart("myCateStatsChart", cateLabels, cateInfo);
    };
</script>
