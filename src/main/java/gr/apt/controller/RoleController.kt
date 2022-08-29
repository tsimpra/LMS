package gr.apt.controller

import gr.apt.dto.RoleDto
import gr.apt.service.RoleService
import java.math.BigInteger
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/role")
@Produces("application/json")
class RoleController {
    @get:Inject
    lateinit var service: RoleService

    @GET
    @Path("/list")
    fun findAll(): Response {
        val list: List<*> = service.findAll()
        return Response.ok(list).build()
    }

    @GET
    @Path("/{id}")
    fun findById(@PathParam("id") id: BigInteger): Response {
        val dto = service.findById(id)
        return Response.ok(dto).build()
    }

    @POST
    @Consumes("application/json")
    fun create(dto: RoleDto): Response {
        return if (service.create(dto)) Response.ok(dto).build() else Response.status(Response.Status.CONFLICT)
            .build()
    }

    @PUT
    @Consumes("application/json")
    fun update(dto: RoleDto): Response {
        return if (service.update(dto)) Response.ok(dto).build() else Response.status(Response.Status.CONFLICT)
            .build()
    }

    @DELETE
    @Consumes("application/json")
    fun delete(dto: RoleDto): Response {
        return if (service.delete(dto)) Response.ok().build() else Response.status(Response.Status.CONFLICT).build()
    }
}