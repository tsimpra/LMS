package gr.apt.lms.dto.leave

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import gr.apt.lms.persistence.enumeration.YesOrNo
import gr.apt.lms.utils.Dto
import java.math.BigInteger

@Dto
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class ApproveLeaveDto(
    var id: BigInteger,
    var approved: YesOrNo,
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
//    var approvedDate: LocalDate? = null,
//    var approvedBy: BigInteger? = null
)