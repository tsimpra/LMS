package gr.apt.lms.persistence.entity

import gr.apt.lms.persistence.entity.superclass.AbstractEntity
import java.math.BigInteger
import javax.persistence.*

@Entity
@Table(name = "menu", schema = "lms")
class Menu : AbstractEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_seq")
    @SequenceGenerator(name = "menu_seq", sequenceName = "lms.lms_menu_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 0)
    var id: BigInteger? = null

    @Basic
    @Column(name = "description", nullable = true, length = 500)
    var description: String? = null

    @Basic
    @Column(name = "path", nullable = true, length = 500)
    var path: String? = null

    @Basic
    @Column(name = "icon", nullable = true, length = 500)
    var icon: String? = null

    @Basic
    @Column(name = "parent_id", nullable = true)
    var parentId: BigInteger? = null
}