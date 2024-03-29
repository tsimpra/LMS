package gr.apt.lms.repository

import gr.apt.lms.metamodel.entity.Leave_
import gr.apt.lms.persistence.entity.Leave
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.inject.Singleton

@Singleton
class LeaveRepository : PanacheRepositoryBase<Leave?, BigInteger?> {

    fun getPersonLeaves(id: BigInteger) =
        find("from Leave e where e.${Leave_.PERSON_ID} = ?1", id).list<Leave>()
}