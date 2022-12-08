/*
 *
 */
package com.nhale.controllers;

import com.nhale.pojos.Comment;
import com.nhale.pojos.User;
import com.nhale.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author LeNha
 */
@RestController
public class ApiCommentController {
    @Autowired
    private CommentService commentService;

    /**
     * yêu cầu client gửi cho rest server này method dạng post
     * thêm comment vào csdl với các tham số nhận được từ body của url gửi dạng post
     * @param params là dữ liệu dạng Map<> nhận từ body của request nên thêm annotation @RequestBody
     * @return cho client json của comment đã thêm
     */
    @PostMapping(value = "/api/add-comment", consumes = {"*/*"})
    public ResponseEntity<Comment> addComment(@RequestBody Map<String, String> params, HttpSession session) {
        // lấy user trong session lưu thông tin tài khoản đã đăng nhập
        User user = (User) session.getAttribute("currentUser");

        // nếu có session tài khoản đã đăng nhập thì mới tiếp tục thêm comment vào csdl
        if (user != null) {
            // có thể gặp lỗi vì int không thể nhận giá trị null
            try {
                // lấy các tham số nội dung comment và sản phẩm comment
                String content = params.get("content");
                int productId = Integer.parseInt(params.get("productID"));

                // gọi hàm thêm comment vào csdl với các tham số nội dung, sản phẩm comment lấy từ clent
                // user lấy từ session đăng nhập
                // và trả lại 1 đối tượng comment đã liên kết trong csdl
                Comment c = this.commentService.addComment(content, productId, user);

                // trả về cho view json của đối tượng comment với status là tạo mới
                return new ResponseEntity<>(c, HttpStatus.CREATED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // nếu có lỗi chỉ trả về status xấu
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
}
