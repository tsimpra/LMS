package gr.apt.lms.repository

import gr.apt.lms.persistence.entity.Person
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.inject.Singleton

@Singleton
class PersonRepository : PanacheRepositoryBase<Person?, BigInteger?> {
    fun getPersonBasicInfo(id: BigInteger?): Person? =
        find("id = ?1", id).singleResult()

}