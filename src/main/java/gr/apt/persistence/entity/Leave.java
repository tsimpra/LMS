package gr.apt.persistence.entity;

import gr.apt.persistence.holiday.MobileHolidays;
import gr.apt.persistence.holiday.PublicHolidays;
import gr.apt.persistence.entity.superclass.AbstractEntity;
import gr.apt.persistence.enumeration.LeaveType;
import gr.apt.persistence.enumeration.YesOrNo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "leave",schema = "lms")
public class Leave extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "leave_seq")
    @SequenceGenerator(name = "leave_seq",sequenceName = "lms.lms_leave_id_seq")
    @Column(name = "id", nullable = false, precision = 0)
    private BigInteger id;
    @Basic
    @Column(name = "description", nullable = true, length = 500)
    private String description;
    @Basic
    @Column(name = "start_date", nullable = false)
    @FutureOrPresent
    private LocalDate startDate;
    @Basic
    @Column(name = "end_date", nullable = false)
    @FutureOrPresent
    private LocalDate endDate;
    @Basic
    @Enumerated
    @Column(name = "type", nullable = true)
    private LeaveType type;
    @Basic
    @Enumerated
    @Column(name = "approved", nullable = true)
    private YesOrNo approved;
    @Basic
    @Column(name = "approved_by", nullable = true, precision = 0)
    private BigInteger approvedBy;
    @Basic
    @Column(name = "approved_date", nullable = true)
    @FutureOrPresent
    private LocalDate approvedDate;
    @Basic
    @Column(name = "person_id", nullable = false, precision = 0)
    @NotNull
    private BigInteger personId;


    @ManyToOne
    @JoinColumn(name = "approved_by", referencedColumnName = "id",updatable = false,insertable = false)
    private Person personByApprovedBy;
    @ManyToOne()
    @JoinColumn(name = "person_id", referencedColumnName = "id",updatable = false,insertable = false)
    private Person personByPersonId;

    @Transient
    private Integer numberOfRequestedLeaves;

    public Integer getNumberOfRequestedLeaves() {
        int daysCount = this.getEndDate().compareTo(this.getStartDate()) + 1;
        int weekendsCount =0;
        int publicHolidays =0;
        for(int i=0;i<daysCount;i++){
            DayOfWeek day = this.getStartDate().plusDays(i).getDayOfWeek();
            if(day.equals(DayOfWeek.SATURDAY) || day.equals(DayOfWeek.SUNDAY)){
                weekendsCount++;
                continue;
            }
            if(PublicHolidays.getPublicHolidays().contains(this.getStartDate().plusDays(i)) ||
                    MobileHolidays.getMobileHolidays().contains(this.getStartDate().plusDays(i))){
                publicHolidays++;
            }
        }
        return daysCount - weekendsCount - publicHolidays;
    }
}
