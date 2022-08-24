package gr.apt.persistence.entity

import gr.apt.persistence.entity.superclass.AbstractEntity
import java.math.BigInteger
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "person_roles", schema = "lms")
open class PersonRoles : AbstractEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_roles_seq")
    @SequenceGenerator(name = "person_roles_seq", sequenceName = "lms.lms_person_roles_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 0)
    open var id: BigInteger? = null

    @Basic
    @Column(name = "role_id", nullable = false, precision = 0)
    open var roleId: @NotNull BigInteger? = null

    @Basic
    @Column(name = "person_id", nullable = false, precision = 0)
    open var personId: @NotNull BigInteger? = null

//    @ManyToOne
//    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
//    open var roleByRoleId: Role? = null

//    @JsonBackReference
//    @ManyToOne
//    @JoinColumn(
//        name = "person_id",
//        referencedColumnName = "id",
//        nullable = false,
//        insertable = false,
//        updatable = false
//    )
//    open var personByPersonId: Person? = null
}