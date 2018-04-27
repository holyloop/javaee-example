package com.github.holyloop.repository;

import java.util.List;

public interface BaseJpaRepository<T> {

    void save(T entity);

    void delete(T entity);

    void deleteById(Long id);

    void update(T entity);

    T findById(Long id);

    List<T> findAll();

}
