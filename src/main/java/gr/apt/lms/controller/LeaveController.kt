package gr.apt.lms.controller

import gr.apt.lms.dto.leave.ApproveLeaveDto
import gr.apt.lms.dto.leave.LeaveDto
import gr.apt.lms.exception.LmsException
import gr.apt.lms.service.LeaveService
import java.math.BigInteger
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/leave")
@Produces("application/json")
class LeaveController {
    @get:Inject
    lateinit var service: LeaveService

    @GET
    @Path("/list")
    @Throws(LmsException::class)
    fun findAll(@QueryParam("index") index: Int?, @QueryParam("size") size: Int?): Response {
        val list: List<*> = service.findAll(index, size)
        return Response.ok(list).build()
    }

    @GET
    @Path("/{personId}/list")
    @Throws(LmsException::class)
    fun findAllByPersonId(
        @PathParam("personId") personId: BigInteger?,
        @QueryParam("index") index: Int?,
        @QueryParam("size") size: Int?
    ): Response {
        val list: List<*> = service.findAllByPersonId(personId, index, size)
        return Response.ok(list).build()
    }

    @GET
    @Path("/list-pending")
    @Throws(LmsException::class)
    fun findAllPending(@QueryParam("index") index: Int?, @QueryParam("size") size: Int?): Response {
        val list: List<*> = service.findAllPending(index, size)
        return Response.ok(list).build()
    }

    @GET
    @Path("/list-approved")
    @Throws(LmsException::class)
    fun findAllApproved(@QueryParam("index") index: Int?, @QueryParam("size") size: Int?): Response {
        val list: List<*> = service.findAllApproved(index, size)
        return Response.ok(list).build()
    }

    @GET
    @Path("/{id}")
    @Throws(LmsException::class)
    fun findById(@PathParam("id") id: BigInteger): Response {
        val dto = service.findById(id)
        return Response.ok(dto).build()
    }

    @POST
    @Consumes("application/json")
    @Throws(LmsException::class)
    fun create(dto: LeaveDto): Response {
        return if (service.create(dto)) Response.ok(dto).build() else Response.status(Response.Status.CONFLICT)
            .build()
    }

    @PUT
    @Consumes("application/json")
    @Throws(LmsException::class)
    fun update(dto: LeaveDto): Response {
        return if (service.update(dto)) Response.ok(dto).build() else Response.status(Response.Status.CONFLICT)
            .build()
    }

    @DELETE
    @Consumes("application/json")
    @Throws(LmsException::class)
    fun delete(dto: LeaveDto): Response {
        return if (service.delete(dto)) Response.ok().build() else Response.status(Response.Status.CONFLICT).build()
    }

    @PUT
    @Path("/approve")
    @Consumes("application/json")
    @Throws(LmsException::class)
    fun approve(dto: ApproveLeaveDto): Response {
        return if (service.approve(dto)) Response.ok(dto).build() else Response.status(Response.Status.CONFLICT)
            .build()
    }
}