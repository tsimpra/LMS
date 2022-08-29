package gr.apt.lms.repository.notification

import gr.apt.lms.persistence.entity.notification.Notification
import gr.apt.lms.utils.isNeitherNullNorEmpty
import io.quarkus.hibernate.orm.panache.PanacheQuery
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class NotificationRepository : PanacheRepositoryBase<Notification?, BigInteger?> {

    fun getNotificationsByPersonIdAndRoleIds(
        roleIds: Set<BigInteger>,
        personId: BigInteger?
    ): PanacheQuery<Notification?> {
        val sb = StringBuilder()
        sb.append("select * from notification not where not.id in (")
        if (roleIds.isNeitherNullNorEmpty()) {
            sb.append("select nrr.notifId from notification_recipient_roles nrr where nrr.roleId = ?1")
        }
        if (personId != null) {
            sb.append(") or not.id in (select nrp.notifId from notification_recipient_persons nrp where nrp.personId = ?2) and not.id not in (select nov.notifId from notification_viewers nov where nov.personId = ?2")
        }

        sb.append(") ")
        return find(sb.toString(), roleIds, personId)
    }
}