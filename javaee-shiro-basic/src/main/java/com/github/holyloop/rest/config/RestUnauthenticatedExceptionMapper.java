package com.github.holyloop.rest.config;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.shiro.authz.UnauthenticatedException;

/**
 * @author holyloop
 */
@Provider
public class RestUnauthenticatedExceptionMapper implements ExceptionMapper<UnauthenticatedException> {

    @Override
    public Response toResponse(UnauthenticatedException arg0) {
        return Response.status(Status.UNAUTHORIZED)
                .entity(new ExceptionMessageWrapper(401, "unauthorized"))
                .build();
    }

}
