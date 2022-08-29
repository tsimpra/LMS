package gr.apt.controller

import gr.apt.config.security.AuthenticationDto
import gr.apt.config.security.TokenGenerator
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

@Path("/auth")
@Produces("application/json")
class AuthenticationController {

    @get:Inject
    lateinit var tokenGenerator: TokenGenerator

    @POST
    @Path("/login")
    @Consumes("application/json")
    fun login(auth: AuthenticationDto): Response {
        val token = tokenGenerator.generateToken(auth.username)
        return Response.ok(token).build()
    }
}