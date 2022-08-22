package gr.apt.controller;

import gr.apt.config.security.AuthenticationDto;
import gr.apt.config.security.TokenGenerator;
import gr.apt.exception.LmsException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/auth")
@Produces("application/json")
public class AuthenticationController {

    @Inject
    TokenGenerator tokenGenerator;

    @POST
    @Path("/login")
    @Consumes("application/json")
    public Response login(AuthenticationDto auth) throws LmsException {
        String token = tokenGenerator.generateToken(auth.getUsername());
        if(token != null){
            return Response.ok(token).build();
        }
        throw new LmsException("Could not find user. Please verify your username and password.");
    }
}
