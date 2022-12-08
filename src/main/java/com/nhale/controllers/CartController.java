/*
 *
 */
package com.nhale.controllers;

import com.nhale.pojos.Cart;
import com.nhale.utils.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author LeNha
 */
@Controller
public class CartController {

    @GetMapping("/cart")
    public String cart(Model model, HttpSession session) {
        Map<Integer, Cart> cartMap = (Map<Integer, Cart>) session.getAttribute("cart");
        if (cartMap != null) {
            model.addAttribute("carts", cartMap.values());
        } else {
            model.addAttribute("carts", null);
        }

        // gọi hàm trả về map chứa tổng sản phẩm và tổng tiền gửi vào model
        model.addAttribute("cartStats", Utils.cartStats(cartMap));

        // xem có session thông báo thanh toán thành công không
        // nếu có tạo model cartStatus gửi thông báo đi thanh toán thành công
        // nếu không vẫn tạo model cartStatus không có nội dung gì
        if (session.getAttribute("cartStatus") != null) {
            model.addAttribute("cartStatus", "paySuccess");
        } else {
            model.addAttribute("cartStatus", "");
        }

        // xóa session thông báo thanh toán thành công vì chỉ cần hiển thị 1 lần
        // khi load lại trang này thì không cần nữa
        session.removeAttribute("cartStatus");

        return "cart";
    }
}
