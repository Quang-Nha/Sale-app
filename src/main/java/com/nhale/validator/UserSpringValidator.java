/*
 *
 */
package com.nhale.validator;

import com.nhale.pojos.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author LeNha
 */
@Component
public class UserSpringValidator implements Validator {
    /**
     * khai báo class cần xác thực là {@link com.nhale.pojos.User}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    /**
     * lấy class ra và xác minh các thuộc tính của nó
     *
     * @param target class đã khai báo
     * @param errors xuất ra lỗi tương ứng với thuộc tính đang xác thực
     */
    @Override
    public void validate(Object target, Errors errors) {
        // lấy class đã khai báo ra là User
        User u = (User) target;

        // xác thực hợp lệ giá trị thuộc tính confirmPassword
        // nếu confirmPassword không trùng với password thì xuất ra lỗi bằng errors.rejectValue và
        // xác định thuộc tính lỗi là confirmPassword bằng tham số đầu tiên "confirmPassword"
        if (!u.getPassword().equals(u.getConfirmPassword())) {
            // tham số đầu tiền phải trùng tên với thuộc tính đang check của class đang check (confirmPassword)
            // tham số thứ hai là tin nhắn của lỗi
            errors.rejectValue("confirmPassword", "user.confirmPassword.myErr");
        }

        // nếu 1 trong các giá trị là rỗng khi điền form User thì cũng báo lỗi,
        // sử dụng tham số đầu tiên "confirmPassword" để báo lỗi chung cho tất cả cá thuộc tính
        if (u.getEmail().isBlank() || u.getUsername().isBlank() || u.getFirstName().isBlank()
                || u.getLastName().isBlank() || u.getPassword().isBlank()
                || u.getConfirmPassword().isBlank()) {

            errors.rejectValue("confirmPassword", "user.empty.myErr");
        }

//        // nếu file nhận được không có dữ liệu thì xuất lỗi rỗng
//        if (u.getFile().getSize() == 0) {
//            errors.rejectValue("file", "user.file.nullErr");
//        }

        // nếu file nhận được không có dữ liệu thì xuất lỗi rỗng
        if (u.getFile().getSize() == 0) {
            errors.rejectValue("avatar", "user.file.nullErr");
        }
    }
}
