package gr.apt.lms.controller

import gr.apt.lms.dto.person.PersonDto
import gr.apt.lms.exception.LmsException
import gr.apt.lms.service.PersonService
import java.math.BigInteger
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/person")
@Produces("application/json")
class PersonController {

    @get:Inject
    lateinit var service: PersonService

    @GET
    @Path("/list")
    @Throws(LmsException::class)
    fun findAll(@QueryParam("index") index: Int?, @QueryParam("size") size: Int?): Response {
        val list: List<*> = service.findAll(index, size)
        return Response.ok(list).build()
    }

    @GET
    @Path("/{id}")
    @Throws(LmsException::class)
    fun findById(@PathParam("id") id: BigInteger): Response {
        val dto = service.findById(id)
        return Response.ok(dto).build()
        throw LmsException("Could not find Leave with id:$id")
    }

    @POST
    @Consumes("application/json")
    @Throws(LmsException::class)
    fun create(dto: PersonDto): Response {
        return if (service.create(dto)) Response.ok(dto).build() else Response.status(Response.Status.CONFLICT)
            .build()
    }

    @PUT
    @Consumes("application/json")
    @Throws(LmsException::class)
    fun update(dto: PersonDto): Response {
        return if (service.update(dto)) Response.ok(dto).build() else Response.status(Response.Status.CONFLICT)
            .build()
    }

    @DELETE
    @Consumes("application/json")
    @Throws(LmsException::class)
    fun delete(dto: PersonDto): Response {
        return if (service.delete(dto)) Response.ok().build() else Response.status(Response.Status.CONFLICT).build()
    }

    //TODO: replacer this endpoint with a cronjob if possible
    @GET
    @Path("/refresh")
    @Throws(LmsException::class)
    fun refresh(): Response {
        return if (service.refreshDaysOfLeaves()) Response.ok().build() else Response.status(Response.Status.CONFLICT)
            .build()
    }
}