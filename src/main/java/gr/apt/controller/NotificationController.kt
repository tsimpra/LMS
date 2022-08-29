package gr.apt.controller

import gr.apt.dto.notification.NotificationDto
import gr.apt.exception.LmsException
import gr.apt.service.notification.NotificationService
import org.eclipse.microprofile.jwt.JsonWebToken
import java.math.BigInteger
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/notification")
@Produces("application/json")
class NotificationController {
    @get:Inject
    lateinit var service: NotificationService

    @get:Inject
    lateinit var jwt: JsonWebToken

    @GET
    @Path("/list")
    @Throws(LmsException::class)
    fun findAll(@QueryParam("index") index: Int?, @QueryParam("size") size: Int?): Response {
        val list = service.findAll(jwt.getClaim("userId"), index, size)
        return Response.ok(list).build()
    }

    @POST
    @Path("/read-list")
    @Consumes("application/json")
    @Throws(LmsException::class)
    fun readAll(dtos: List<BigInteger>): Response {
        service.readAll(jwt.getClaim("userId"), dtos)
        return Response.ok().build()
    }

    @GET
    @Path("/read/{id}")
    @Throws(LmsException::class)
    fun read(@PathParam("id") notifId: BigInteger): Response {
        service.read(jwt.getClaim("userId"), notifId)
        return Response.ok().build()
    }

    @POST
    @Path("/create")
    @Consumes("application/json")
    @Throws(LmsException::class)
    fun create(dto: NotificationDto): Response {
        return if (service.createGeneralNotification(dto)) {
            Response.ok().build()
        } else Response.status(Response.Status.CONFLICT)
            .build()
    }
}