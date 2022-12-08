<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
    Author  : LeNha
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<%-- danh sách sản phẩm --%>
<div class="row">
    <div class="col-md-9 col-xs-12">
        <div class="row">
            <c:forEach var="product" items="${products}">
                <div class="col-md-4 cart-padding">
                    <div class="card">
                        <a href="<c:url value="/product/${product.id}"/> ">
                            <c:choose>
                                <c:when test="${product.image != null && product.image.startsWith('http')}">
                                    <img class="card-img-top img-fluid" src="${product.image}" alt="Card image"/>
                                </c:when>
                                <c:otherwise>
                                    <img class="card-img-top img-fluid"
                                         src="<c:url value="/resources/images/avatar.jpg"/>"
                                         alt="Card image"/>
                                </c:otherwise>
                            </c:choose>
                        </a>
                        <div class="card-body">
                            <h5 class="card-title">${product.name}</h5>
                            <p class="card-text">${product.price}.000.000 VND</p>
                            <a href="#" class="fw-bold btn btn-primary"
                                <%-- gọi hàm thêm sản phẩm này vào giỏ hàng --%>
                               onclick="addToCart(${product.id}, '${product.name}', ${product.price})">
                                Đặt hàng
                            </a>
                        </div>
                    </div>
                </div>

            </c:forEach>
        </div>

        <%-- phân trang--%>
        <div style="text-align: center">
            <ul class="pagination" style="display:flex;justify-content: center; align-items: center">
                <%-- nếu có param pageNum(null thì ko tính) và pageNum > 1 thì hiển thị nút back quay lại trang nhỏ hơn 1 --%>
                <c:if test="${param.pageNum > 1}">
                    <li class="page-item">
                        <a class="page-link"
                            <%-- quay lại trang này nhưng trang sản phẩm - 1: pageNum - 1
                                 thêm param pageNum vào link để controller lấy được giá trị trang cần đến
                                 cả link là /?pageNum=pageNum - 1
                                 thêm param cateId để xác định chủng loại nếu đang xem theo chủng loại
                                 thêm param search giá trị là giá trị đang tìm kiếm hiện tại param.search --%>
                           href="<c:url value="/"/>?cateId=${param.cateId}&search=${param.search}&pageNum=${param.pageNum - 1}">
                            &lt;
                        </a>
                    </li>
                </c:if>

                <%-- xác định số trang theo số lượng sản phẩm theo từ tìm kiếm
                    mỗi trang có 9 sản phẩm thì số trang bằng số lượng sản phẩm / 9 và làm tròn lên
                    để nếu có phần dư cho vào 1 trang nữa --%>
                <c:set var="numPage" value="${Math.ceil(numProduct / 9)}"/>
                <%-- hiển thị danh sách các trang theo số lượng sản phẩn đang có trong csdl
                    tính theo mỗi trang 6 sản phẩm thì lấy số sản phẩm / 6 và làm tròn lên là ra số trang cần hiển thị
                    tạo vòng lặp với số trang tính được và in ra các link a trang hiển thị giá trị link a --%>
                <c:forEach begin="1" end="${numPage}" var="num">
                    <li class="page-item">
                            <%--  gắn vào link gửi đến trang chủ với param pageNum là giá trị đang lặp chính là trang
                                cần chuyển đến
                                thêm param cateId để xác định chủng loại nếu đang xem theo chủng loại
                                thêm param search giá trị là giá trị đang tìm kiếm hiện tại param.search--%>
                        <a class="page-link"
                           href="<c:url value="/"/>?cateId=${param.cateId}&search=${param.search}&pageNum=${num}"

                            <%-- sử dụng toán tử tam phân
                                nếu link có giá trị số trang = trang đang ở thì cho màu nền xanh và chữ trắng, so sánh là
                                nếu trang hiện tại param pageNum = biến đang lặp tức là link này chính là trang đang hiển thị
                                hoặc nếu không có param pageNum thì tức là controller mặc định cho hiển thị trang 1 nên
                                nếu link này có giá trị số trang(lần lặp thứ 1) thì cũng cho cho màu nền xanh và chữ trắng--%>
                            ${(num == param.pageNum || (param.pageNum == null && num == 1)) ? "style='background-color:green;color:white'" : ""}>
                                ${num}
                        </a>
                    </li>
                </c:forEach>

                <%-- nếu trang hiện tại nhỏ hơn trang lớn nhất thì hiển thị nút next tiến đến trang lớn hơn 1
                    nếu param pageNum == null thì controller cho mặc định là đến trang sản phẩm 1
                    thì vần cần hiện nút next nên thêm điều kiện || pageNum == null
                    và phải có điều kiện số trang cũng cần lớn hơn 1 --%>
                <c:if test="${(param.pageNum < numPage || param.pageNum == null) && numPage > 1}">
                    <li class="page-item">
                        <a class="page-link"
                            <%-- nếu param pageNum null thì tức là đang ở trang 1 cho hiển thị trang tiếp theo là 2
                                 không null thì cho trang tiếp theo là pageNum + 1
                                 thêm param pageNum vào link để controller lấy được giá trị trang cần đến
                                 cả link là /?pageNum=pageNum + 1
                                 thêm param cateId để xác định chủng loại nếu đang xem theo chủng loại
                                 thêm param search giá trị là giá trị đang tìm kiếm hiện tại param.search--%>
                           href="<c:url value="/"/>?search=${param.search}&cateId=${param.cateId}&pageNum=${param.pageNum == null ? 2 : param.pageNum + 1}">
                            &gt;
                        </a>
                    </li>
                </c:if>
            </ul>
        </div>
    </div>

    <div class="col-md-3 col-xs-12">
        <%-- hiển thị các sản phẩm được mua nhiều nhất --%>
        <div class="row">
            <c:forEach var="product" items="${hotProducts}">
                <div class="col-md-12 cart-padding" style="padding: 10px">
                    <div class="alert alert-success"><h4>Sản phẩm bán chạy</h4></div>
                    <div class="card bg-warning">
                        <a href="<c:url value="/product/${product[0]}"/> ">
                            <c:choose>
                                <c:when test="${product[3] != null && product[3].startsWith('http')}">
                                    <img class="card-img-top img-fluid" src="${product[3]}" alt="Card image"/>
                                </c:when>
                                <c:otherwise>
                                    <img class="card-img-top img-fluid"
                                         src="<c:url value="/resources/images/avatar.jpg"/>"
                                         alt="Card image"/>
                                </c:otherwise>
                            </c:choose>
                        </a>
                        <div class="card-body">
                            <h4 class="card-title">${product[1]}</h4>
                            <p class="card-text">${product[2]}.000.000 VND</p>
                            <p class="text-bg-success text-center font-weight-bold">Số lượt bán: ${product[4]}</p>
                            <a href="#" class="btn btn-danger font-weight-bold"
                                <%-- gọi hàm thêm sản phẩm này vào giỏ hàng --%>
                               onclick="addToCart(${product[0]}, '${product[1]}', ${product[2]})">
                                Đặt hàng
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <hr/>
        <%-- hiển thị các sản phẩm được thảo luận nhiều nhất --%>
        <div class="row">

            <c:forEach var="product" items="${disProducts}">
                <div class="col-md-12 cart-padding" style="padding: 10px">
                    <div class="alert alert-success"><h4>Sản phẩm được thảo luận nhiều nhất</h4></div>
                    <div class="card bg-info">
                        <a href="<c:url value="/product/${product[0]}"/> ">
                            <c:choose>
                                <c:when test="${product[3] != null && product[3].startsWith('http')}">
                                    <img class="card-img-top img-fluid" src="${product[3]}" alt="Card image"/>
                                </c:when>
                                <c:otherwise>
                                    <img class="card-img-top img-fluid"
                                         src="<c:url value="/resources/images/avatar.jpg"/>"
                                         alt="Card image"/>
                                </c:otherwise>
                            </c:choose>
                        </a>
                        <div class="card-body">
                            <h4 class="card-title">${product[1]}</h4>
                            <p class="card-text">${product[2]}.000.000 VND</p>
                            <p class="text-bg-success text-center fw-bold">Số lượt thảo luận: ${product[4]}</p>
                            <a href="#" class="btn btn-warning font-weight-bold"
                                <%-- gọi hàm thêm sản phẩm này vào giỏ hàng --%>
                               onclick="addToCart(${product[0]}, '${product[1]}', ${product[2]})">
                                Đặt hàng
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

</div>
