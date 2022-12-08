/**
 * gửi nội dung nhận xét sản phẩm trong body cho rest api để api thêm nhận xét vào csdl
 * và trả về json kết quả nhận xét
 * add nhận xét vào phần bình luận của trang sản phẩm
 * @param productID id của sản phẩm nhận xét
 */
function addComment(productID) {
    // gửi link cho server api dạng post với link /Shopping/api/add-comment
    // cấu hình dạng post bằng method: 'post'
    // thêm body 2 tham số chứa nội dung nhận xét và id của sản phẩm gửi cho server
    // cấu hình thêm nội dung gửi là dạng json
    fetch("/Shopping/api/add-comment", {
        method: 'post',
        body: JSON.stringify({
            "content": document.getElementById("commentId").value,
            "productID": productID
        }),
        headers: {
            "Content-Type": "application/json"
        }
    }).then(function (res) {// nhận dữ liệu trả về vào tham số res
        console.info(res)

        return res.json()// chuyển dữ liệu nhận được là 1 đối tượng Comment sang json và trả về
    }).then(function (data) {// nhận dữ liệu trả về của hàm then trên vào tham số data
        console.info(data)

        // lấy phần tử chứa bình luận
        let area = document.getElementById("commentArea")

        // set lại giá trị html của phần tử trên bằng phần bình luận mới theo json nhận được từ server
        // cộng với giá trị gốc của nó(các bình luận ban đầu)
        area.innerHTML = `
          <div class="row">
            <div class="col-md-1">
            <!-- set avatar theo thuộc tính avatar của user, user là thuộc tính liên kết khóa nằm trong Comment -->
                <img class="rounded-circle img-fluid" width="80" height="80" src="${data.user.avatar}" alt=""/>
            </div>
            <div class="col-md-11 my-date">
                <p>${data.content}</p>
                <i>${moment(data.createdDate).fromNow()}</i>
            </div>
          </div>
          <br>
        ` + area.innerHTML
    });
}

/**
 * gọi api thêm sản phẩm vào giỏ hàng,
 * gửi cho api đối tượng giỏ hàng trong body của hàm post
 * nhận giá trị api trả về là số lượng sản phẩm đang có trong giỏ và
 * set cho phần tử html hiển thị giá trị này
 */
function addToCart(id, name, price) {
    // cấu hình xóa cách hoạt động cũ của phần tử html gọi hàm này
    // thay bằng hành động mới mà hàm này sẽ làm
    event.preventDefault()
    // gọi api bằng link gửi cho nó json giỏ hàng trong body của hàm post
    fetch("/Shopping/api/cart", {
        method: "post",
        body: JSON.stringify({
                "productId": id,
                "productName": name,
                "price": price,
                "quantity": 1
            }
        ),
        headers: {
            "Content-Type": "application/json"
        }
    }).then(function (res) {// nhận dữ liệu trả về vào tham số res
        return res.json()// chuyển dữ liệu nhận được sang json và trả về
    }).then(function (data) {// nhận dữ liệu trả về của hàm then trên vào tham số data
        // lấy phần tử hiển thị số lượng sản phẩm trong giỏ
        let counter = document.getElementById("cartCounter")

        // set lại giá trị html của phần tử trên bằng data mà api trả về
        counter.innerText = data
    })
}

/**
 * gọi api sửa lại số lượng sản phẩm trong giỏ chứa nó theo id sản phẩm
 * gửi cho api id sản phẩm và số lượng cần update
 * nhận giá trị api trả về là map chứa 2 cặp giá trị là tổng tiền và tổng sản phẩm có trong các giỏ
 * set cho phần tử html hiển thị số lượng sản phẩm trong giỏ bằng phần từ có key counter trong map trên
 * set cho phần tử html hiển thị tổng tiền trong giỏ bằng phần từ có key amount trong map trên
 * @param obj chứa số lượng cần update
 * @param productId id của sản phẩm
 */
function updateCart(obj, productId) {
    fetch("/Shopping/api/cart", {
        method: "put",
        body: JSON.stringify({
                "productId": productId,
                "productName": "",
                "price": 0,
                "quantity": obj.value// lấy giá trị của nó là số lượng cần sửa
            }
        ),
        headers: {
            "Content-Type": "application/json"
        }
    }).then(function (res) {
        return res.json()
    }).then(function (data) {
        // lấy phần tử hiển thị số lượng sản phẩm trong giỏ
        let counter = document.getElementById("cartCounter")
        // set lại giá trị html của phần tử trên bằng phần tử có key counter trong map data mà api trả về
        counter.innerText = data.counter
        // location.reload()//load lại trang

        // lấy phần tử hiển thị tổng tiền trong giỏ
        let amount = document.getElementById("amountCart")
        // set lại giá trị html của phần tử trên bằng phần tử có key amount trong map data mà api trả về
        amount.innerText = data.amount + ".000.000 VND"
    });
}

/**
 * gọi api xóa giỏ chứa sản phẩm có id truyền vào
 * gửi cho api id của sản phẩm
 * nhận giá trị api trả về là map chứa 2 cặp giá trị là tổng tiền và tổng sản phẩm có trong các giỏ
 * set cho phần tử html hiển thị số lượng sản phẩm trong giỏ bằng phần từ có key counter trong map trên
 * set cho phần tử html hiển thị tổng tiền trong giỏ bằng phần từ có key amount trong map trên
 * ẩn hàng chứa sản phẩm đã xóa khỏi giỏ
 * @param productId id của sản phẩm
 */
function deleteCart(productId) {
    // hỏi người dùng xem có chắc chắn xóa không
    if (confirm("Bạn chắc chắn xóa sản phẩm này không?")) {

        fetch(`/Shopping/api/cart/${productId}`, {
            method: "delete",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(function (res) {
            return res.json()
        }).then(function (data) {
            // lấy phần tử hiển thị số lượng sản phẩm trong giỏ
            let counter = document.getElementById("cartCounter")
            // set lại giá trị html của phần tử trên bằng phần tử có key counter trong map data mà api trả về
            counter.innerText = data.counter

            // nếu số lượng sản phẩm còn lại = 0 thì load lại trang
            if (data.counter.toString() === "0") {
                location.reload()//load lại trang
            }

            // location.reload()//load lại trang

            // lấy phần tử hiển thị tổng tiền trong giỏ
            let amount = document.getElementById("amountCart");
            // set lại giá trị html của phần tử trên bằng phần tử có key amount trong map data mà api trả về
            amount.innerText = data.amount + ".000.000 VND"

            // location.reload()//load lại trang

            // lấy phần tử html chứa dòng hiển thị sản phẩm cần xóa bằng id sản phẩm
            let row = document.getElementById(`product${productId}`)

            // set cho phần tử ẩn đi
            row.style.display = "none";
        });
    }
}

/**
 * gọi api thanh toán
 * nhận dữ liệu trả về và reload lại trang để xem lại lại giỏ hàng đã bị xóa khỏi session
 */
function pay() {
    if (confirm("Bạn chắc chắn thanh toán?")) {
        fetch("/Shopping/api/cart/pay", {
            method: "post"
        }).then(function (res) {
            return res.json()
        }).then(function (code) {
            console.info(code);
            location.reload();
        });
    }
}