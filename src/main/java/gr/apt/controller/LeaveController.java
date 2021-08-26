package gr.apt.controller;

import gr.apt.exception.LmsException;
import gr.apt.persistence.dto.ApproveLeaveDto;
import gr.apt.persistence.dto.LeaveDto;
import gr.apt.service.LeaveService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.List;

@Path("/leave")
@Produces("application/json")
public class LeaveController {
    @Inject
    LeaveService service;

    @GET
    @Path("/list")
    public Response findAll(@QueryParam("index") Integer index, @QueryParam("size") Integer size) throws LmsException{
        List list = service.findAll(index,size);
        if(list != null){
            return Response.ok(list).build();
        }
        return Response.status(Response.Status.CONFLICT).build();
    }

    @GET
    @Path("/{personId}/list")
    public Response findAllByPersonId(@PathParam("personId") BigInteger personId,@QueryParam("index") Integer index, @QueryParam("size") Integer size) throws LmsException{
        List list = service.findAllByPersonId(personId,index,size);
        if(list != null){
            return Response.ok(list).build();
        }
        return Response.status(Response.Status.CONFLICT).build();
    }

    @GET
    @Path("/list-pending")
    public Response findAllPending(@QueryParam("index") Integer index, @QueryParam("size") Integer size) throws LmsException{
        List list = service.findAllPending(index,size);
        if(list != null){
            return Response.ok(list).build();
        }
        return Response.status(Response.Status.CONFLICT).build();
    }

    @GET
    @Path("/list-approved")
    public Response findAllApproved(@QueryParam("index") Integer index, @QueryParam("size") Integer size) throws LmsException{
        List list = service.findAllApproved(index,size);
        if(list != null){
            return Response.ok(list).build();
        }
        return Response.status(Response.Status.CONFLICT).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id")BigInteger id) throws LmsException{
        LeaveDto dto = service.findById(id);
        if(dto != null){
            return Response.ok(dto).build();
        }
        throw new LmsException("Could not find Leave with id:"+id);
    }

    @POST
    @Consumes("application/json")
    public Response create(LeaveDto dto) throws LmsException{
        return service.create(dto) ? Response.ok(dto).build():Response.status(Response.Status.CONFLICT).build();
    }

    @PUT
    @Consumes("application/json")
    public Response update(LeaveDto dto) throws LmsException{
        return service.update(dto) ? Response.ok(dto).build():Response.status(Response.Status.CONFLICT).build();
    }

    @DELETE
    @Consumes("application/json")
    public Response delete(LeaveDto dto) throws LmsException{
        return service.delete(dto) ? Response.ok().build():Response.status(Response.Status.CONFLICT).build();
    }

    @PUT
    @Path("/approve")
    @Consumes("application/json")
    public Response approve(ApproveLeaveDto dto) throws LmsException {
        return service.approve(dto) ? Response.ok(dto).build():Response.status(Response.Status.CONFLICT).build();
    }
}
