package gr.apt.lms.repository

import gr.apt.lms.persistence.entity.Role
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RoleRepository : PanacheRepositoryBase<Role?, BigInteger?> {
    fun getPersonRoles(id: BigInteger) =
        list("select * from roles rol join person_roles pro on rol.id = pro.roleId where pro.personId = ?1", id)

}