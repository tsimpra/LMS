package gr.apt.repository.notification

import gr.apt.persistence.entity.notification.NotificationRecipientRoles
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class NotificationRecipientRolesRepository : PanacheRepositoryBase<NotificationRecipientRoles?, BigInteger?>