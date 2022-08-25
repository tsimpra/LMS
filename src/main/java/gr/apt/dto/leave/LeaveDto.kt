package gr.apt.dto.leave

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import gr.apt.dto.person.PersonBasicInfoDto
import gr.apt.persistence.entity.superclass.AbstractEntity
import gr.apt.persistence.enumeration.LeaveType
import gr.apt.persistence.enumeration.YesOrNo
import java.math.BigInteger
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class LeaveDto(
    var id: BigInteger? = null,
    var description: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var startDate: LocalDate? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var endDate: LocalDate? = null,
    var type: LeaveType? = null,
    var personId: BigInteger? = null
) : AbstractEntity() {
    var numberOfRequestedLeaves: Int? = null
    var approved: YesOrNo? = null
    var approvedDate: LocalDate? = null
    var approvedBy: PersonBasicInfoDto? = null
}