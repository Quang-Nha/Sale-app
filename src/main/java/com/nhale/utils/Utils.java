/*
 *
 */
package com.nhale.utils;

import com.nhale.pojos.Cart;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LeNha
 */
public class Utils {

    /**
     * đếm tổng số lượng sản phẩm trong tất cả các giỏ của map
     * số lượng không phải số loại sản phẩm
     */
    public static int countProductInCart(Map<Integer, Cart> cartMap) {
        if (cartMap == null) {
            return 0;
        }
        int quantity = 0;
        for (Cart c : cartMap.values()) {
            quantity += c.getQuantity();
        }

        return quantity;
    }

    /**
     * tính tổng tiền của các giỏ trong map
     * đếm tổng số lượng sản phẩm trong tất cả các giỏ của map
     * @param cartMap map chứa các giỏ
     * @return map chứa 2 cặp giá trị là tổng tiền và tổng sản phẩm có trong các giỏ
     */
    public static Map<String, Long> cartStats(Map<Integer, Cart> cartMap) {
        if (cartMap == null) {
            return null;
        }
        Map<String, Long> kq = new HashMap<>();
        int q = 0;
        long s = 0L;
        for (Cart c : cartMap.values()) {
            q += c.getQuantity();
            s += c.getQuantity() * Long.parseLong(String.valueOf(c.getPrice()));
        }

        kq.put("counter", (long) q);
        kq.put("amount", s);

        return kq;
    }
}
