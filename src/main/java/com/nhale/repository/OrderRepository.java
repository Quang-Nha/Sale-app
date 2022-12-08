package com.nhale.repository;

import com.nhale.pojos.Cart;
import com.nhale.pojos.User;

import java.util.Map;

public interface OrderRepository {
    /**
     * thêm đơn hang vào csdl
     * @param cartMap chứa các giỏ hàng {@link Cart}
     * @param user tài khoản đang đăng nhập
     */
    boolean addReceipt(Map<Integer, Cart> cartMap, User user);
}
