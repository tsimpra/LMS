package gr.apt.lms.controller

import gr.apt.lms.dto.person.PersonDto
import gr.apt.lms.exception.LmsException
import gr.apt.lms.service.PersonService
import java.math.BigInteger
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/person")
@Produces(MediaType.APPLICATION_JSON)
class PersonController {

    @get:Inject
    lateinit var service: PersonService

    @GET
    @Path("/list")
    @Throws(LmsException::class)
    fun findAll(@QueryParam("index") index: Int?, @QueryParam("size") size: Int?) =
        Response.ok(service.findAll(index, size)).build()


    @GET
    @Path("/{id}")
    @Throws(LmsException::class)
    fun findById(@PathParam("id") id: BigInteger) = Response.ok(service.findById(id)).build()


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Throws(LmsException::class)
    fun create(dto: PersonDto) = Response.ok(service.create(dto)).build()

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Throws(LmsException::class)
    fun update(dto: PersonDto) = Response.ok(service.update(dto)).build()

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Throws(LmsException::class)
    fun delete(dto: PersonDto) = Response.ok(service.delete(dto)).build()

    //TODO: replace this endpoint with a cronjob if possible
    @GET
    @Path("/refresh")
    @Throws(LmsException::class)
    fun refresh() = Response.ok(service.refreshDaysOfLeaves()).build()
}