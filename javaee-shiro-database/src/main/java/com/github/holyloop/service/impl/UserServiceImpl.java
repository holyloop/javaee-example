package com.github.holyloop.service.impl;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;

import com.github.holyloop.dto.UserDTO;
import com.github.holyloop.entity.RolePermissionRel;
import com.github.holyloop.entity.User;
import com.github.holyloop.entity.UserRoleRel;
import com.github.holyloop.exception.UsernameExistedException;
import com.github.holyloop.repository.UserRepository;
import com.github.holyloop.service.UserService;

@Stateless
public class UserServiceImpl implements UserService {

    @Inject
    private UserRepository userRepository;

    @Override
    public User findOneByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("username must not be null");
        }

        try {
            return userRepository.findOneByUsername(username);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Set<String> listRolesByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("username must not be null");
        }

        User user = userRepository.findOneByUsername(username);
        Set<String> roles = new HashSet<>();
        if (user != null && user.getUserRoleRels() != null) {
            for (UserRoleRel rel : user.getUserRoleRels()) {
                roles.add(rel.getRole().getRoleName());
            }
        }

        return roles;
    }

    @Override
    public Set<String> listPermissionsByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("username must not be null");
        }

        User user = userRepository.findOneByUsername(username);
        Set<String> permissions = new HashSet<>();
        if (user != null && user.getUserRoleRels() != null) {
            for (UserRoleRel rel : user.getUserRoleRels()) {
                for (RolePermissionRel permRel : rel.getRole().getRolePermissionRels()) {
                    permissions.add(permRel.getPermission().getPermissionStr());
                }
            }
        }

        return permissions;
    }

    @Override
    public void addUser(UserDTO user) throws UsernameExistedException {
        // TODO
    }

}
