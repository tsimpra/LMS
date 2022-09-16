package gr.apt.lms.controller

import gr.apt.lms.dto.leave.ApproveLeaveDto
import gr.apt.lms.dto.leave.LeaveDto
import gr.apt.lms.exception.LmsException
import gr.apt.lms.service.LeaveService
import java.math.BigInteger
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/leave")
@Produces(MediaType.APPLICATION_JSON)
class LeaveController {
    @Inject
    lateinit var service: LeaveService

    @GET
    @Path("/list")
    @Throws(LmsException::class)
    fun findAll(@QueryParam("index") index: Int?, @QueryParam("size") size: Int?) =
        Response.ok(service.findAll(index, size)).build()


    @GET
    @Path("/{personId}/list")
    @Throws(LmsException::class)
    fun findAllByPersonId(
        @PathParam("personId") personId: BigInteger,
        @QueryParam("index") index: Int?,
        @QueryParam("size") size: Int?
    ) =
        Response.ok(service.findAllByPersonId(personId, index, size)).build()


    @GET
    @Path("/list-pending")
    @Throws(LmsException::class)
    fun findAllPending(@QueryParam("index") index: Int?, @QueryParam("size") size: Int?) =
        Response.ok(service.findAllPending(index, size)).build()


    @GET
    @Path("/list-approved")
    @Throws(LmsException::class)
    fun findAllApproved(@QueryParam("index") index: Int?, @QueryParam("size") size: Int?) =
        Response.ok(service.findAllApproved(index, size)).build()


    @GET
    @Path("/{id}")
    @Throws(LmsException::class)
    fun findById(@PathParam("id") id: BigInteger) = Response.ok(service.findById(id)).build()


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Throws(LmsException::class)
    fun create(dto: LeaveDto) =
        Response.ok(service.create(dto)).build()


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Throws(LmsException::class)
    fun update(dto: LeaveDto) = Response.ok(service.update(dto)).build()

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Throws(LmsException::class)
    fun delete(dto: LeaveDto) = Response.ok(service.delete(dto)).build()

    @PUT
    @Path("/approve")
    @Consumes(MediaType.APPLICATION_JSON)
    @Throws(LmsException::class)
    fun approve(dto: ApproveLeaveDto) = Response.ok(service.approve(dto)).build()
}