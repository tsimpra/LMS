package gr.apt.lms.repository

import gr.apt.lms.metamodel.entity.MenuRoles_
import gr.apt.lms.metamodel.entity.Menu_
import gr.apt.lms.persistence.entity.Menu
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.inject.Singleton

@Singleton
class MenuRepository : PanacheRepositoryBase<Menu?, BigInteger?> {

    fun getUserMenuByRoleId(roleId: BigInteger): List<Menu> =
        find(
            "select men from Menu as men join MenuRoles as mro on men.${Menu_.ID} = mro.${MenuRoles_.MENU_ID} " +
                    "where mro.${MenuRoles_.ROLE_ID} = ?1 order by men.${Menu_.DISPLAY_ORDER}", roleId
        )
            .list<Menu>()
}