package gr.apt.lms.repository

import gr.apt.lms.persistence.entity.Leave
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.inject.Singleton

@Singleton
class LeaveRepository : PanacheRepositoryBase<Leave?, BigInteger?> {

    fun getPersonLeaves(id: BigInteger) =
        find("from Leave e where e.personId = ?1", id).list<Leave>()
}