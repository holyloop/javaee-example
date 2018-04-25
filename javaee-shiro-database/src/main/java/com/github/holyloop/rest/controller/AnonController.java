package com.github.holyloop.rest.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * @author holyloop
 */
@Path("/anon")
public class AnonController {

    @GET
    @Path("/greet")
    @Produces(MediaType.APPLICATION_JSON)
    public Response anyoneCanAccess() {
        return Response.status(Status.OK).entity("hello").build();
    }
    
}
