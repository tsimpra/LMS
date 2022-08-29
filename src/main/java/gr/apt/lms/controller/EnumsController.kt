package gr.apt.lms.controller

import gr.apt.lms.exception.LmsException
import gr.apt.lms.persistence.enumeration.Job
import gr.apt.lms.persistence.enumeration.LeaveType
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

@Path("/enums")
@Produces("application/json")
class EnumsController {

    @get:Throws(LmsException::class)
    @get:Path("/job/list")
    @get:GET
    val jobList: Response
        get() = try {
            Response.ok(Job.valuesList).build()
        } catch (ex: Exception) {
            throw LmsException("An error occurred:" + ex.message)
        }

    @get:Throws(LmsException::class)
    @get:Path("/leave-types/list")
    @get:GET
    val leaveTypesList: Response
        get() = try {
            Response.ok(LeaveType.valuesList).build()
        } catch (ex: Exception) {
            throw LmsException("An error occurred:" + ex.message)
        }
}