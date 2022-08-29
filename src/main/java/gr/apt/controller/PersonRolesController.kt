package gr.apt.controller

import gr.apt.dto.PersonRolesDto
import gr.apt.service.PersonRolesService
import java.math.BigInteger
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/person-roles")
@Produces("application/json")
class PersonRolesController {
    @get:Inject
    lateinit var service: PersonRolesService

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
    fun create(dto: PersonRolesDto?): Response {
        return if (service.create(dto)) Response.ok(dto).build() else Response.status(Response.Status.CONFLICT)
            .build()
    }

    @PUT
    @Consumes("application/json")
    fun update(dto: PersonRolesDto): Response {
        return if (service.update(dto)) Response.ok(dto).build() else Response.status(Response.Status.CONFLICT)
            .build()
    }

    @DELETE
    @Consumes("application/json")
    fun delete(dto: PersonRolesDto): Response {
        return if (service.delete(dto)) Response.ok().build() else Response.status(Response.Status.CONFLICT).build()
    }
}