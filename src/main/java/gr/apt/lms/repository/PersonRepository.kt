package gr.apt.lms.repository

import gr.apt.lms.metamodel.entity.Person_
import gr.apt.lms.persistence.entity.Person
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.inject.Singleton

@Singleton
class PersonRepository : PanacheRepositoryBase<Person?, BigInteger?> {
    fun getPersonBasicInfo(id: BigInteger?): Person? =
        find("${Person_.ID} = ?1", id).singleResult()

    fun getPersonByUsernameAndPassword(username: String, password: String): Person? =
        find("${Person_.USERNAME} = ?1 and ${Person_.PASSWORD} = ?2", username, password).singleResult<Person>()

}