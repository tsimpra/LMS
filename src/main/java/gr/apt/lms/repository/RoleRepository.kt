package gr.apt.lms.repository

import gr.apt.lms.persistence.entity.Role
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.inject.Singleton

@Singleton
class RoleRepository : PanacheRepositoryBase<Role?, BigInteger?> {
    fun getPersonRoles(id: BigInteger) =
        find(
            "select new Role(rol.id,rol.role) from Role rol join PersonRoles pro on rol.id = pro.roleId where pro.personId = ?1",
            id
        ).list<Role>()

}