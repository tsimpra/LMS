package gr.apt.repository.notification;

import gr.apt.persistence.entity.notification.NotificationRecipientPersons;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigInteger;
@ApplicationScoped
public class NotificationRecipientPersonsRepository implements PanacheRepositoryBase<NotificationRecipientPersons, BigInteger> {
}
