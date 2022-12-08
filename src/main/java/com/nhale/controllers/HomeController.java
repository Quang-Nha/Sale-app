/*
 *
 */
package com.nhale.controllers;

import com.nhale.pojos.Cart;
import com.nhale.pojos.Category;
import com.nhale.pojos.Product;
import com.nhale.service.CategoryService;
import com.nhale.service.ProductService;
import com.nhale.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author LeNha
 */
@Controller
@ControllerAdvice
public class HomeController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @ModelAttribute
    public void commonAttr(Model model, HttpSession session) {
        // gửi model các chủng loại sản phẩm
        model.addAttribute("categories", this.categoryService.getCategories());

        // lấy map chứa các giỏ hàng
        Map<Integer, Cart> cartMap = (Map<Integer, Cart>) session.getAttribute("cart");
        int numProducts = 0;
        // nếu map ko null là có giỏ hàng thì gọi hàm đếm sản phẩm trong các giỏ và gán vào biến
        if (cartMap != null) {
            numProducts = Utils.countProductInCart(cartMap);
        }
        // gửi model số lượng sản phẩm có trong đơn hàng
        model.addAttribute("numProductsInCart", numProducts);
    }

    /**
     * hiển thị list các sản phẩm theo từ tìm kiếm và trang
     * hoặc hiển thị theo chủng loại và trang
     */
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(required = false) Map<String, String> params) {
        // nếu có tham số search thì lấy ko thì cho bằng ""
        String kw = params.getOrDefault("search", "");
        // nếu có tham số pageNum thì lấy không thì bằng 1
        int page = Integer.parseInt(params.getOrDefault("pageNum", "1"));
        // lấy tham số id của chủng loại
        String cateId = params.get("cateId");

        // nếu cateId null hoặc rỗng thì không lấy sản phẩm theo chủng loại mà lấy theo page/trang và từ tìm kiếm
        // nếu ko null thì lấy theo chủng loại và trang
        if (cateId == null || cateId.trim().isEmpty()) {
            // gửi model list sản phẩm theo param trang và từ tìm kiếm cho view để hiển thị
            model.addAttribute("products", this.productService.geProducts(kw, page));
            // gửi model số lượng sản phẩm truy vấn theo key tìm kiếm để xác định số trang
            model.addAttribute("numProduct", this.productService.countProduct(kw));

        } else {
            // lấy ra chủng loại theo cateId trong csdl
            Category category = this.categoryService.geCategoryById(Integer.parseInt(cateId));
            // lấy ra các sản phẩm cùng chủng loại trên bằng hàm getProducts vì đã cấu hình quan hệ one to many
            Set<Product> products = category.getProducts();

            // chuyển đổi Set<Product> products sang List products1 để có thể truy vấn theo chỉ số trong
            // vòng lặp ở dưới
            List<Product> products1 = new ArrayList<>(products);
            // xắp xếp lại products1 vì Set ban đầu không tuân theo thứ tự khi chèn vào
            Collections.sort(products1);

            // tạo List<Product> products2 để lấy các sản phẩm trong list
            // products1 theo param trang nhận được, mỗi trang 9 sản phẩm
            List<Product> products2 = new ArrayList<>();

            // xác định chỉ số đầu tiên của sản phẩm trong list products1 theo param trang
            // tính từ chỉ số (pageNum - 1) * max
            // vd trang 1 lấy từ chỉ số 0, (1-1) * 9 = 0, mỗi trang 9 sản phẩm
            int numBegin = (page - 1) * 9;

            // lấy các sản phẩm trong list products1 có trong trang cần tìm thêm vào cho products2
            // vị trí đầu tiên là numBegin đến chỉ số cuối là numBegin + 9 - 1
            for (int i = numBegin; i < products1.size(); i++) {
                if (i < numBegin + 9) {
                    products2.add(products1.get(i));
                }
            }

            // gửi model list sản phẩm theo param trang và chủng loại cho view để hiển thị
            model.addAttribute("products", products2);

            // gửi model số lượng sản phẩm truy vấn theo chủng loại để xác định số trang
            model.addAttribute("numProduct", products.size());

        }

        // gửi model 1 sản phẩm được mua nhiều nhất
        model.addAttribute("hotProducts", this.productService.getHotProducts(1));

        // gửi model 1 sản phẩm được thảo luận nhiều nhất
        model.addAttribute("disProducts", this.productService.getMostDiscussProducts(1));

        return "index";
    }
}
