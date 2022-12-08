package com.nhale.service;

import com.nhale.pojos.Comment;
import com.nhale.pojos.User;

public interface CommentService {
    /**
     * thêm comment vào csdl
     * @param content nội dung comment
     * @param productId id sản phẩm đang comment
     * @param user tài khoản đăng comment
     * @return đối tượng comment đã được thêm vào csdl để api trả về cho client, đưa cho javascript xử lý
     */
    Comment addComment(String content, int productId, User user);
}
