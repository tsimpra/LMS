package gr.apt.lms.repository

import gr.apt.lms.metamodel.entity.MenuRoles_
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Singleton
@Transactional
class MenuRolesRepository {

    @Inject
    private lateinit var entityManager: EntityManager

    fun getMenuListByRoleId(roleId: BigInteger) =
        entityManager.createQuery(
            "select ${MenuRoles_.MENU_ID} from MenuRoles where ${MenuRoles_.ROLE_ID} = ?1",
            BigInteger::class.java
        )
            .setParameter(1, roleId)
            .resultList

    fun create(menuId: BigInteger, roleId: BigInteger) {
        val query =
            entityManager.createNativeQuery("insert into menu_roles(menu_id,role_id) values (?,?);")
        query.setParameter(1, menuId)
        query.setParameter(2, roleId)
        query.executeUpdate()
    }

    fun delete(menuId: BigInteger, roleId: BigInteger) {
        val query =
            entityManager.createNativeQuery("delete from menu_roles where menu_id = ? and role_id = ?;")
        query.setParameter(1, menuId)
        query.setParameter(2, roleId)
        query.executeUpdate()
    }

}