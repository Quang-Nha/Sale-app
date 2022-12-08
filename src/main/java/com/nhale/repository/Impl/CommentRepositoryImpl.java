/*
 *
 */
package com.nhale.repository.Impl;

import com.nhale.pojos.Comment;
import com.nhale.repository.CategoryRepository;
import com.nhale.repository.CommentRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Objects;

/**
 * @author LeNha
 */
@Repository
@Transactional
public class CommentRepositoryImpl implements CommentRepository {
    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    @Override
    public Comment addComment(Comment comment) {
        Session session = Objects.requireNonNull(this.sessionFactory.getObject()).getCurrentSession();

        try {
            session.save(comment);
            session.flush();
            return comment;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
