package gr.apt.lms.repository.notification

import gr.apt.lms.persistence.entity.notification.NotificationRecipientPersons
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class NotificationRecipientPersonsRepository : PanacheRepositoryBase<NotificationRecipientPersons?, BigInteger?>