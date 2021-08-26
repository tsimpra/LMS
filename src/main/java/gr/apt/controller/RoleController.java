package gr.apt.controller;

import gr.apt.persistence.dto.RoleDto;
import gr.apt.service.RoleService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.List;

@Path("/role")
@Produces("application/json")
public class RoleController {
    @Inject
    RoleService service;

    @GET
    @Path("/list")
    public Response findAll(){
        List list = service.findAll();
        if(list != null){
            return Response.ok(list).build();
        }
        return Response.status(Response.Status.CONFLICT).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") BigInteger id){
        RoleDto dto = service.findById(id);
        if(dto != null){
            return Response.ok(dto).build();
        }
        return Response.status(Response.Status.CONFLICT).build();
    }

    @POST
    @Consumes("application/json")
    public Response create(RoleDto dto){
        return service.create(dto) ? Response.ok(dto).build():Response.status(Response.Status.CONFLICT).build();
    }

    @PUT
    @Consumes("application/json")
    public Response update(RoleDto dto){
        return service.update(dto) ? Response.ok(dto).build():Response.status(Response.Status.CONFLICT).build();
    }

    @DELETE
    @Consumes("application/json")
    public Response delete(RoleDto dto){
        return service.delete(dto) ? Response.ok().build():Response.status(Response.Status.CONFLICT).build();
    }
}
