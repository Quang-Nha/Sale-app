/*
 *
 */
package com.nhale.controllers;

import com.nhale.pojos.User;
import com.nhale.service.UserService;
import com.nhale.validator.UserSpringValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author LeNha
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    // bean của class Validator để xác minh các lỗi của class dùng làm ModelAttribute.
    // Chỉ dùng cho spring validator.
    // muốn cho bean validator thì cho bean validator vào trong spring validator
    @Autowired
    private UserSpringValidator userSpringValidator;

    /**
     * phương thức được gọi trước khi các action được thực thi
     * để xác minh hợp lệ các giá trị nhập từ client
     * Chỉ dùng cho spring validator.
     * muốn cho bean validator thì cho bean validator vào trong spring validator
     * @param binder xác định class Validator được sử đụng để xác minh
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(userSpringValidator);

    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * đăng nhập thất bại thì chuyển lại trang login với các tham số của form nó đã gửi
     * nơi chuyển đến url này phải dùng forward nếu ko sẽ ko còn các tham số form
     */
    @PostMapping("/loginFail")
    public String loginFail() {

        return "login";
    }

    /**
     * khi login thành công thì forward về trang này
     * do forward nên lưu lại được các tham số url từ form đăng nhập
     * lấy tham số chứa tên tài khoản đã đăng nhập từ form rồi lấy đối tượng user trong csdl theo tên đó
     * tạo 1 session chứa user này để dùng các thông tin của nó cho các hoạt động khác như set avata
     * @return về trang chủ
     */
    @PostMapping("/loginSuccess")
    public String loginSuccess(HttpServletRequest request) {
        String username = request.getParameter("username");

        // lấy user trong csdl theo tên tài khoản đã đăng nhập, tên lấy từ authentication
        User user = userService.getUsers(username).get(0);

        // lưu user trên vào 1 session
        request.getSession().setAttribute("currentUser", user);

        // chuyển về trang chủ
        return "/";
    }

    @GetMapping("/register")
    public String registerView(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute(value = "user") @Valid User user,
                           BindingResult result, Model model) {
        // nếu có lỗi quay lại trang đăng ký để nhập lại và hiển thị lỗi
        if (result.hasErrors()) {
            return "register";
        }

        // nếu không có lỗi không hợp lệ thì thêm user vào csdl
        // nếu thêm vào csdl thành công thì chuyển về trang đăng nhập
        if (this.userService.addUser(user)) {
            return "redirect:/login";
        }

        // nếu có lỗi khi thêm user vào csdl tức là ko hợp lệ, có thể do đã tồn tại tên người dùng này rồi
        // thì thêm model báo lỗi và quay lại trang đăng ký để hiển thị và nhập lại
        model.addAttribute("errMsg", "Co loi xay ra, vui long dang ky lai!");
        return "register";
    }
}
