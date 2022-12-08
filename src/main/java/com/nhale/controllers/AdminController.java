/*
 *
 */
package com.nhale.controllers;

import com.nhale.service.ProductService;
import com.nhale.service.StatsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author LeNha
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final StatsService statsService;
    private final ProductService productService;

    public AdminController(StatsService service, ProductService productService) {
        this.statsService = service;
        this.productService = productService;
    }

    /**
     * lấy dữ liệu danh sách các mảng Object[],
     * mỗi mảng chứa id, tên các danh mục và số lượng sản phẩm của danh mục đó
     * gửi dữ liệu cho view bằng model
     */
    @GetMapping("/category-stats")
    public String catrStats(Model model) {
        // trả về danh sách các mảng Object[], mỗi mảng chứa
        // id, tên các danh mục và số lượng sản phẩm của danh mục đó
        model.addAttribute("cateStats", this.statsService.cateStats());

        return "category-stats";
    }

    /**
     * lấy hàm thống kê doanh thu các sản phẩm
     * lấy chọn lọc theo tên sản phẩm có chứa từ tìm kiếm truyền vào
     * và khoảng thời gian đặt hàng các sản phẩm đó
     * hàm trả về list các mảng Object, mỗi mảng chứa sản phẩm và doanh thu của nó
     * và gửi kết quả của hàm vào 1 model
     */
    @GetMapping("/product-stats")
    public String productStats(Model model, @RequestParam(required = false) Map<String, String> params) throws ParseException {

        // lấy các tham số để lọc dữ liệu
        String kw = params.get("kw");
        String fDate = params.get("fromDate");
        String tDate = params.get("toDate");

        // tạo 1 fomat để chuyển đổi string sang ngày tháng
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // tạo các biến ngày tháng để fomat từ ngày tháng kiểu String sang nó
        Date fromDate = null;
        Date toDate = null;

        // nếu tham số ngày bắt đầu ko null và ko rỗng
        // thì chuyển biến tham số dạng string sang dạng date
        if (fDate != null && !fDate.trim().isEmpty()) {
            fromDate = sdf.parse(fDate);
        }

        // nếu tham số ngày kết thúc ko null và ko rỗng
        // thì chuyển biến tham số dạng string sang dạng date
        // rồi tăng date lên 1 ngày vì ko rõ lý do gì mà
        // hàm lọc dữ liệu truy vấn csdl coi ngày kết thúc là ngày trước đó
        // chứ ko phải ngày hiện tại nên phải tăng lên 1 ngày
        if (tDate != null && !tDate.trim().isEmpty()) {
            toDate = sdf.parse(tDate);
            Calendar c = Calendar.getInstance();
            c.setTime(toDate);
            c.add(Calendar.DATE, 1);
            toDate = c.getTime();
        }

        // cho các tham số lọc vào hàm lấy dữ liệu và trả về list chứa dữ liệu
        // cho dữ liệu gửi vào 1 model
        model.addAttribute("productStats", this.statsService.productStats(kw, fromDate, toDate));
        return "product-stats";
    }

    /**
     * lấy hàm thống kê doanh thu theo tháng, năm
     * lấy chọn lọc theo tên sản phẩm phải giống từ tìm kiếm truyền vào vì
     * nếu lọc theo tên product có chứa từ tìm kiếm thì sẽ không biết đang lọc theo những sản phẩm nào
     * và khoảng thời gian đặt hàng các sản phẩm đó
     * hàm trả về list các mảng Object, mỗi mảng chứa tháng, năm và doanh thu của tháng năm đó
     * và gửi kết quả của hàm vào 1 model
     */
    @GetMapping("/product-month-stats")
    public String productMonthStats(Model model, @RequestParam(required = false) Map<String, String> params) throws ParseException {

        // lấy các tham số để lọc dữ liệu
        String kw = params.get("kw");
        String fDate = params.get("fromDate");
        String tDate = params.get("toDate");

        // nếu có param tên sản phẩm tìm kiếm nhưng rỗng thì cho param này null luôn
        // để khi truyền vào hàm truy vấn thấy tên null thì ko truy vấn tên nữa và trả ra tất cả các bản ghi
        // vì đây là tìm kiếm chuẩn xác theo tên sản phẩm, không phải là tên có chứa từ tìm kiếm
        // nên cho rỗng vào tức muốn lấy tất cả bản ghi thì hàm truy vấn lại không so sánh tên được
        // và ko lấy bản ghi nào. vì thế phải cho bằng null
        if (kw != null && kw.trim().isEmpty()) {
            kw = null;
        }

        // tạo 1 fomat để chuyển đổi string sang ngày tháng
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // tạo các biến ngày tháng để fomat từ ngày tháng kiểu String sang nó
        Date fromDate = null;
        Date toDate = null;

        // nếu tham số ngày bắt đầu ko null và ko rỗng
        // thì chuyển biến tham số dạng string sang dạng date
        if (fDate != null && !fDate.trim().isEmpty()) {
            fromDate = sdf.parse(fDate);
        }

        // nếu tham số ngày kết thúc ko null và ko rỗng
        // thì chuyển biến tham số dạng string sang dạng date
        // rồi tăng date lên 1 ngày vì ko rõ lý do gì mà
        // hàm lọc dữ liệu truy vấn csdl coi ngày kết thúc là ngày trước đó
        // chứ ko phải ngày hiện tại nên phải tăng lên 1 ngày
        if (tDate != null && !tDate.trim().isEmpty()) {
            toDate = sdf.parse(tDate);
            Calendar c = Calendar.getInstance();
            c.setTime(toDate);
            c.add(Calendar.DATE, 1);
            toDate = c.getTime();
        }

        // gửi model danh sách tên các product để người dùng chọn lọc dữ liệu theo tên product nào vì
        // nếu lọc theo tên product có chứa từ tìm kiếm thì sẽ không biết đang lọc theo những sản phẩm nào
        model.addAttribute("productsName", this.productService.getProductsName());

        // cho các tham số lọc vào hàm lấy dữ liệu và trả về list chứa dữ liệu
        // cho dữ liệu gửi vào 1 model
        model.addAttribute("productMonthStats", this.statsService.productMonthStats(kw, fromDate, toDate));
        return "product-month-stats";
    }

    /**
     * lấy hàm thống kê doanh thu theo ngày, tháng, năm
     * lấy chọn lọc theo tên sản phẩm phải giống từ tìm kiếm truyền vào vì
     * nếu lọc theo tên product có chứa từ tìm kiếm thì sẽ không biết đang lọc theo những sản phẩm nào
     * và khoảng thời gian đặt hàng các sản phẩm đó
     * hàm trả về list các mảng Object, mỗi mảng chứa ngày, tháng, năm và doanh thu của tháng năm đó
     * và gửi kết quả của hàm vào 1 model
     */
    @GetMapping("/product-day-stats")
    public String productDayStats(Model model, @RequestParam(required = false) Map<String, String> params) throws ParseException {

        // lấy các tham số để lọc dữ liệu
        String kw = params.get("kw");
        String fDate = params.get("fromDate");
        String tDate = params.get("toDate");

        // nếu có param tên sản phẩm tìm kiếm nhưng rỗng thì cho param này null luôn
        // để khi truyền vào hàm truy vấn thấy tên null thì ko truy vấn tên nữa và trả ra tất cả các bản ghi
        // vì đây là tìm kiếm chuẩn xác theo tên sản phẩm, không phải là tên có chứa từ tìm kiếm
        // nên cho rỗng vào tức muốn lấy tất cả bản ghi thì hàm truy vấn lại không so sánh tên được
        // và ko lấy bản ghi nào. vì thế phải cho bằng null
        if (kw != null && kw.trim().isEmpty()) {
            kw = null;
        }

        // tạo 1 fomat để chuyển đổi string sang ngày tháng
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // tạo các biến ngày tháng để fomat từ ngày tháng kiểu String sang nó
        Date fromDate = null;
        Date toDate = null;

        // nếu tham số ngày bắt đầu ko null và ko rỗng
        // thì chuyển biến tham số dạng string sang dạng date
        if (fDate != null && !fDate.trim().isEmpty()) {
            fromDate = sdf.parse(fDate);
        }

        // nếu tham số ngày kết thúc ko null và ko rỗng
        // thì chuyển biến tham số dạng string sang dạng date
        // rồi tăng date lên 1 ngày vì ko rõ lý do gì mà
        // hàm lọc dữ liệu truy vấn csdl coi ngày kết thúc là ngày trước đó
        // chứ ko phải ngày hiện tại nên phải tăng lên 1 ngày
        if (tDate != null && !tDate.trim().isEmpty()) {
            toDate = sdf.parse(tDate);
            Calendar c = Calendar.getInstance();
            c.setTime(toDate);
            c.add(Calendar.DATE, 1);
            toDate = c.getTime();
        }

        // gửi model danh sách tên các product để người dùng chọn lọc dữ liệu theo tên product nào vì
        // nếu lọc theo tên product có chứa từ tìm kiếm thì sẽ không biết đang lọc theo những sản phẩm nào
        model.addAttribute("productsName", this.productService.getProductsName());

        // cho các tham số lọc vào hàm lấy dữ liệu và trả về list chứa dữ liệu
        // cho dữ liệu gửi vào 1 model
        model.addAttribute("productDayStats", this.statsService.productDayStats(kw, fromDate, toDate));
        return "product-day-stats";
    }
}
