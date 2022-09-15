package gr.apt.lms.repository.notification

import gr.apt.lms.persistence.entity.notification.NotificationRecipientRoles
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.inject.Singleton

@Singleton
class NotificationRecipientRolesRepository : PanacheRepositoryBase<NotificationRecipientRoles?, BigInteger?>