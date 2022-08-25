package gr.apt.controller;

import gr.apt.dto.notification.NotificationDto;
import gr.apt.exception.LmsException;
import gr.apt.service.notification.NotificationService;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.List;

@Path("/notification")
@Produces("application/json")
public class NotificationController {
    @Inject
    NotificationService service;
    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/list")
    public Response findAll(@QueryParam("index") Integer index, @QueryParam("size") Integer size) throws LmsException {
        List<NotificationDto> list = service.findAll(jwt.getClaim("userId"), index, size);
        return Response.ok(list).build();
    }

    @POST
    @Path("/read-list")
    @Consumes("application/json")
    public Response readAll(List<BigInteger> dtos) throws LmsException {
        service.readAll(jwt.getClaim("userId"), dtos);
        return Response.ok().build();
    }

    @GET
    @Path("/read/{id}")
    public Response read(@PathParam("id") BigInteger notifId) throws LmsException {
        service.read(jwt.getClaim("userId"), notifId);
        return Response.ok().build();
    }

    @POST
    @Path("/create")
    @Consumes("application/json")
    public Response create(NotificationDto dto) throws LmsException {
        if(service.createGeneralNotification(dto)){
            return Response.ok().build();
        }
        return Response.status(Response.Status.CONFLICT).build();
    }
}
