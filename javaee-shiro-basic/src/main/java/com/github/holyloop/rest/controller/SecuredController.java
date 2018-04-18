package com.github.holyloop.rest.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.shiro.authz.annotation.RequiresRoles;

import com.github.holyloop.interceptor.ShiroSecured;

/**
 * @author holyloop
 */
@ShiroSecured
@Path("/secured")
public class SecuredController {

    @RequiresRoles("admin")
    @GET
    @Path("/root")
    @Produces(MediaType.APPLICATION_JSON)
    public Response roleRootNeeded() {
        return Response.status(Status.OK).entity("hello, i'm secured").build();
    }

}
