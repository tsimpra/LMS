package gr.apt.lms.persistence.entity

import gr.apt.lms.persistence.entity.superclass.AbstractEntity
import gr.apt.lms.persistence.enumeration.Job
import gr.apt.lms.persistence.enumeration.YesOrNo
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
    open var firstName: @NotNull String? = null

    @Basic
    @Column(name = "lname", nullable = true, length = 60)
    open var lastName: @NotNull String? = null

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
    @Column(name = "enabled", nullable = true)
    open var enabled: YesOrNo? = null

//    @JsonBackReference
//    @OneToMany(mappedBy = "personByPersonId")
//    open val leavesById: Collection<Leave>? = mutableListOf()
//
//    @OneToMany(mappedBy = "personByPersonId")
//    open val personRolesById: Collection<PersonRoles>? = mutableListOf()
}