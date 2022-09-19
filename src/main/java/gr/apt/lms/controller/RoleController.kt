package gr.apt.lms.controller

import gr.apt.lms.dto.RoleDto
import gr.apt.lms.service.RoleService
import java.math.BigInteger
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/role")
@Produces(MediaType.APPLICATION_JSON)
class RoleController {
    @Inject
    lateinit var service: RoleService

    @GET
    @Path("/list")
    fun findAll(@QueryParam("index") index: Int?, @QueryParam("size") size: Int?) =
        Response.ok(service.findAll(index, size)).build()


    @GET
    @Path("/{id}")
    fun findById(@PathParam("id") id: BigInteger) =
        Response.ok(service.findById(id)).build()


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun create(dto: RoleDto) = Response.ok(service.create(dto)).build()

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    fun update(dto: RoleDto) = Response.ok(service.update(dto)).build()

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    fun delete(dto: RoleDto) = Response.ok(service.delete(dto)).build()
}