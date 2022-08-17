package gr.apt.controller;

import gr.apt.persistence.dto.RestHolidaysDto;
import gr.apt.service.RestHolidaysService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.List;

@Path("/rest-holidays")
@Produces("application/json")
public class RestHolidaysController {
    @Inject
    RestHolidaysService service;

    @GET
    @Path("/list")
    public Response findAll() {
        List list = service.findAll();
        if (list != null) {
            return Response.ok(list).build();
        }
        return Response.status(Response.Status.CONFLICT).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") BigInteger id) {
        RestHolidaysDto dto = service.findById(id);
        if (dto != null) {
            return Response.ok(dto).build();
        }
        return Response.status(Response.Status.CONFLICT).build();
    }

    @POST
    @Consumes("application/json")
    public Response create(RestHolidaysDto dto) {
        return service.create(dto) ? Response.ok(dto).build() : Response.status(Response.Status.CONFLICT).build();
    }

    @PUT
    @Consumes("application/json")
    public Response update(RestHolidaysDto dto) {
        return service.update(dto) ? Response.ok(dto).build() : Response.status(Response.Status.CONFLICT).build();
    }

    @DELETE
    @Consumes("application/json")
    public Response delete(RestHolidaysDto dto) {
        return service.delete(dto) ? Response.ok().build() : Response.status(Response.Status.CONFLICT).build();
    }
}
