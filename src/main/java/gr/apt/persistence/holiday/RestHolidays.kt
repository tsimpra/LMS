package gr.apt.persistence.holiday

import gr.apt.persistence.entity.superclass.AbstractEntity
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import java.math.BigInteger
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.FutureOrPresent

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rest_holidays", schema = "lms")
open class RestHolidays : AbstractEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rest_holidays_seq")
    @SequenceGenerator(name = "rest_holidays_seq", sequenceName = "lms.lms_rest_holidays_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 0)
    open var id: BigInteger? = null

    @Basic
    @Column(name = "description")
    open var description: String? = null

    @Basic
    @Column(name = "start_date", nullable = false)
    open var startDate: @FutureOrPresent LocalDate? = null

    @Basic
    @Column(name = "end_date", nullable = false)
    open var endDate: @FutureOrPresent LocalDate? = null
}