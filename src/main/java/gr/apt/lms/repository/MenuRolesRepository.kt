package gr.apt.lms.repository

import gr.apt.lms.metamodel.entity.MenuRoles_
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton
import javax.persistence.EntityManager

@Singleton
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

}