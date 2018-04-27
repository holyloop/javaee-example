package com.github.holyloop.repository;

import com.github.holyloop.entity.User;

public interface UserRepository extends BaseJpaRepository<User> {

    User getOneByUsername(String username);

}
