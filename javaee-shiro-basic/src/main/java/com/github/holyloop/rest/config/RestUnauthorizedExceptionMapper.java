package com.github.holyloop.rest.config;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.shiro.authz.AuthorizationException;

/**
 * @author holyloop
 */
@Provider
public class RestUnauthorizedExceptionMapper implements ExceptionMapper<AuthorizationException> {

    @Override
    public Response toResponse(AuthorizationException arg0) {
        return Response.status(Status.FORBIDDEN)
                .entity(new ExceptionMessageWrapper(403, "forbidden"))
                .build();
    }

}
