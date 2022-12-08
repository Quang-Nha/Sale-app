/*
 *
 */
package com.nhale.handlers;

import com.nhale.pojos.Cart;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * đối tượng này sẽ được gọi khi logout thành công
 * method của nó sẽ được gọi và thực thi các công việc cần thiết sau khi logout thành công
 * xóa session chứa thông tin tài khoản vừa logout
 * @author LeNha
 */
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    /**
     * xóa session chứa thông tin tài khoản vừa logout
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        request.getSession().removeAttribute("currentUser");

        Map<Integer, Cart> cartMap = (Map<Integer, Cart>) request.getSession().getAttribute("cart");

        request.getSession().setAttribute("cart", cartMap);
        response.sendRedirect(response.encodeURL(request.getContextPath() + "/login"));
//        request.getRequestDispatcher("/login").forward(request, response);
    }
}
