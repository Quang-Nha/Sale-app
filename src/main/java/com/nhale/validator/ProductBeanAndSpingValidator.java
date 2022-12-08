/*
 *
 */
package com.nhale.validator;

import com.nhale.pojos.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * kết hợp cả beanValidator và spingValidator để xác thực hợp lệ các thuộc tính cho class dùng làm ModelAttribute Product
 * vì 2 loại validator không tự kết hợp mà hệ thống chỉ chọn 1 loại
 * bằng cách gọi beanValidator trong 1 class của spingValidator và xử lý
 * @author LeNha
 */
@Component
public class ProductBeanAndSpingValidator implements Validator {
    // lấy bean của class ProductNameSpringValidator được hệ thống tự tạo để xác thực spingValidator
    @Autowired
    private ProductNameSpringValidator productNameSpringValidator;
    // lấy bean của class Validator đã tạo trong class config để xác thực beanValidator
    @Autowired
    private javax.validation.Validator beanValidator;

    /**
     * khai báo class cần xác thực là Product
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return Product.class.isAssignableFrom(clazz);
    }

    /**
     * kết hợp cả 2 cách xác thực beanValidator và spingValidator
     * @param target class đã khai báo
     * @param errors xuất ra lỗi tương ứng với thuộc tính đang xác thực
     */
    @Override
    public void validate(Object target, Errors errors) {
        // lấy bean productNameValidator gọi lại hàm validate() và truyền lại các tham số của hàm validate() cho nó
        // bắt buộc gọi lại vì class mới là class được khai báo làm class xác thực trong controller cần dùng
        productNameSpringValidator.validate(target, errors);

        // lấy bean của beanValidator và gọi hàm validate truyền target để xác định class cần xác thực
        Set<ConstraintViolation<Object>> beansValid = this.beanValidator.validate(target);
        // lặp qua các set do hàm validate() trên trả về và xuất ra tất cả các lỗi của beanValidator.
        // set này chứa các lỗi của beanValidator, ko có thì size = 0
        // gọi hàm xuất lỗi của spingValidator là errors.rejectValue() truyền vào
        // 1 là thuộc tính của class bị lỗi, lấy thông tin từ lỗi của beanValidator là getPropertyPath().toString()
        // 2 là tin nhắn mặc định của lỗi, lấy thông tin từ lỗi của beanValidator là getMessageTemplate()
        // 3 là tin nhắn của lỗi, lấy thông tin từ lỗi của beanValidator là getMessage()
        for (ConstraintViolation<Object> obj : beansValid) {
            errors.rejectValue(
                    obj.getPropertyPath().toString(),
                    obj.getMessageTemplate(),
                    obj.getMessage());
        }
    }
}
