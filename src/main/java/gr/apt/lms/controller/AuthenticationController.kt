package gr.apt.lms.controller

import gr.apt.lms.config.security.AuthenticationDto
import gr.apt.lms.config.security.TokenGenerator
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
class AuthenticationController {

    @get:Inject
    lateinit var tokenGenerator: TokenGenerator

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    fun login(auth: AuthenticationDto) = Response.ok(tokenGenerator.generateToken(auth.username)).build()

}