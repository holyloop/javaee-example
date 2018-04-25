package com.github.holyloop.repository.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class BaseJpaRepository<T> {

    @PersistenceContext
    private EntityManager em;
    protected Class<T> clazz;

    @SuppressWarnings("unchecked")
    public BaseJpaRepository() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        clazz = (Class<T>) pt.getActualTypeArguments()[0];
    }

    public void save(T entity) {
        em.persist(entity);
    }

    public void delete(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        em.remove(findById(id));
    }

    public void update(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }
        em.merge(entity);
    }

    public T findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return em.find(clazz, id);
    }

    public List<T> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> criteria = cb.createQuery(this.clazz);
        Root<T> root = criteria.from(this.clazz);

        criteria.select(root);
        return em.createQuery(criteria).getResultList();
    }

}
