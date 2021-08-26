package gr.apt.persistence.entity.superclass;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@RegisterForReflection
public class AbstractEntity extends PanacheEntityBase implements Serializable {

    @Basic
    @Column(name = "created")
    private LocalDateTime created;
    @Basic
    @Column(name = "created_by")
    private BigInteger createdBy;
    @Basic
    @Column(name = "updated")
    private LocalDateTime updated;
    @Basic
    @Column(name = "updated_by")
    private BigInteger updatedBy;

    @PrePersist
    private void prePersist() {
        if (created == null) {
            created = LocalDateTime.now();
            createdBy = BigInteger.ONE;
        }
        updated = LocalDateTime.now();
        updatedBy = BigInteger.ONE;
    }
}
