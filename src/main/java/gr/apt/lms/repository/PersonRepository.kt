package gr.apt.lms.repository

import gr.apt.lms.persistence.entity.Person
import io.quarkus.hibernate.orm.panache.PanacheQuery
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PersonRepository : PanacheRepositoryBase<Person?, BigInteger?> {
    fun getPersonBasicInfo(id: BigInteger?): PanacheQuery<Person?> =
        find("select id,fname,lname,email,username,job from person where id = ?1", id)

}