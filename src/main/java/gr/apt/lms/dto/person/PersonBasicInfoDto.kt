package gr.apt.lms.dto.person

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import gr.apt.lms.persistence.enumeration.Job
import gr.apt.lms.utils.Dto
import java.math.BigInteger

@Dto
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class PersonBasicInfoDto(
    var id: BigInteger? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var username: String? = null,
    var job: Job? = null
)
val PersonBasicInfoDto.fullname
    get() = this.lastName + " " + this.firstName