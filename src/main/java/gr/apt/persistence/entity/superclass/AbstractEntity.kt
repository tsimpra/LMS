package gr.apt.persistence.entity.superclass

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import io.quarkus.runtime.annotations.RegisterForReflection
import java.io.Serializable
import java.math.BigInteger
import java.time.LocalDateTime
import javax.persistence.*

@MappedSuperclass
@RegisterForReflection
open class AbstractEntity : PanacheEntityBase(), Serializable {
    @Basic
    @Column(name = "created")
    open var created: LocalDateTime? = null

    @Basic
    @Column(name = "created_by")
    open var createdBy: BigInteger? = null

    @Basic
    @Column(name = "updated")
    open var updated: LocalDateTime? = null

    @Basic
    @Column(name = "updated_by")
    open var updatedBy: BigInteger? = null

    @PrePersist
    private fun prePersist() {
        if (created == null) {
            created = LocalDateTime.now()
            createdBy = BigInteger.ONE
        }
        updated = LocalDateTime.now()
        updatedBy = BigInteger.ONE
    }

    @PreUpdate
    private fun preUpdate(){
        updated = LocalDateTime.now()
        updatedBy = BigInteger.ONE
    }
}