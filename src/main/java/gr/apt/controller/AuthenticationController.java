package gr.apt.controller;

import gr.apt.exception.LmsException;
import gr.apt.security.AuthenticationDto;
import gr.apt.security.TokenGenerator;

import javax.inject.Inject;
import javax.ws.rs.*;
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
