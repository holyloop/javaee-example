package com.github.holyloop.rest.config;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RestExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException arg0) {
        return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity(new ExceptionMessageWrapper(500, arg0.getMessage()))
                .build();
    }

}
