package gr.apt.lms.persistence.entity

import gr.apt.lms.persistence.entity.superclass.AbstractEntity
import java.math.BigInteger
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "roles", schema = "lms", uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("role"))])
open class Role() : AbstractEntity() {

    constructor(id: BigInteger, role: String) : this() {
        this.id = id
        this.role = role
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_seq")
    @SequenceGenerator(name = "roles_seq", sequenceName = "lms.lms_roles_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 0)
    open var id: BigInteger? = null

    @Basic
    @Column(name = "role", nullable = false, length = 255, unique = true)
    open var role: @NotNull String? = null

    companion object {
        @JvmField
        val ROLE_ADMIN = BigInteger.valueOf(-44L)
        val ROLE_USER = BigInteger.valueOf(-41L)
    }
}