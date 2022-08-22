package gr.apt.persistence.entity.notification;

import gr.apt.persistence.entity.superclass.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "notification", schema = "lms")
public class Notification extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq")
    @SequenceGenerator(name = "notification_seq", sequenceName = "lms.lms_notification_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 0)
    private BigInteger id;
    @Basic
    @Column(name = "content", nullable = false, length = 4000)
    private String content;

//    @OneToMany(mappedBy ="notificationByNotifId")
//    private Set<NotificationRecipientPersons> recipientPersons;
//
//    @OneToMany(mappedBy = "notificationByNotifId")
//    private Set<NotificationRecipientRoles> recipientRoles;

    @OneToMany(mappedBy = "notificationByNotifId")
    private Set<NotificationViewers> viewers;


}
