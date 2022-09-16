package gr.apt.lms.controller

import gr.apt.lms.dto.PersonRolesDto
import gr.apt.lms.service.PersonRolesService
import java.math.BigInteger
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/person-roles")
@Produces(MediaType.APPLICATION_JSON)
class PersonRolesController {
    @Inject
    lateinit var service: PersonRolesService

    @GET
    @Path("/list")
    fun findAll() = Response.ok(service.findAll()).build()


    @GET
    @Path("/{id}")
    fun findById(@PathParam("id") id: BigInteger) = Response.ok(service.findById(id)).build()


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun create(dto: PersonRolesDto) = Response.ok(service.create(dto)).build()

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    fun update(dto: PersonRolesDto) = Response.ok(service.update(dto)).build()

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    fun delete(dto: PersonRolesDto) = Response.ok(service.delete(dto)).build()
}