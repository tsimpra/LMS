package gr.apt.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import gr.apt.persistence.entity.superclass.AbstractEntity
import java.math.BigInteger

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class PersonRolesDto(
    var id: BigInteger? = null,
    var roleId: BigInteger? = null,
    var personId: BigInteger? = null
) : AbstractEntity()