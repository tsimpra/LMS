package gr.apt.persistence.entity

import gr.apt.persistence.entity.superclass.AbstractEntity
import gr.apt.persistence.enumeration.Job
import gr.apt.persistence.enumeration.YesOrNo
import org.hibernate.annotations.Where
import java.math.BigInteger
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.*

@Entity
@Table(name = "person", schema = "lms")
open class Person : AbstractEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_seq")
    @SequenceGenerator(name = "person_seq", sequenceName = "lms.lms_person_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 0)
    open var id: BigInteger? = null

    @Basic
    @Column(name = "fname", nullable = true, length = 50)
    open var fname: @NotNull String? = null

    @Basic
    @Column(name = "lname", nullable = true, length = 60)
    open var lname: @NotNull String? = null

    @Basic
    @Column(name = "date_of_birth", nullable = true)
    open var dateOfBirth: @Past LocalDate? = null

    @Basic
    @Column(name = "email", nullable = true, length = 255)
    open var email: @Email String? = null

    @Basic
    @Column(name = "username", nullable = true, length = 255)
    open var username: @NotNull String? = null

    @Basic
    @Column(name = "password", nullable = true, length = 255)
    open var password: @NotNull String? = null

    @Basic
    @Column(name = "date_of_employment", nullable = true)
    open var dateOfEmployment: @PastOrPresent LocalDate? = null

    @Basic
    @Enumerated
    @Column(name = "job", nullable = true)
    open var job: Job? = null

    @Basic
    @Column(name = "number_of_leaves", nullable = true)
    open var numberOfLeaves: @Positive Int? = null

    @Basic
    @Enumerated
    @Column(name = "is_active", nullable = true)
    @Where(clause = "isActive = 1")
    open var isActive: YesOrNo? = null

//    @JsonBackReference
//    @OneToMany(mappedBy = "personByPersonId")
//    open val leavesById: Collection<Leave>? = mutableListOf()
//
//    @OneToMany(mappedBy = "personByPersonId")
//    open val personRolesById: Collection<PersonRoles>? = mutableListOf()
}