package gr.apt.lms.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import gr.apt.lms.persistence.entity.superclass.AbstractEntity
import gr.apt.lms.utils.Dto
import java.math.BigInteger

@Dto
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class PersonRolesDto(
    var id: BigInteger? = null,
    var roleId: BigInteger? = null,
    var personId: BigInteger? = null
) : AbstractEntity()