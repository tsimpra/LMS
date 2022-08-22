package gr.apt.persistence.entity.notification

import gr.apt.persistence.entity.superclass.AbstractEntity
import java.math.BigInteger
import javax.persistence.*

@Entity
@Table(name = "notification", schema = "lms")
open class Notification : AbstractEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq")
    @SequenceGenerator(name = "notification_seq", sequenceName = "lms.lms_notification_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 0)
    open var id: BigInteger? = null

    @Basic
    @Column(name = "content", nullable = false, length = 4000)
    open var content: String? = null

    @OneToMany(mappedBy = "notificationByNotifId", targetEntity = NotificationViewers::class)
    open var viewers: Set<NotificationViewers>? = null
}