package gr.apt.persistence.holiday;

import gr.apt.persistence.entity.superclass.AbstractEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rest_holidays", schema = "lms")
public class RestHolidays extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rest_holidays_seq")
    @SequenceGenerator(name = "rest_holidays_seq", sequenceName = "lms.lms_rest_holidays_id_seq")
    @Column(name = "id", nullable = false, precision = 0)
    private BigInteger id;
    @Basic
    @Column(name="description")
    private String description;
    @Basic
    @Column(name = "start_date", nullable = false)
    @FutureOrPresent
    private LocalDate startDate;
    @Basic
    @Column(name = "end_date", nullable = false)
    @FutureOrPresent
    private LocalDate endDate;
}
