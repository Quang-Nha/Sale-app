/*
 *
 */
package com.nhale.repository.Impl;

import com.cloudinary.provisioning.Account;
import com.nhale.pojos.User;
import com.nhale.repository.UserRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

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
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    @Override
    public User getUserById(int userId) {
        Session session = Objects.requireNonNull(this.sessionFactory.getObject()).getCurrentSession();

        return session.get(User.class, userId);
    }

    @Override
    public boolean addUser(User user) {
        Session session = Objects.requireNonNull(sessionFactory.getObject()).getCurrentSession();

        // nếu lưu thành công thì return về true
        // ko thành công thì in lỗi và return về false
        try {
            session.save(user);
            session.flush();

            return true;
        } catch (Exception ex) {
            // nếu đã bắt được lỗi nhưng truy vấn vẫn chạy và sau đó làm sập chương trình
            // thì clear để dừng truy vấn tránh chương trình bị sập
            session.clear();
            System.err.println(ex.getMessage());
        }

        return false;
    }

    @Override
    public List<User> getUsers(String kw) {
        Session session = Objects.requireNonNull(sessionFactory.getObject()).getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        // truy vấn lấy tất cả bản ghi từ root, root chứa các bản ghi của bảng Account
        criteriaQuery.select(root);

        if (!kw.trim().isEmpty()) {
            Predicate p = builder.equal(root.get("username").as(String.class), kw.trim());
            criteriaQuery.where(p);
        }

        // tạo query từ criteriaQuery
        Query<User> query = session.createQuery(criteriaQuery);
//        query.setParameter("name", account);
        System.out.println(query.getResultList());
        return query.getResultList();
    }
}
