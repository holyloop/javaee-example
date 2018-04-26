package com.github.holyloop.service;

import java.util.Set;

import com.github.holyloop.dto.UserDTO;
import com.github.holyloop.entity.User;
import com.github.holyloop.exception.UsernameExistedException;

public interface UserService {

    User getOneByUsername(String username);

    Set<String> listRolesByUsername(String username);

    Set<String> listPermissionsByUsername(String username);

    void addUser(UserDTO user) throws UsernameExistedException;

}
