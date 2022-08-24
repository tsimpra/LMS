package gr.apt.dto.notification

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigInteger

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class CreateNotificationDto(
    var content: String? = null,
    var recipientPersonId: BigInteger? = null,
    var recipientRoleIds: Set<BigInteger>? = null
)