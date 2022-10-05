package gr.apt.lms.persistence.entity

import java.io.Serializable
import java.math.BigInteger
import javax.persistence.*

@Entity
@Table(name = "menu_roles", schema = "lms")
@IdClass(value = MenuRolesPK::class)
class MenuRoles() {
    @Id
    @Column(name = "menu_id")
    var menuId: BigInteger? = null

    @Id
    @Column(name = "role_id")
    var roleId: BigInteger? = null

    constructor(menuId: BigInteger, roleId: BigInteger) : this() {
        this.menuId = menuId
        this.roleId = roleId
    }
}

class MenuRolesPK : Serializable {
    private var menuId: BigInteger? = null
    private var roleId: BigInteger? = null
}