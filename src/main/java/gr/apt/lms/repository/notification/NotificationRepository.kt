package gr.apt.lms.repository.notification

import gr.apt.lms.metamodel.entity.NotificationRecipientPersons_
import gr.apt.lms.metamodel.entity.NotificationRecipientRoles_
import gr.apt.lms.metamodel.entity.NotificationViewers_
import gr.apt.lms.metamodel.entity.Notification_
import gr.apt.lms.persistence.entity.notification.Notification
import gr.apt.lms.utils.isNeitherNullNorEmpty
import io.quarkus.hibernate.orm.panache.PanacheQuery
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.inject.Singleton

@Singleton
class NotificationRepository : PanacheRepositoryBase<Notification?, BigInteger?> {

    fun getNotificationsByPersonIdAndRoleIds(
        roleIds: Set<BigInteger>,
        personId: BigInteger?
    ): PanacheQuery<Notification?> {
        val sb = StringBuilder()
        sb.append("from Notification notif where notif.${Notification_.ID} in (")
        if (roleIds.isNeitherNullNorEmpty()) {
            sb.append("select nrr.${NotificationRecipientRoles_.NOTIF_ID} from NotificationRecipientRoles nrr where nrr.${NotificationRecipientRoles_.ROLE_ID} = ?1")
        }
        if (personId != null) {
            sb.append(
                ") or notif.${Notification_.ID} in (select nrp.${NotificationRecipientPersons_.NOTIF_ID} from NotificationRecipientPersons nrp where nrp.${NotificationRecipientPersons_.PERSON_ID} = ?2) " +
                        "and notif.${Notification_.ID} not in (select nov.${NotificationViewers_.NOTIF_ID} from NotificationViewers nov where nov.${NotificationViewers_.PERSON_ID} = ?2"
            )
        }

        sb.append(") ")
        return find(sb.toString(), roleIds, personId)
    }
}