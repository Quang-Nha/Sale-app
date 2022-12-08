<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User   : Admin
  Date   : 05/12/2022
  Time   : 1:04 SA
  Author : LeNha
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<h1 class="text-center text-danger">THỐNG KÊ DOANH THU THEO THỜI GIAN(THÁNG)</h1>
<br>

<%-- phần lọc dữ liệu cho truy vấn, chỉ lấy trong các khoảng dữ liệu này
    gồm các sản phẩm có tên phải giống từ khóa vì nếu lọc theo tên product có
    chứa từ tìm kiếm thì sẽ không biết đang lọc theo những sản phẩm nào
    ngày bán trong khoảng ngày bắt đầu và ngày kết thúc --%>
<form action="" method="get">
    <div class="form-group">
        <%-- tạo mục chọn sản phẩm trong 1 danh sách các sản phẩm để
            chọn sản phẩm nào sẽ thống kê doanh thu theo thời gian
            có 1 mục đầu là lấy tất cả sản phẩm --%>
        <label class="w-100">Sản phẩm cần thống kê theo thời gian(tháng)
            <select name="kw" class="form-control">
                <%-- nếu đã gửi form 1 lần tức param của select này ko null (param.kw != null)
                    và giá trị param là rỗng tức là đã gửi lựa chọn/option rỗng này đi thì
                    cho mục chọn sản phẩm/select này mặc định lựa chọn /option rỗng này --%>
                <option value="" ${param.kw != null && "".equals(param.kw) ? "selected" : ""}>
                    All
                </option>
                <c:forEach var="cate" items="${productsName}">
                    <%-- nếu đã gửi form 1 lần tức param của select này ko null (param.kw != null)
                        và giá trị param = giá trị option này tức là đã gửi lựa chọn/option này đi thì
                        cho mục chọn sản phẩm/select này mặc định lựa chọn /option này --%>
                    <option value="${cate}" ${param.kw != null && cate.equals(param.kw) ? "selected" : ""}>
                            ${cate}
                    </option>
                </c:forEach>
            </select>
        </label>
    </div>
    <div class="form-group">
        <label class="w-100">Từ thời điểm
            <%-- lấy chính param nó gửi đi để set giá trị cho chính nó--%>
            <input type="date" name="fromDate" onkeydown="return false" class="form-control" value="${param.fromDate}">
        </label>
    </div>
    <div class="form-group">
        <label class="w-100">Đến thời điểm
            <input type="date" name="toDate" onkeydown="return false" class="form-control" value="${param.toDate}">
        </label>
    </div>
    <input type="submit" value="Báo cáo" class="btn btn-success">
</form>

<%-- phần vẽ biểu đồ --%>
<div>
    <canvas id="myProductMonthStatsChart"></canvas>
</div>

<table class="table table-hover">
    <tr>
        <th>Tháng</th>
        <th>Doanh thu</th>
    </tr>
    <%-- lặp qua danh sách lấy ra các mảng Object[] nó chứa,
         tại mỗi mảng lấy tháng, năm cho vào cột 1 và tổng doanh thu của tháng, năm đó cho vào cột 2 để hiển thị --%>
    <c:forEach var="pms" items="${productMonthStats}">
        <tr>
            <td>${pms[0]}/${pms[1]}</td>
                <%-- fomat dạng kiểu số có dấu . đánh dấu phần nghìn và bỏ phần thập phân --%>
            <td><fmt:formatNumber type="number" maxFractionDigits="0" value="${pms[2]}"/>.000.000 VND</td>
        </tr>
    </c:forEach>
</table>

<script>
    // tạo 2 biến mảng chứa danh sách 1 là tháng, năm và 2 là danh sách doanh thu của tháng, năm tương ứng
    let dateLabels = [], amountDateInfo = [];

    // lặp qua model chứa list dữ liệu đã truy vấn
    // thêm các dữ liệu vào 2 mảng tương ứng
    <c:forEach var="pms" items="${productMonthStats}">
    dateLabels.push('${pms[0]}/${pms[1]}');
    amountDateInfo.push(${pms[2]});
    </c:forEach>

    // khi load xong trang mới chạy vẽ biểu đồ
    window.onload = function () {
        // gọi hàm vẽ biểu đồ truyền cho nó id phần tử cần vẽ và 2 mảng chứa dữ liệu cho biểu đồ
        productDayMonthChart("myProductMonthStatsChart", dateLabels, amountDateInfo);
    };
</script>

