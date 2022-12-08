/*
 *
 */
package com.nhale.handlers;

import com.nhale.pojos.User;
import com.nhale.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * đối tượng này sẽ được gọi khi login thành công
 * method của nó sẽ được gọi và thực thi các công việc cần thiết sau khi login thành công
 * @author LeNha
 */
@Component // cần annotation này để sử dụng được @Autowired bean
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    /**
     * method này sẽ xử lý các công việc sau khi đăng nhập thành công
     * hệ thống sẽ cung cấp các tham số cần thiết như của 1 controller cho method của class này
     * lấy ra 1 đối tượng user trong csdl theo tên tài khoản đã đăng nhập
     * tạo 1 session chứa user này để dùng các thông tin của nó cho các hoạt động khác như set avata
     * @param request
     * @param response
     * @param authentication chứa tên tài khoản đã đăng nhập
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // lấy user trong csdl theo tên tài khoản đã đăng nhập, tên lấy từ authentication
        User user = userService.getUsers(authentication.getName()).get(0);

        // lưu user trên vào 1 session
        request.getSession().setAttribute("currentUser", user);

        // điều hướng về trang chủ, mã hóa url để theo dõi session, đảm bảo client ko cho phép cookie vẫn
        // hoạt động
        response.sendRedirect(response.encodeURL(request.getContextPath() + "/"));
    }
}
