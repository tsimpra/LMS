package gr.apt.persistence.entity.notification;

import gr.apt.persistence.entity.superclass.AbstractEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "notification_recipient_roles", schema = "lms")
public class NotificationRecipientRoles extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "notification_recipient_roles_seq")
    @SequenceGenerator(name = "notification_recipient_roles_seq",sequenceName = "lms.lms_notification_recipient_roles_id_seq")
    @Column(name = "id", nullable = false, precision = 0)
    private BigInteger id;
    @Basic
    @Column(name = "role_id", nullable = false, precision = 0)
    private BigInteger roleId;
    @Basic
    @Column(name = "notif_id", nullable = false, precision = 0)
    private BigInteger notifId;

    @ManyToOne
    @JoinColumn(name = "notif_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Notification notificationByNotifId;

    public NotificationRecipientRoles(BigInteger roleId, BigInteger notifId) {
        this.roleId = roleId;
        this.notifId = notifId;
    }
}
