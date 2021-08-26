package gr.apt.controller;

import gr.apt.exception.LmsException;
import gr.apt.persistence.dto.PersonDto;
import gr.apt.service.PersonService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.List;

@Path("/person")
@Produces("application/json")
public class PersonController {
    @Inject
    PersonService service;

    @GET
    @Path("/list")
    public Response findAll(@QueryParam("index") Integer index, @QueryParam("size") Integer size) throws LmsException {
        List list = service.findAll(index,size);
        if(list != null){
            return Response.ok(list).build();
        }
        return Response.status(Response.Status.CONFLICT).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") BigInteger id) throws LmsException {
        PersonDto dto = service.findById(id);
        if(dto != null){
            return Response.ok(dto).build();
        }
        throw new LmsException("Could not find Leave with id:"+id);
    }

    @POST
    @Consumes("application/json")
    public Response create(PersonDto dto) throws LmsException {
        return service.create(dto) ? Response.ok(dto).build():Response.status(Response.Status.CONFLICT).build();
    }

    @PUT
    @Consumes("application/json")
    public Response update(PersonDto dto) throws LmsException {
        return service.update(dto) ? Response.ok(dto).build():Response.status(Response.Status.CONFLICT).build();
    }

    @DELETE
    @Consumes("application/json")
    public Response delete(PersonDto dto) throws LmsException {
        return service.delete(dto) ? Response.ok().build():Response.status(Response.Status.CONFLICT).build();
    }

    @GET
    @Path("/refresh")
    public Response refresh() throws LmsException {
        return service.refreshDaysOfLeaves() ? Response.ok().build():Response.status(Response.Status.CONFLICT).build();
    }
}
