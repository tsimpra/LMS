package gr.apt.repository.notification;

import gr.apt.persistence.entity.notification.NotificationViewers;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigInteger;
@ApplicationScoped
public class NotificationViewersRepository implements PanacheRepositoryBase<NotificationViewers, BigInteger> {
}
