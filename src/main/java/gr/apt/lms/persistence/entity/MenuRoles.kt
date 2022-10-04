package gr.apt.lms.persistence.entity

import java.io.Serializable
import java.math.BigInteger
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "menu_roles", schema = "lms")
@IdClass(value = MenuRolesPK::class)
class MenuRoles {
    @Id
    var menuId: BigInteger? = null

    @Id
    var roleId: BigInteger? = null
}

class MenuRolesPK : Serializable {
    private var menuId: BigInteger? = null
    private var roleId: BigInteger? = null
}