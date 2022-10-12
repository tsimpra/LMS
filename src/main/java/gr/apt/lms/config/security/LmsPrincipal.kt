package gr.apt.lms.config.security

import gr.apt.lms.dto.person.PersonDto
import java.math.BigInteger
import java.security.Principal

class LmsPrincipal(private val user: PersonDto, private val roles: Set<String>, val selectedRole: BigInteger) :
    Principal {

    override fun getName(): String = user.id.toString()

    fun isUserInRole(role: String?): Boolean {
        return roles.contains(role)
    }

}