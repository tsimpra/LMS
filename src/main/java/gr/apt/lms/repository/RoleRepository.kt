package gr.apt.lms.repository

import gr.apt.lms.metamodel.entity.PersonRoles_
import gr.apt.lms.metamodel.entity.Role_
import gr.apt.lms.persistence.entity.Role
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.inject.Singleton

@Singleton
class RoleRepository : PanacheRepositoryBase<Role?, BigInteger?> {
    fun getPersonRoles(id: BigInteger) =
        find(
            "select new Role(rol.${Role_.ID},rol.${Role_.ROLE}) " +
                    "from Role rol " +
                    "join PersonRoles pro on rol.${Role_.ID} = pro.${PersonRoles_.ROLE_ID} " +
                    "where pro.${PersonRoles_.PERSON_ID} = ?1",
            id
        ).list<Role>()

}