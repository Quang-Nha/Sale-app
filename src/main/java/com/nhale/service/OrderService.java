/*
 *
 */
package com.nhale.service;

import com.nhale.pojos.Cart;
import com.nhale.pojos.User;

import java.util.Map;

/**
 * @author LeNha
 */
public interface OrderService {
    /**
     * thêm đơn hang vào csdl
     * @param cartMap chứa các giỏ hàng {@link Cart}
     * @param user tài khoản đang đăng nhập
     */
    public boolean addReceipt(Map<Integer, Cart> cartMap, User user);
}
