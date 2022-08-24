package gr.apt.exception

import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class LmsExceptionHandler : ExceptionMapper<LmsException> {
    override fun toResponse(e: LmsException): Response {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.message).build()
    }
}