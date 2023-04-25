/*
 *
 */
package com.nhale.service.Impl;

import com.nhale.pojos.Comment;
import com.nhale.pojos.Product;
import com.nhale.pojos.User;
import com.nhale.repository.CommentRepository;
import com.nhale.repository.ProductRepository;
import com.nhale.repository.UserRepository;
import com.nhale.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author LeNha
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment addComment(String content, int productId, User user) {
        // lấy Product theo id
        Product p = this.productRepository.geProductById(productId);

        // Tạo comment và set các giá trị product và user, thời gian cho nó
        Comment c = new Comment();
        c.setContent(content);
        c.setProduct(p);
        c.setUser(user);
        c.setCreatedDate(new Date());

        // gọi hàm thêm comment vào csdl và trả về chính nó
        return this.commentRepository.addComment(c);
    }
}


