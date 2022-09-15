package gr.apt.lms.dto.person

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import gr.apt.lms.dto.RoleDto
import gr.apt.lms.persistence.entity.superclass.AbstractEntity
import gr.apt.lms.persistence.enumeration.Job
import gr.apt.lms.persistence.enumeration.YesOrNo
import gr.apt.lms.utils.Dto
import java.math.BigInteger
import java.time.LocalDate

@Dto
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class PersonDto(
    var id: BigInteger? = null,
    var fname: String? = null,
    var lname: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var dateOfBirth: LocalDate = LocalDate.now(),
    var email: String? = null,
    var username: String? = null,
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var password: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var dateOfEmployment: LocalDate = LocalDate.now(),
    var job: Job? = null,
    var numberOfLeaves: Int? = null,
    var usedLeaves: Int? = null,
    var remainingLeaves: Int? = null,
    var enabled: YesOrNo? = null,
    var roles: Collection<RoleDto>? = null
) : AbstractEntity() {

}