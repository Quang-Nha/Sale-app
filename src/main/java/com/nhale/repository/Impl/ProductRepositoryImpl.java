/*
 *
 */
package com.nhale.repository.Impl;

import com.nhale.pojos.Comment;
import com.nhale.pojos.OrderDetail;
import com.nhale.pojos.Product;
import com.nhale.repository.ProductRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

/**
 * @author LeNha
 */
@Repository
@Transactional
public class ProductRepositoryImpl implements ProductRepository {

    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    /**
     * lấy danh sách Product có productName chứa từ khóa kw và trang sản phẩm số bao nhiêu truyền vào
     */
    @Override
    public List geProducts(String kw, int pageNum) {
        Session session = Objects.requireNonNull(sessionFactory.getObject()).getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = builder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);

        if (!kw.trim().isEmpty()) {
            Predicate p = builder.like(root.get("name").as(String.class),
                    String.format("%%%s%%", kw));
            criteriaQuery.where(p);
        }

        criteriaQuery.orderBy(builder.desc(root.get("id")));

        int max = 9;

        // truy vấn chỉ lấy max = 9 sản phẩm theo key và tên trang số bao nhiêu cần đến(pageNum)
        // setFirstResult lấy sản phẩm trong list truy vấn được tính từ chỉ số (pageNum - 1) * max
        // vd trang 1 lấy từ chỉ số 0, (1-1) * 9 = 0
        Query query = session.createQuery(criteriaQuery)
                .setMaxResults(max)
                .setFirstResult((pageNum - 1) * max);

        return query.getResultList();
    }

    @Override
    public Product geProductById(int productId) {
        Session session = Objects.requireNonNull(sessionFactory.getObject()).getCurrentSession();
        return session.get(Product.class, productId);
    }

    /**
     * thêm 1 Product vào csdl
     */
    @Override
    public boolean addOrUpdate(Product product) {
        Session session = Objects.requireNonNull(sessionFactory.getObject()).getCurrentSession();
        try {
            session.save(product);
            session.flush();

            return true;
        } catch (Exception e) {
            System.err.println("== ADD PRODUCT ERR ==" + e.getMessage());
            // nếu đã bắt được lỗi nhưng truy vấn vẫn chạy và sau đó làm sập chương trình
            // thì clear để dừng truy vấn tránh chương trình bị sập
            session.clear();
            e.printStackTrace();
        }

        return false;
    }

    /**
     * lấy số lượng các Product trong bảng Product có tên chứa từ tìm kiếm
     */
    @Override
    public int countProduct(String search) {
        Session session = Objects.requireNonNull(sessionFactory.getObject()).getCurrentSession();

        Query query = session.createQuery("select count (*) from Product where name like :search");

        // nếu từ tìm kiếm rỗng hoặc null thì lấy tất cả bản ghi không thì
        // lấy các bản ghi có giá trị trường tên chứa từ tìm kiếm
        if (search != null && !search.trim().isEmpty()) {
            query.setParameter("search", "%" + search + "%");
        } else {
            query.setParameter("search", "%%");
        }

        // lấy số lượng các Product trong bảng Product
        return Integer.parseInt(query.getSingleResult().toString());
    }

    /**
     * lấy num các sản phẩm được mua nhiều nhất
     * join theo bảng Product và bảng OrderDetail tại id của Product
     * nhóm theo id Product và đếm số bản ghi trong nhóm đó là ra 1 Product đã mua bao nhiêu lần
     * xắp xếp theo số bản ghi trong nhóm giảm dần thì những sản phẩm mua nhiều nhất ở trên đầu
     * lấy số lượng bản ghi mong muốn
     */
    @Override
    public List<Object[]> getHotProducts(int num) {
        Session session = Objects.requireNonNull(sessionFactory.getObject()).getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);
        Root<Product> rootP = criteriaQuery.from(Product.class);
        Root<OrderDetail> detailRoot = criteriaQuery.from(OrderDetail.class);

        // join tại id của Product = OrderDetail.product
        // (product là bảng cha, khóa ngoại kết nối tới bảng Product chính là thuộc tính product của bảng OrderDetail)
        criteriaQuery.where(builder.equal(rootP.get("id"), detailRoot.get("product")));
        // chọn lấy các trường product.id, product.name, product.price, product.image,
        // và đếm số lượng các product.id trong 1 nhóm(truy vấn có sử dụng group by)
        // các trường này sẽ là các phần tử của 1 mảng Object[]
        criteriaQuery.multiselect(rootP.get("id"), rootP.get("name"), rootP.get("price")
                ,rootP.get("image"), builder.count(rootP.get("id")));

        // nhóm theo product.id
        criteriaQuery.groupBy(rootP.get("id"));
        // xắp xếp
        criteriaQuery.orderBy(builder.desc(builder.count(rootP.get("id"))));

        Query query = session.createQuery(criteriaQuery);
        query.setMaxResults(num);

        // trả về list chứa các mảng Object[]
        return query.getResultList();
    }

    /**
     * lấy num các sản phẩm được nhận xét nhiều nhất
     */
    @Override
    public List<Object[]> getMostDiscussProducts(int num) {
        Session session = Objects.requireNonNull(sessionFactory.getObject()).getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);
        Root<Product> rootP = criteriaQuery.from(Product.class);
        Root<Comment> rootC = criteriaQuery.from(Comment.class);

        criteriaQuery.where(builder.equal(rootP.get("id"), rootC.get("product")));
        criteriaQuery.multiselect(rootP.get("id"), rootP.get("name"), rootP.get("price")
                ,rootP.get("image"), builder.count(rootP.get("id")));

        criteriaQuery.groupBy(rootP.get("id"));
        // xắp xếp theo số lượng comment trong nhóm của 1 sản phẩm giảm dần, sau đó
        // là xắp xếp theo id sản phẩm
        criteriaQuery.orderBy(builder.desc(builder.count(rootP.get("id"))),
                builder.desc(rootP.get("id")));

        Query query = session.createQuery(criteriaQuery);
        query.setMaxResults(num);

        return query.getResultList();
    }

    /**
     * @return danh sách tất cả tên các sản phẩm
     */
    @Override
    public List<String> getProductsName() {
        Session session = Objects.requireNonNull(sessionFactory.getObject()).getCurrentSession();

        Query query = session.createQuery("select name from Product ");

        return query.getResultList();
    }
}
