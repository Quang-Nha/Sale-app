/*
 *
 */
package com.nhale.repository.Impl;

import com.nhale.pojos.Category;
import com.nhale.pojos.OrderDetail;
import com.nhale.pojos.Product;
import com.nhale.pojos.SaleOrder;
import com.nhale.repository.StatsRepository;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author LeNha
 */
@Repository
@Transactional
public class StatsRepositoryImpl implements StatsRepository {
    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    /**
     * @return list các mảng Object[],
     * 1 mảng gồm các phần tử id của danh mục, tên danh mục và phần tử chứa số lượng sản phẩm có trong danh mục đó
     */
    @Override
    public List<Object[]> cateStats() {
        Session session = Objects.requireNonNull(sessionFactory.getObject()).getCurrentSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);
        Root<Product> rootP = criteriaQuery.from(Product.class);
        Root<Category> rootC = criteriaQuery.from(Category.class);

        // join 2 bảng Product và Category
        criteriaQuery.where(builder.equal(rootP.get("category"), rootC.get("id")));
        // lấy các cột id, name của Category, cột 3 đếm các id của Product trong nhóm gộp theo id của Category
        criteriaQuery.multiselect(rootC.get("id"), rootC.get("name"), builder.count(rootP.get("id")));

        // nhóm theo id của Category
        criteriaQuery.groupBy(rootC.get("id"));

        // xắp xếp tăng dần theo id của Category
        criteriaQuery.orderBy(builder.asc(rootC.get("id")));

        Query query = session.createQuery(criteriaQuery);

        List<Object[]> objects = query.getResultList();

