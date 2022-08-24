package gr.apt.repository.notification

import gr.apt.persistence.entity.notification.Notification
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class NotificationRepository : PanacheRepositoryBase<Notification?, BigInteger?>