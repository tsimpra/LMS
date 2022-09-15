package gr.apt.lms.repository.notification

import gr.apt.lms.persistence.entity.notification.NotificationViewers
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.inject.Singleton

@Singleton
class NotificationViewersRepository : PanacheRepositoryBase<NotificationViewers?, BigInteger?>