/*
 *
 */
package com.nhale.controllers;

import com.nhale.pojos.Comment;
import com.nhale.pojos.Product;
import com.nhale.service.ProductService;
import com.nhale.validator.ProductBeanAndSpingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author LeNha
 */
@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    // bean của class Validator để xác minh các lỗi của class dùng làm ModelAttribute.
    //  Chỉ dùng cho spring validator. muốn cho bean validator thì cho bean validator vào trong spring validator
    @Autowired
    private ProductBeanAndSpingValidator productBeanAndSpingValidator;

    /**
     * phương thức được gọi trước khi các action được thực thi
     * để xác minh hợp lệ các giá trị nhập từ client
     * @param binder xác định class Validator được sử đụng để xác minh
     * Chỉ dùng cho spring validator. muốn cho bean validator thì cho bean validator vào trong spring validator
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(productBeanAndSpingValidator);
    }

    /**
     * chuyển đến trang chi tiết sản phẩm với tham số cuối url PathVariable là id sản phẩm
     */
    @GetMapping("/product/{productId}")
    public String detail(@PathVariable (value = "productId") int proId,
                         Model model) {
        Product p = this.productService.geProductById(proId);

        // lấy các comments của product và xắp xếp nó theo thời gian, Comment đã implements Comparable<Comment>
        Set<Comment> comments = p.getComments();
        List<Comment> commentList = new ArrayList<>(comments);
        Collections.sort(commentList);

        // trả cho view 2 model chứa sản phẩm và comment
        model.addAttribute("product", p);
        model.addAttribute("comments", commentList);

        return "product-detail";
    }

    @GetMapping("admin/addProduct")
    public String list(Model model) {
        model.addAttribute("product", new Product());

        return "add-product";
    }

    /**
     * up load dữ liệu lên cloud cloudinary
     * và thêm product vào csdl
     * @param product nhận lại model product do form gửi về có thuộc tính tên file chứa file form gửi
     */
    @PostMapping("admin/addProduct")
    public String add(Model model,
                      // @Valid là xác nhận dữ liệu từ form hợp lệ mới truyền cho ModelAttribute product
                      @ModelAttribute(value = "product") @Valid Product product,
                      // BindingResult xác nhận xem có lỗi không hợp lệ từ form không
                      BindingResult result){

        if (!result.hasErrors()) {
            // thực hiện upload dữ liệu lên cloud cloudinary và thêm product vào csdl rồi trả về kết quả
            if (this.productService.addOrUpdate(product)) {

                // nếu không lỗi quay về trang chủ
                return "redirect:/";
            } else {
                model.addAttribute("errMsg", "Something wrong!!!");
            }

        }

        // nếu có lỗi quay lại trang hiện tại luôn
        return "add-product";
    }
}
