package gr.apt.dto.person

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import gr.apt.persistence.enumeration.Job
import java.math.BigInteger

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class PersonBasicInfoDto(
    var id: BigInteger? = null,
    var fname: String? = null,
    var lname: String? = null,
    var email: String? = null,
    var username: String? = null,
    var job: Job? = null
)