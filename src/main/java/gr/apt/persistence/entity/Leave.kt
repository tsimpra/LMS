package gr.apt.persistence.entity

import gr.apt.persistence.entity.superclass.AbstractEntity
import gr.apt.persistence.enumeration.LeaveType
import gr.apt.persistence.enumeration.YesOrNo
import java.math.BigInteger
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.FutureOrPresent
import javax.validation.constraints.NotNull


@Entity
@Table(name = "leave", schema = "lms")
open class Leave : AbstractEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leave_seq")
    @SequenceGenerator(name = "leave_seq", sequenceName = "lms.lms_leave_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 0)
    open var id: BigInteger? = null

    @Basic
    @Column(name = "description", nullable = true, length = 500)
    open var description: String? = null

    @Basic
    @Column(name = "start_date", nullable = false)
    open var startDate: @FutureOrPresent LocalDate? = null

    @Basic
    @Column(name = "end_date", nullable = false)
    open var endDate: @FutureOrPresent LocalDate? = null

    @Basic
    @Enumerated
    @Column(name = "type", nullable = true)
    open var type: LeaveType? = null

    @Basic
    @Enumerated
    @Column(name = "approved", nullable = true)
    open var approved: YesOrNo? = null

    @Basic
    @Column(name = "approved_by", nullable = true, precision = 0)
    open var approvedBy: BigInteger? = null

    @Basic
    @Column(name = "approved_date", nullable = true)
    open var approvedDate: @FutureOrPresent LocalDate? = null

    @Basic
    @Column(name = "person_id", nullable = false, precision = 0)
    open var personId: @NotNull BigInteger? = null

    @ManyToOne
    @JoinColumn(name = "approved_by", referencedColumnName = "id", updatable = false, insertable = false)
    open var personByApprovedBy: Person? = null

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id", updatable = false, insertable = false)
    open var personByPersonId: Person? = null
}