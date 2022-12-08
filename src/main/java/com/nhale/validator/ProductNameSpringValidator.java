/*
 *
 */
package com.nhale.validator;

import com.nhale.pojos.Product;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

/**
 * tạo xác thực hợp lệ các giá trị thuộc tính cho class dùng làm ModelAttribute Product
 *
 * @author LeNha
 */
@Component
public class ProductNameSpringValidator implements Validator {
    /**
     * khai báo class cần xác thực là Product
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return Product.class.isAssignableFrom(clazz);
    }

    /**
     * lấy class ra và xác minh các thuộc tính của nó
     *
     * @param target class đã khai báo
     * @param errors xuất ra lỗi tương ứng với thuộc tính đang xác thực
     */
    @Override
    public void validate(Object target, Errors errors) {
        // lấy class đã khai báo ra là Product
        Product p = (Product) target;
//
//        try {
//            BigDecimal price = p.getPrice();
//        } catch (Exception e) {
//            errors.rejectValue("price", "product.price.valueErr");
//        }

        // nếu file nhận được không có dữ liệu thì xuất lỗi rỗng
        if (p.getFile().getSize() == 0) {
            errors.rejectValue("file", "product.file.nullErr");

        }
    }
}
