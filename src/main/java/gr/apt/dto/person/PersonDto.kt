package gr.apt.dto.person

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import gr.apt.dto.RoleDto
import gr.apt.persistence.entity.superclass.AbstractEntity
import gr.apt.persistence.enumeration.Job
import gr.apt.persistence.enumeration.YesOrNo
import java.math.BigInteger
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class PersonDto(
    var id: BigInteger? = null,
    var fname: String? = null,
    var lname: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var dateOfBirth: LocalDate? = null,
    var email: String? = null,
    var username: String? = null,
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var password: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var dateOfEmployment: LocalDate? = null,
    var job: Job? = null,
    var numberOfLeaves: Int? = null,
    var usedLeaves: Int? = null,
    var remainingLeaves: Int? = null,
    var isActive: YesOrNo? = null,
    var roles: Collection<RoleDto>? = null
) : AbstractEntity() {

}