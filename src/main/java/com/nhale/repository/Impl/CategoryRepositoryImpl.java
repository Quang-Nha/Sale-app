/*
 *
 */
package com.nhale.repository.Impl;

import com.nhale.pojos.Category;
import com.nhale.repository.CategoryRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author LeNha
 */
@Repository
@Transactional
public class CategoryRepositoryImpl implements CategoryRepository {
    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    @Override
    public List<Category> getCategories() {
        Session session = Objects.requireNonNull(this.sessionFactory.getObject()).getCurrentSession();

        List<Category> categories = new ArrayList<>();

        try {
            Query<Category> query = session.createQuery("from Category");
            categories = query.getResultList();
            session.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return categories;
    }

    @Override
    public Category geCategoryById(int id) {
        Session session = Objects.requireNonNull(this.sessionFactory.getObject()).getCurrentSession();

        return session.get(Category.class, id);
    }
}
