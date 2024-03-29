package gr.apt.lms.dto.leave

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import gr.apt.lms.dto.person.PersonBasicInfoDto
import gr.apt.lms.persistence.entity.superclass.AbstractEntity
import gr.apt.lms.persistence.enumeration.LeaveType
import gr.apt.lms.persistence.enumeration.YesOrNo
import gr.apt.lms.utils.Dto
import java.math.BigInteger
import java.time.LocalDate

@Dto
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