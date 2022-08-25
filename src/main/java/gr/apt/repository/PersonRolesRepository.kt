package gr.apt.repository

import gr.apt.persistence.entity.PersonRoles
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PersonRolesRepository : PanacheRepositoryBase<PersonRoles?, BigInteger?> {

    fun getRoleIdsByPersonId(personId: BigInteger) =
        find("select roleId from person_roles where personId = ?1", personId).project(BigInteger::class.java)
            .list<BigInteger>()

}