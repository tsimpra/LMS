package gr.apt.lms.repository

import gr.apt.lms.persistence.entity.PersonRoles
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.inject.Singleton

@Singleton
class PersonRolesRepository : PanacheRepositoryBase<PersonRoles?, BigInteger?> {

    fun getRoleIdsByPersonId(personId: BigInteger) =
        find("select roleId from person_roles where personId = ?1", personId).project(BigInteger::class.java)
            .list<BigInteger>()

}