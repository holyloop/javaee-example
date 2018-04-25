package com.github.holyloop.repository;

import com.github.holyloop.entity.User;

public interface UserRepository {

    User findOneByUsername(String username);

}
