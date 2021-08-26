package gr.apt.controller;

import gr.apt.exception.LmsException;
import gr.apt.persistence.enumeration.Job;
import gr.apt.persistence.enumeration.LeaveType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/enums")
@Produces("application/json")
public class EnumsController {

    @GET
    @Path("/job/list")
    public Response getJobList() throws LmsException {
        try{
            return Response.ok(Job.getValuesList()).build();
        }catch (Exception ex){
            throw new LmsException("An error occurred:" + ex.getMessage());
        }
    }

    @GET
    @Path("/leave-types/list")
    public Response getLeaveTypesList() throws LmsException {
        try{
            return Response.ok(LeaveType.getValuesList()).build();
        }catch (Exception ex){
            throw new LmsException("An error occurred:" + ex.getMessage());
        }
    }
}
