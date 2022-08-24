package gr.apt.dto.notification

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import gr.apt.persistence.entity.superclass.AbstractEntity
import java.math.BigInteger

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class NotificationDto(
    var id: BigInteger? = null,
    var content: String? = null
) : AbstractEntity()