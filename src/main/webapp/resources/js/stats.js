
/**
 * tạo ra một màu ngẫu nhiên
 * @returns {string} chứa 3 giá trị của 1 màu được tạo ngẫu nhiên
 */
function generateColor() {
    let r = parseInt(Math.random() * 255);
    let g = parseInt(Math.random() * 255);
    let b = parseInt(Math.random() * 255);
    return `rgb(${r}, ${g}, ${b})`
}

/**
 * khai báo biểu đồ
 * @param id của phần tử html cần vẽ trên nó
 * @param cateLabels mảng các tên danh mục
 * @param cateInfo mảng chứa số lượng các sản phẩm theo danh mục tương ứng với mảng danh mục
 */
function cateChart(id, cateLabels=[], cateInfo=[]) {
    // tạo mảng các màu
    let colors = [];

    // tạo số lượng màu bằng size của mảng danh mục
    // mỗi màu lấy từ hàm tạo màu ngẫu nhiên thêm vào mảng trên
    for (let i = 0; i < cateLabels.length; i++) {
        colors.push(generateColor());
    }

    const data = {
        // khai báo danh sách danh mục trong biểu đồ
        labels: cateLabels,
        datasets: [{
            label: ' So san pham',
            // khai báo danh sách các giá trị cần hiển thị là danh sách số lượng sản phẩm trong 1 danh mục
            data: cateInfo,
            // khai báo danh sách các màu lấy từ mảng đã tạo
            backgroundColor: colors,
            hoverOffset: 4
        }]
    };

    // cấu hình biểu đồ
    const config = {
        // loại biểu đồ
        type: 'doughnut',
        // truyền dữ liệu đã khai báo ở trên vào
        data: data,
    };

    // lấy id của phần tử html cần vẽ lên
    const ctx = document.getElementById(id);

    // vẽ biểu đồ lên phần tử bằng biểu đồ khai báo
    new Chart(ctx, config);
}

/**
 * khai báo biểu đồ
 * @param id của phần tử html cần vẽ trên nó
 * @param productLabels mảng các tên sản phẩm
 * @param productInfo mảng chứa doanh thu các sản phẩm tương ứng
 */
function productChart(id, productLabels = [], productInfo = []) {
    // tạo mảng các màu
    let colors = [];

    // tạo số lượng màu bằng size của mảng tên sản phẩm
    // mỗi màu lấy từ hàm tạo màu ngẫu nhiên thêm vào mảng trên
    for (let i = 0; i < productLabels.length; i++) {
        colors.push(generateColor());
    }

    const data = {
        // khai báo danh sách tên sản phẩm trong biểu đồ
        labels: productLabels,
        datasets: [{
            label: 'Thống kê doanh thu theo sản phẩm',
            // khai báo danh sách các giá trị cần hiển thị là danh sách doanh thu các sản phẩm
            data: productInfo,
            // khai báo danh sách các màu lấy từ mảng đã tạo
            backgroundColor: colors,
            borderColor: colors,
            hoverOffset: 4
        }]
    };

    // cấu hình biểu đồ
    const config = {
        // loại biểu đồ
        type: 'line',
        // type: 'bar',

        // truyền dữ liệu đã khai báo ở trên vào
        data: data,
    };

    // lấy id của phần tử html cần vẽ lên
    const ctx = document.getElementById(id);

    // vẽ biểu đồ lên phần tử bằng biểu đồ khai báo
    new Chart(ctx, config);
}

/**
 * khai báo biểu đồ
 * @param id  của phần tử html cần vẽ trên nó
 * @param dateLabels mảng các mốc thời gian dạng tháng/năm
 * @param amountDateInfo mảng chứa doanh thu các sản phẩm theo thời gian tương ứng
 */
function productDayMonthChart(id, dateLabels = [], amountDateInfo = []) {
    // tạo mảng các màu
    let colors = [];

    // tạo số lượng màu bằng size của mảng tên sản phẩm
    // mỗi màu lấy từ hàm tạo màu ngẫu nhiên thêm vào mảng trên
    for (let i = 0; i < dateLabels.length; i++) {
        colors.push(generateColor());
    }

    const data = {
        // khai báo danh sách mốc thời gian trong biểu đồ
        labels: dateLabels,
        datasets: [{
            label: 'Thống kê doanh thu theo thời gian',
            // khai báo danh sách các giá trị cần hiển thị là danh sách doanh thu theo các mốc thời gian
            data: amountDateInfo,
            // khai báo danh sách các màu lấy từ mảng đã tạo
            backgroundColor: colors,
            borderColor: colors,
            hoverOffset: 4
        }]
    };

    // cấu hình biểu đồ
    const config = {
        // loại biểu đồ
        // type: 'line',
        type: 'bar',

        // truyền dữ liệu đã khai báo ở trên vào
        data: data,
    };

    // lấy id của phần tử html cần vẽ lên
    const ctx = document.getElementById(id);

    // vẽ biểu đồ lên phần tử bằng biểu đồ khai báo
    new Chart(ctx, config);
}
