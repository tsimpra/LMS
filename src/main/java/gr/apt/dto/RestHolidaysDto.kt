package gr.apt.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import gr.apt.persistence.entity.superclass.AbstractEntity
import java.math.BigInteger
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class RestHolidaysDto(
    var id: BigInteger? = null,
    var description: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var startDate: LocalDate? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var endDate: LocalDate? = null
) : AbstractEntity()