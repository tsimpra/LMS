package gr.apt.lms.config.security

import gr.apt.lms.persistence.entity.Person
import java.math.BigInteger
import java.security.Principal

class LmsPrincipal(
    private val user: Person,
    private val roles: Set<String>,
    val selectedRole: BigInteger
) : Principal {

    override fun getName(): String = user.id.toString()

    fun isUserInRole(role: String?): Boolean {
        return roles.contains(role)
    }

}