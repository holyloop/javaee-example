package com.github.holyloop.rest.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.github.holyloop.dto.UserDTO;
import com.github.holyloop.exception.UsernameExistedException;
import com.github.holyloop.interceptor.ShiroSecured;
import com.github.holyloop.service.UserService;

/**
 * @author holyloop
 */
@ShiroSecured
@Path("/user")
public class UserController {

    @Inject
    private UserService userService;

    @RequiresPermissions("user:insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(UserDTO user) {
        if (user == null || StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return Response.status(Status.BAD_REQUEST).entity("username or password must not be null").build();
        }

        user.setUsername(StringUtils.trim(user.getUsername()));
        user.setPassword(StringUtils.trim(user.getPassword()));

        try {
            userService.addUser(user);
        } catch (UsernameExistedException e) {
            return Response.status(Status.BAD_REQUEST).entity("username existed").build();
        }

        return Response.status(Status.OK).build();
    }

}
