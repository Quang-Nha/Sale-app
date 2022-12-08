package com.nhale.repository;

import com.nhale.pojos.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> geProducts(String kw, int pageNum);

    /**
     * @param productId id của product cần lấy
     * @return product trong csdl theo id truyền vào
     */
    Product geProductById(int productId);

    boolean addOrUpdate(Product product);

    int countProduct(String search);

    /**
     * lấy num các sản phẩm được mua nhiều nhất
     */
    List<Object[]> getHotProducts(int num);

    /**
     * lấy num các sản phẩm được nhận xét nhiều nhất
     */
    List<Object[]> getMostDiscussProducts(int num);

    /**
     * @return danh sách tất cả tên các sản phẩm
     */
    List<String> getProductsName();

}
