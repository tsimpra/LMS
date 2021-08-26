package gr.apt.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class LmsExceptionHandler implements ExceptionMapper<LmsException> {
    @Override
    public Response toResponse(LmsException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
}
