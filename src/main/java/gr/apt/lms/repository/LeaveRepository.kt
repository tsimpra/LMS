package gr.apt.lms.repository

import gr.apt.lms.persistence.entity.Leave
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class LeaveRepository : PanacheRepositoryBase<Leave?, BigInteger?> {

    fun getPersonLeaves(id: BigInteger) =
        list("select * from leaves where personId = ?1", id)
}