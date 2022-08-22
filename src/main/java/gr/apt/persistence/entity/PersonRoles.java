package gr.apt.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import gr.apt.persistence.entity.superclass.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "person_roles", schema = "lms")
public class PersonRoles extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_roles_seq")
    @SequenceGenerator(name = "person_roles_seq", sequenceName = "lms.lms_person_roles_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 0)
    private BigInteger id;
    @Basic
    @Column(name = "role_id", nullable = false, precision = 0)
    @NotNull
    private BigInteger roleId;
    @Basic
    @Column(name = "person_id", nullable = false, precision = 0)
    @NotNull
    private BigInteger personId;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Role roleByRoleId;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Person personByPersonId;

}
