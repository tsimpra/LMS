package gr.apt.persistence.entity.notification

import gr.apt.persistence.entity.superclass.AbstractEntity
import java.math.BigInteger
import javax.persistence.*

@Entity
@Table(name = "notification_recipient_persons", schema = "lms")
open class NotificationRecipientPersons(
    @Basic
    @Column(name = "person_id", nullable = false, precision = 0)
    open var personId: BigInteger?,
    @Basic
    @Column(name = "notif_id", nullable = false, precision = 0)
    open var notifId: BigInteger?
) : AbstractEntity() {
    constructor() : this(null, null)

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_recipient_persons_seq")
    @SequenceGenerator(
        name = "notification_recipient_persons_seq",
        sequenceName = "lms.lms_notification_recipient_persons_id_seq",
        allocationSize = 1
    )
    @Column(name = "id", nullable = false, precision = 0)
    open var id: BigInteger? = null

    @ManyToOne
    @JoinColumn(name = "notif_id", referencedColumnName = "id", insertable = false, updatable = false)
    open var notificationByNotifId: Notification? = null
}