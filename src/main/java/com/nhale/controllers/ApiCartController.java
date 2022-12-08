/*
 *
 */
package com.nhale.controllers;

import com.nhale.pojos.Cart;
import com.nhale.pojos.User;
import com.nhale.service.OrderService;
import com.nhale.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LeNha
 */
@RestController
public class ApiCartController {

    @Autowired
    private OrderService orderService;
    /**
     * thêm giỏ hàng vào map giỏ lưu trong session
     * @param cart giỏ cần thêm, lấy từ body trong hàm post của client gửi
     * @return số lượng sản phẩm có trong map giỏ
     */
    @PostMapping("/api/cart")
    public int addToCart(@RequestBody Cart cart, HttpSession session) {

        // lấy map giỏ trong session
        Map<Integer, Cart> cartMap = (Map<Integer, Cart>) session.getAttribute("cart");
        // nếu chưa có tạo mới 1 giỏ rỗng
        if (cartMap == null) {
            cartMap = new HashMap<>();
        }

        // lấy id của sản phẩm trong cart cần thêm vào map giỏ
        int productId = cart.getProductId();

        // nếu giỏ chứa sản phẩm đã có trong map
        // lấy giỏ ra theo id sản phẩm và tăng số lượng thêm 1
        if (cartMap.containsKey(productId)) {
            Cart c = cartMap.get(productId);
            c.setQuantity(c.getQuantity() + 1);
        } else {// giỏ chưa có trong map thì thêm giỏ vào map
            cartMap.put(productId, cart);
        }

        // set lại session chứa giỏ hàng bằng cartMap đã chỉnh sửa
        session.setAttribute("cart", cartMap);

        // trả về số lượng sản phẩm có trong map giỏ
        return Utils.countProductInCart(cartMap);
    }

    /**
     * update số lượng của 1 cart chứa sản phẩm trong map giỏ
     * nếu thành công trả về status OK
     * @param cart giỏ cần update số lượng
     * @return map chứa 2 cặp giá trị là tổng tiền và tổng sản phẩm có trong các giỏ
     */
    @PutMapping("/api/cart")
    public ResponseEntity<Map<String, Long>> updateCart(@RequestBody Cart cart, HttpSession session) {
        // lấy map giỏ trong session, chắc chắn ko null, vì update thì chắc chắn đã có session chứa giỏ rồi
        Map<Integer, Cart> cartMap = (Map<Integer, Cart>) session.getAttribute("cart");
//        // nếu chưa có tạo mới 1 giỏ rỗng
//        if (cartMap == null) {
//            cartMap = new HashMap<>();
//        }

        // lấy id của sản phẩm trong cart cần thêm vào map giỏ
        int productId = cart.getProductId();

        // nếu giỏ chứa sản phẩm đã có trong map
        // lấy giỏ ra theo id sản phẩm và thay số lượng sản phẩm trong giỏ bằng số lượng sản phẩm trong giỏ mới
        if (cartMap.containsKey(productId)) {
            Cart c = cartMap.get(productId);
            c.setQuantity(cart.getQuantity());
        }

//        session.setAttribute("cart", cartMap);
        return new ResponseEntity<>(Utils.cartStats(cartMap), HttpStatus.OK);
    }

    /**
     * xóa giỏ trong map giỏ có id sản phẩm truyền vào
     * @param productId id của sản phẩm cần xóa, giỏ chứa sản phẩm này sẽ bị xóa
     * @return map chứa 2 cặp giá trị là tổng tiền và tổng sản phẩm có trong các giỏ
     */
    @DeleteMapping("/api/cart/{productId}")
    public ResponseEntity<Map<String, Long>> deleteCart(HttpSession session,
                                     @PathVariable(value = "productId") int productId) {
        // lấy map giỏ trong session, chắc chắn ko null, vì xóa thì chắc chắn đã có session chứa giỏ rồi
        Map<Integer, Cart> cartMap = (Map<Integer, Cart>) session.getAttribute("cart");
        // xóa 1 giỏ trong map theo key là id của sản phẩm/ id này tương ứng với id sản phẩm của giỏ
        cartMap.remove(productId);

        return new ResponseEntity<>(Utils.cartStats(cartMap), HttpStatus.OK);
    }

    /**
     * thực hiện thanh toán
     * lưu đơn hàng vào csdl
     */
    @PostMapping("/api/cart/pay")
    public HttpStatus pay(HttpSession session) {
        // lấy user trong session lưu thông tin tài khoản đã đăng nhập
        User user = (User) session.getAttribute("currentUser");

        // nếu có session tài khoản đã đăng nhập thì mới thực hiện thanh toán
        if (user != null) {
            // lấy map giỏ trong session
            Map<Integer, Cart> cartMap = (Map<Integer, Cart>) session.getAttribute("cart");

            // gọi hàm thêm đơn hàng vào csdl truyền vào map giỏ và user đang đăng nhập
            // nếu thành công trả về status ok, ngược lại trả về status thất bại
            if (this.orderService.addReceipt(cartMap, user)) {
                // xóa session chứa đơn hàng đã thanh toán
                session.removeAttribute("cart");
                // tạo 1 session thông báo thanh toán thành công
                session.setAttribute("cartStatus", "paySuccess");
                return HttpStatus.OK;
            }
        }

        // nếu có lỗi return về status lỗi
        return HttpStatus.BAD_REQUEST;
    }
}
