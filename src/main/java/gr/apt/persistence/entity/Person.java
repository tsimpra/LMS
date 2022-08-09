package gr.apt.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import gr.apt.persistence.entity.superclass.AbstractEntity;
import gr.apt.persistence.enumeration.Job;
import gr.apt.persistence.enumeration.LeaveType;
import gr.apt.persistence.enumeration.YesOrNo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "person", schema = "lms")
public class Person extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_seq")
    @SequenceGenerator(name = "person_seq", sequenceName = "lms.lms_person_id_seq")
    @Column(name = "id", nullable = false, precision = 0)
    private BigInteger id;
    @Basic
    @Column(name = "fname", nullable = true, length = 50)
    @NotNull
    private String fname;
    @Basic
    @Column(name = "lname", nullable = true, length = 60)
    @NotNull
    private String lname;
    @Basic
    @Column(name = "date_of_birth", nullable = true)
    @Past
    private LocalDate dateOfBirth;
    @Basic
    @Column(name = "email", nullable = true, length = 255)
    @Email
    private String email;
    @Basic
    @Column(name = "username", nullable = true, length = 255)
    @NotNull
    private String username;
    @Basic
    @Column(name = "password", nullable = true, length = 255)
    @NotNull
    private String password;
    @Basic
    @Column(name = "date_of_employment", nullable = true)
    @PastOrPresent
    private LocalDate dateOfEmployment;
    @Basic
    @Enumerated
    @Column(name = "job", nullable = true)
    private Job job;
    @Basic
    @Column(name = "number_of_leaves", nullable = true)
    @Positive
    private Integer numberOfLeaves;

    @Basic
    @Enumerated
    @Column(name = "is_active", nullable = true)
    private YesOrNo isActive;

    @JsonBackReference
    @OneToMany(mappedBy = "personByPersonId")
    private Collection<Leave> leavesById;

    @OneToMany(mappedBy = "personByPersonId")
    private Collection<PersonRoles> personRolesById;

    public Integer getRemainingLeaves() {
        return this.numberOfLeaves - this.getUsedLeaves();
    }

    public Integer getUsedLeaves() {
        Integer used = 0;
        if (!leavesById.isEmpty()) {
            for (Leave leave : leavesById) {
                if(leave.getApproved() != null && leave.getApproved().equals(YesOrNo.YES)) {
                    if (leave.getType().equals(LeaveType.PAID_LEAVE)) {
                        used = used + leave.getNumberOfRequestedLeaves();
                    }
                }
            }
        }
        return used;
    }

}
