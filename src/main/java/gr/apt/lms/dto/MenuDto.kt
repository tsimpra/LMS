package gr.apt.lms.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import gr.apt.lms.persistence.entity.superclass.AbstractEntity
import gr.apt.lms.utils.Dto
import java.math.BigInteger

@Dto
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class MenuDto(
    var id: BigInteger? = null,
    var description: String? = null,
    var path: String? = null,
    var icon: String? = null,
    var parentId: BigInteger? = null
) : AbstractEntity()

