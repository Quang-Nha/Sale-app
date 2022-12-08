/*
 *
 */
package com.nhale.repository.Impl;

import com.nhale.pojos.Cart;
import com.nhale.pojos.OrderDetail;
import com.nhale.pojos.SaleOrder;
import com.nhale.pojos.User;
import com.nhale.repository.OrderRepository;
import com.nhale.repository.ProductRepository;
import com.nhale.repository.UserRepository;
import com.nhale.utils.Utils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author LeNha
 */
@Repository
@Transactional
public class OrderRepositoryImpl implements OrderRepository {
    @Autowired
    private LocalSessionFactoryBean sessionFactory;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * thêm đơn hang vào csdl
     * @param cartMap chứa các giỏ hàng {@link Cart}
     * @param user tài khoản đang đăng nhập
     */
    @Override
    // do có nhiều thao tác cập nhật bảng cha rồi cập nhật bảng con nên cần thêm cờ propagation
    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRED)
    public boolean addReceipt(Map<Integer, Cart> cartMap, User user) {
        try {

            Session session = Objects.requireNonNull(this.sessionFactory.getObject()).getCurrentSession();

            SaleOrder order = new SaleOrder();
            // set user cho đơn hàng là tài khoản đang đăng nhập
            order.setUser(user);

            // set date là thời gian hiện tại
            order.setCreatedDate(new Date());

            // lấy map chứa phần tử lưu tổng tiền
            Map<String, Long> stats = Utils.cartStats(cartMap);
            // set tổng tiền từ phần tử key amount trong map
            order.setAmount(BigDecimal.valueOf(stats.get("amount")));

            // thêm order vào csdl
            session.save(order);

            // thêm các bản ghi chi tiết đơn hàng chứa các sản phẩm vào bảng OrderDetail
            // lặp qua từng giỏ trang map cartMap để lấy ra sản phẩm và số lượng sản phẩm
            for (Cart c : cartMap.values()) {
                OrderDetail od = new OrderDetail();
                // set order cho OrderDetail là order vừa thêm vào csdl
                od.setOrder(order);
                // lấy Product theo id có trong giỏ và set cho OrderDetail
                od.setProduct(this.productRepository.geProductById(c.getProductId()));
                // set tiếp giá thì lấy của giỏ cũng là giá của sản phẩm và số lượng sản phẩm lấy từ giỏ
                od.setUnitPrice(c.getPrice());
                od.setNum(c.getQuantity());

                // thêm OrderDetail vao csdl
                session.save(od);
            }
            session.flush();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