        return objects;
    }

    /**
     * thống kê doanh thu các sản phẩm theo khoảng thời gian
     * lấy chọn lọc theo tên sản phẩm có chứa từ tìm kiếm truyền vào
     * và khoảng thời gian đặt hàng các sản phẩm đó
     *
     * @param kw       từ tìm kiếm
     * @param fromDate từ ngày
     * @param toDate   đến ngày
     * @return list các mảng Object, mỗi mảng chứa sản phẩm và doanh thu của nó
     */
    @Override
    public List<Object[]> productStats(String kw, Date fromDate, Date toDate) {
        Session session = Objects.requireNonNull(sessionFactory.getObject()).getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);
        // lấy 3 bảng Product, SaleOrder, OrderDetail
        Root<Product> rootP = criteriaQuery.from(Product.class);
        Root<SaleOrder> rootO = criteriaQuery.from(SaleOrder.class);
        Root<OrderDetail> rootD = criteriaQuery.from(OrderDetail.class);

        // tạo 1 list các vị từ/predicates để chứa các vị từ của where
        List<Predicate> predicates = new ArrayList<>();

        // 2 vị từ đầu tiên là join 3 bảng lại tại Product.id = OrderDetail.product
        // và SaleOrder.id = OrderDetail.order
        predicates.add(builder.equal(rootP.get("id"), rootD.get("product")));
        predicates.add(builder.equal(rootO.get("id"), rootD.get("order")));

        // nếu có từ tìm kiếm thì thêm vị từ like Product.name có chứa từ tìm kiếm
        if (kw != null) {
            predicates.add(builder.like(rootP.get("name"), String.format("%%%S%%", kw)));
        }

        // nếu có ngày bắt đầu thì thêm vị từ SaleOrder.createdDate >= ngày bắt đầu
        if (fromDate != null) {
            predicates.add(builder.greaterThanOrEqualTo(rootO.get("createdDate"), fromDate));
        }

        // nếu có ngày kết thúc thì thêm vị từ SaleOrder.createdDate <= ngày kết thúc
        if (toDate != null) {
            predicates.add(builder.lessThanOrEqualTo(rootO.get("createdDate"), toDate));
        }

        // thêm vào vị từ trên vào phần where của truy vấn bằng cách chuyển list sang mảng và khai báo cách chuyển
        // predicates.toArray(new Predicate[]{}), cách chuyển là new Predicate[]{}
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));
//        criteriaQuery.where(builder.and(predicates.toArray(new Predicate[]{})));

        // nhóm lại theo Product.id
        criteriaQuery.groupBy(rootP.get("id"));

        // xắp xếp tăng dần theo Product.id
        criteriaQuery.orderBy(builder.asc(rootP.get("id")));

        // lấy các cột tại Product.id, Product.name, và cột 3 lấy tích của
        // bản ghi giá sản phẩm OrderDetail.unitPrice nhân với số lượng sản phẩm OrderDetail.num
        // và cộng tổng các tích lại trong 1 nhóm Product.id(do nhóm theo Product.id)
        criteriaQuery.multiselect(rootP.get("id"), rootP.get("name"),
                builder.sum(builder.prod(rootD.get("unitPrice"), rootD.get("num"))));

        Query query = session.createQuery(criteriaQuery);

        return query.getResultList();
    }

    /**
     * thống kê doanh thu các sản phẩm theo từng mốc thời gian theo tháng, năm
     * lấy chọn lọc theo tên sản phẩm phải giống từ tìm kiếm truyền vào
     * và khoảng thời gian đặt hàng các sản phẩm đó
     *
     * @param kw       từ tìm kiếm
     * @param fromDate từ ngày
     * @param toDate   đến ngày
     * @return list các mảng Object, mỗi mảng chứa tháng, năm và doanh thu của tháng năm đó
     */
    @Override
    public List<Object[]> productMonthStats(String kw, Date fromDate, Date toDate) {
        Session session = Objects.requireNonNull(sessionFactory.getObject()).getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);
        // lấy 3 bảng Product, SaleOrder, OrderDetail
        Root<Product> rootP = criteriaQuery.from(Product.class);
        Root<SaleOrder> rootO = criteriaQuery.from(SaleOrder.class);
        Root<OrderDetail> rootD = criteriaQuery.from(OrderDetail.class);

        // tạo 1 list các vị từ/predicates để chứa các vị từ của where
        List<Predicate> predicates = new ArrayList<>();

        // 2 vị từ đầu tiên là join 3 bảng lại tại Product.id = OrderDetail.product
        // và SaleOrder.id = OrderDetail.order
        predicates.add(builder.equal(rootP.get("id"), rootD.get("product")));
        predicates.add(builder.equal(rootO.get("id"), rootD.get("order")));

        // nếu có từ tìm kiếm thì thêm vị từ like Product.name phải giống từ tìm kiếm
        if (kw != null) {
            predicates.add(builder.like(rootP.get("name"), kw));
        }

        // nếu có ngày bắt đầu thì thêm vị từ SaleOrder.createdDate >= ngày bắt đầu
        if (fromDate != null) {
            predicates.add(builder.greaterThanOrEqualTo(rootO.get("createdDate"), fromDate));
        }

        // nếu có ngày kết thúc thì thêm vị từ SaleOrder.createdDate <= ngày kết thúc
        if (toDate != null) {
            predicates.add(builder.lessThanOrEqualTo(rootO.get("createdDate"), toDate));
        }

        // thêm vào vị từ trên vào phần where của truy vấn bằng cách chuyển list sang mảng và khai báo cách chuyển
        // predicates.toArray(new Predicate[]{}), cách chuyển là new Predicate[]{}
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));
//        criteriaQuery.where(builder.and(predicates.toArray(new Predicate[]{})));

        // nhóm lại theo từng cặp 2 cột tháng và năm giống nhau của SaleOrder.createdDate
        // cột 1 gọi hàm lấy giá trị tháng của SaleOrder.createdDate
        // cột 2 gọi hàm lấy giá trị năm của SaleOrder.createdDate
        criteriaQuery.groupBy(builder.function("MONTH", Integer.class, rootO.get("createdDate")),
                builder.function("YEAR", Integer.class, rootO.get("createdDate")), rootO.get("createdDate"));

        // xắp xếp tăng dần theo thời gian SaleOrder.createdDate
        criteriaQuery.orderBy(builder.asc(rootO.get("createdDate")));

        // lấy các cột tại tháng của SaleOrder.createdDate, năm của SaleOrder.createdDate, và cột 3 lấy tích của
        // bản ghi giá sản phẩm OrderDetail.unitPrice nhân với số lượng sản phẩm OrderDetail.num
        // và cộng tổng các tích lại trong 1 nhóm tháng và năm giống nhau của SaleOrder.createdDate
        // (do nhóm theo tháng và năm của SaleOrder.createdDate)
        criteriaQuery.multiselect(builder.function("MONTH", Integer.class, rootO.get("createdDate")),
                builder.function("YEAR", Integer.class, rootO.get("createdDate")),
                builder.sum(builder.prod(rootD.get("unitPrice"), rootD.get("num"))));

        Query query = session.createQuery(criteriaQuery);

        return query.getResultList();
    }

    /**
     * thống kê doanh thu các sản phẩm theo từng mốc thời gian theo ngày, tháng, năm
     * lấy chọn lọc theo tên sản phẩm phải giống từ tìm kiếm truyền vào
     * và khoảng thời gian đặt hàng các sản phẩm đó
     *
     * @param kw       từ tìm kiếm
     * @param fromDate từ ngày
     * @param toDate   đến ngày
     * @return list các các mảng Object, mỗi mảng chứa ngày, tháng, năm và doanh thu của tháng năm đó
     */
    @Override
    public List<Object[]> productDayStats(String kw, Date fromDate, Date toDate) {
        Session session = Objects.requireNonNull(sessionFactory.getObject()).getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);
        // lấy 3 bảng Product, SaleOrder, OrderDetail
        Root<Product> rootP = criteriaQuery.from(Product.class);
        Root<SaleOrder> rootO = criteriaQuery.from(SaleOrder.class);
        Root<OrderDetail> rootD = criteriaQuery.from(OrderDetail.class);

        // tạo 1 list các vị từ/predicates để chứa các vị từ của where
        List<Predicate> predicates = new ArrayList<>();

        // 2 vị từ đầu tiên là join 3 bảng lại tại Product.id = OrderDetail.product
        // và SaleOrder.id = OrderDetail.order
        predicates.add(builder.equal(rootP.get("id"), rootD.get("product")));
        predicates.add(builder.equal(rootO.get("id"), rootD.get("order")));

        // nếu có từ tìm kiếm thì thêm vị từ like Product.name phải giống từ tìm kiếm
        if (kw != null) {
            predicates.add(builder.like(rootP.get("name"), kw));
        }

        // nếu có ngày bắt đầu thì thêm vị từ SaleOrder.createdDate >= ngày bắt đầu
        if (fromDate != null) {
            predicates.add(builder.greaterThanOrEqualTo(rootO.get("createdDate"), fromDate));
        }

        // nếu có ngày kết thúc thì thêm vị từ SaleOrder.createdDate <= ngày kết thúc
        if (toDate != null) {
            predicates.add(builder.lessThanOrEqualTo(rootO.get("createdDate"), toDate));
        }

        // thêm vào vị từ trên vào phần where của truy vấn bằng cách chuyển list sang mảng và khai báo cách chuyển
        // predicates.toArray(new Predicate[]{}), cách chuyển là new Predicate[]{}
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));
//        criteriaQuery.where(builder.and(predicates.toArray(new Predicate[]{})));

        // nhóm lại theo từng cặp 3 cột ngày, tháng và năm giống nhau của SaleOrder.createdDate
        // cột 1 gọi hàm lấy giá trị ngày của SaleOrder.createdDate
        // cột 2 gọi hàm lấy giá trị tháng của SaleOrder.createdDate
        // cột 3 gọi hàm lấy giá trị năm của SaleOrder.createdDate
        criteriaQuery.groupBy(
                builder.function("DAY", Integer.class, rootO.get("createdDate")),
                builder.function("MONTH", Integer.class, rootO.get("createdDate")),
                builder.function("YEAR", Integer.class, rootO.get("createdDate")), rootO.get("createdDate"));

        // xắp xếp tăng dần theo thời gian SaleOrder.createdDate
        criteriaQuery.orderBy(builder.asc(rootO.get("createdDate")));

        // lấy các cột tại ngày của SaleOrder.createdDate,
        // tháng của SaleOrder.createdDate, năm của SaleOrder.createdDate, và cột 4 lấy tích của
        // bản ghi giá sản phẩm OrderDetail.unitPrice nhân với số lượng sản phẩm OrderDetail.num
        // và cộng tổng các tích lại trong 1 nhóm tháng và năm giống nhau của SaleOrder.createdDate
        // (do nhóm theo tháng và năm của SaleOrder.createdDate)
        criteriaQuery.multiselect(
                builder.function("DAY", Integer.class, rootO.get("createdDate")),
                builder.function("MONTH", Integer.class, rootO.get("createdDate")),
                builder.function("YEAR", Integer.class, rootO.get("createdDate")),
                builder.sum(builder.prod(rootD.get("unitPrice"), rootD.get("num"))));

        Query query = session.createQuery(criteriaQuery);

        return query.getResultList();
    }
}
