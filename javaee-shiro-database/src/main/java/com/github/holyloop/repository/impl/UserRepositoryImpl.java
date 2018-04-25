package com.github.holyloop.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.github.holyloop.entity.User;
import com.github.holyloop.repository.UserRepository;

public class UserRepositoryImpl extends BaseJpaRepository<User> implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public User findOneByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("username must not be null");
        }

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        query.select(root).where(builder.equal(root.get("username"), username));
        return em.createQuery(query).getSingleResult();
    }

}
