package gr.apt.dto.leave

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import gr.apt.dto.person.PersonBasicInfoDto
import gr.apt.persistence.enumeration.YesOrNo
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class ApproveLeaveDto(
    var approved: YesOrNo? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var approvedDate: LocalDate? = null,
    var approvedBy: PersonBasicInfoDto? = null
) : LeaveDto()