package gr.apt.lms.controller

import gr.apt.lms.dto.RestHolidaysDto
import gr.apt.lms.service.RestHolidaysService
import java.math.BigInteger
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/rest-holidays")
@Produces(MediaType.APPLICATION_JSON)
class RestHolidaysController {
    @get:Inject
    lateinit var service: RestHolidaysService

    @GET
    @Path("/list")
    fun findAll() = Response.ok(service.findAll()).build()


    @GET
    @Path("/{id}")
    fun findById(@PathParam("id") id: BigInteger) =
        Response.ok(service.findById(id)).build()


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun create(dto: RestHolidaysDto) = Response.ok(service.create(dto)).build()

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    fun update(dto: RestHolidaysDto) = Response.ok(service.update(dto)).build()

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    fun delete(dto: RestHolidaysDto) = Response.ok(service.delete(dto)).build()
}