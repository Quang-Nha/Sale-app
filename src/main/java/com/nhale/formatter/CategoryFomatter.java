/*
 *
 */
package com.nhale.formatter;

import com.nhale.pojos.Category;
import com.nhale.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/**
 * hướng dẫn cách chuyển đổi dữ liệu khác kiểu sang kiểu của class Email
 * @author LeNha
 */
public class CategoryFomatter implements Formatter<Category> {
    @Autowired
    private CategoryService categoryService;

    /**
     * chuyển từ kiểu khác là String sang kiểu {@link Category}
     */
    @Override
    public Category parse(String id, Locale locale) throws ParseException {
        // lấy String id gán cho thuộc tính id của Category và trả về Category
        Category category = new Category();
        category.setId(Integer.valueOf(id));
        return category;
    }

    /**
     * chuyển từ kiểu Category sang String
     * chuyển giá trị thuộc tính name của Category sang cho kiểu khác là String
     */
    @Override
    public String print(Category category, Locale locale) {
        return category.getName();
    }
}
