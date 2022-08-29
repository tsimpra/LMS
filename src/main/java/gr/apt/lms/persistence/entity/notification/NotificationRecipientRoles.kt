package gr.apt.lms.persistence.entity.notification

import gr.apt.lms.persistence.entity.superclass.AbstractEntity
import java.math.BigInteger
import javax.persistence.*

@Entity
@Table(name = "notification_recipient_roles", schema = "lms")
open class NotificationRecipientRoles(
    @field:Column(
        name = "role_id",
        nullable = false,
        precision = 0
    ) @field:Basic open var roleId: BigInteger?, @field:Column(
        name = "notif_id",
        nullable = false,
        precision = 0
    ) @field:Basic open var notifId: BigInteger?
) : AbstractEntity() {
    constructor() : this(null, null)

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_recipient_roles_seq")
    @SequenceGenerator(
        name = "notification_recipient_roles_seq",
        sequenceName = "lms.lms_notification_recipient_roles_id_seq",
        allocationSize = 1
    )
    @Column(name = "id", nullable = false, precision = 0)
    open var id: BigInteger? = null

//    @ManyToOne
//    @JoinColumn(name = "notif_id", referencedColumnName = "id", insertable = false, updatable = false)
//    open var notificationByNotifId: Notification? = null
}