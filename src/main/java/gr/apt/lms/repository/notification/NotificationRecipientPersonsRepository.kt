package gr.apt.lms.repository.notification

import gr.apt.lms.persistence.entity.notification.NotificationRecipientPersons
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.inject.Singleton

@Singleton
class NotificationRecipientPersonsRepository : PanacheRepositoryBase<NotificationRecipientPersons?, BigInteger?>