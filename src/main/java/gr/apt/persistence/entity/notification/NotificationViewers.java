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
@Table(name = "notification_viewers", schema = "lms")
public class NotificationViewers extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "notification_viewers_seq")
    @SequenceGenerator(name = "notification_viewers_seq",sequenceName = "lms.lms_notification_viewers_id_seq")
    @Column(name = "id", nullable = false, precision = 0)
    private BigInteger id;
    @Basic
    @Column(name = "person_id", nullable = false, precision = 0)
    private BigInteger personId;
    @Basic
    @Column(name = "notif_id", nullable = false, precision = 0)
    private BigInteger notifId;

    @ManyToOne
    @JoinColumn(name = "notif_id", referencedColumnName = "id", insertable = false,updatable = false)
    private Notification notificationByNotifId;

    public NotificationViewers(BigInteger personId, BigInteger notifId){
        this.personId = personId;
        this.notifId = notifId;
    }

}
