package gr.apt.repository.notification;

import gr.apt.persistence.entity.notification.Notification;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigInteger;

@ApplicationScoped
public class NotificationRepository implements PanacheRepositoryBase<Notification, BigInteger> {
}
