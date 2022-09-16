package gr.apt.lms.controller

import gr.apt.lms.config.security.TokenGenerator.USERNAME_CLAIM
import gr.apt.lms.dto.notification.NotificationDto
import gr.apt.lms.exception.LmsException
import gr.apt.lms.service.notification.NotificationService
import org.eclipse.microprofile.jwt.JsonWebToken
import java.math.BigInteger
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/notification")
@Produces(MediaType.APPLICATION_JSON)
class NotificationController {
    @Inject
    lateinit var service: NotificationService

    @get:Inject
    lateinit var jwt: JsonWebToken

    @GET
    @Path("/list")
    @Throws(LmsException::class)
    fun findAll(@QueryParam("index") index: Int?, @QueryParam("size") size: Int?) =
        Response.ok(service.findAll(jwt.getClaim(USERNAME_CLAIM), index, size)).build()


    @POST
    @Path("/read-list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Throws(LmsException::class)
    fun readAll(dtos: List<BigInteger>) = Response.ok(service.readAll(jwt.getClaim(USERNAME_CLAIM), dtos)).build()


    @GET
    @Path("/read/{id}")
    @Throws(LmsException::class)
    fun read(@PathParam("id") notifId: BigInteger) =
        Response.ok(service.read(jwt.getClaim(USERNAME_CLAIM), notifId)).build()


    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Throws(LmsException::class)
    fun create(dto: NotificationDto) =
        Response.ok(service.createGeneralNotification(dto)).build()

}