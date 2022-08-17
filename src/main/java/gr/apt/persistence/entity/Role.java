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
import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles", schema = "lms",uniqueConstraints = {@UniqueConstraint(columnNames = "role")})
public class Role extends AbstractEntity {

    public static BigInteger ROLE_ADMIN = BigInteger.valueOf(-44L);
    public static BigInteger ROLE_USER = BigInteger.valueOf(-41L);


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "roles_seq")
    @SequenceGenerator(name = "roles_seq",sequenceName = "lms.lms_roles_id_seq")
    @Column(name = "id", nullable = false, precision = 0)
    private BigInteger id;
    @Basic
    @Column(name = "role", nullable = false, length = 255,unique = true)
    @NotNull
    private String role;

}
