/*
 *
 */
package com.nhale.service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nhale.pojos.Product;
import com.nhale.repository.ProductRepository;
import com.nhale.service.CategoryService;
import com.nhale.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author LeNha
 */
@Service
public class ProductServiceImpl implements ProductService {
    // bean để upload dữ liệu lên cloud
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryService categoryService;

    @Override
    public List<Product> geProducts(String kw, int pageNum) {
        return this.productRepository.geProducts(kw, pageNum);
    }

    @Override
    public Product geProductById(int productId) {
        return this.productRepository.geProductById(productId);
    }

    /**
     * upload ảnh của Product lên cloud cloudinary
     * thêm Product vào csdl
     */
    @Override
    public boolean addOrUpdate(Product product) {
        try {
            // thực hiện up load file bằng giá trị thuộc tính file của product
            // thuộc tính này chứa dữ liệu của file gửi từ form của client
            // method trả về 1 map có 1 key tên secure_url chứa link của hình ảnh đã tải lên cloud
            Map map = this.cloudinary.uploader().upload(product.getFile().getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));

            // set lại đường dẫn ảnh của Product là link ảnh đã upload lên cloud
            product.setImage((String) map.get("secure_url"));
            product.setCreatedDate(new Date());
            // do product có Category đang được set cho chỉ chứa id
            // nên cần lấy Category có id này trong csdl thì mới liên kết được với csdl
            // lấy xong rồi gán lại Category cho product
            product.setCategory(this.categoryService.geCategoryById(product.getCategory().getId()));
            // gọi repository và thêm Product vào csdl
            return this.productRepository.addOrUpdate(product);
        } catch (IOException e) {
            System.err.println("== ADD PRODUCT ==" + e.getMessage());
        }

        // nêu có lỗi xảy ra thì trả về false
        return false;
    }

    @Override
    public int countProduct(String search) {
        return this.productRepository.countProduct(search);
    }

    /**
     * lấy num các sản phẩm được mua nhiều nhất
     */
    @Override
    public List<Object[]> getHotProducts(int num) {
        return this.productRepository.getHotProducts(num);
    }

    /**
     * lấy num các sản phẩm được nhận xét nhiều nhất
     */
    @Override
    public List<Object[]> getMostDiscussProducts(int num) {
        return this.productRepository.getMostDiscussProducts(num);
    }

    /**
     * @return danh sách tất cả tên các sản phẩm
     */
    @Override
    public List<String> getProductsName() {
        return this.productRepository.getProductsName();
    }
}
