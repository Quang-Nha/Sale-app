/*
 *
 */
package com.nhale.service.Impl;
import com.nhale.pojos.Cart;
import com.nhale.pojos.User;
import com.nhale.repository.OrderRepository;
import com.nhale.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author LeNha
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    /**
     * thêm đơn hang vào csdl
     * @param cartMap chứa các giỏ hàng {@link Cart}
     * @param user tài khoản đang đăng nhập
     */
    @Override
    public boolean addReceipt(Map<Integer, Cart> cartMap, User user) {
        if (cartMap != null) {
            return this.orderRepository.addReceipt(cartMap, user);
        }

        return false;
    }
}
