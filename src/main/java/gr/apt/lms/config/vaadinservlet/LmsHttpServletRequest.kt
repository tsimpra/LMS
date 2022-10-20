package gr.apt.lms.config.vaadinservlet

import gr.apt.lms.config.security.LmsPrincipal
import gr.apt.lms.config.security.TokenService
import gr.apt.lms.exception.LmsException
import gr.apt.lms.persistence.entity.Person
import gr.apt.lms.repository.PersonRepository
import gr.apt.lms.repository.RoleRepository
import gr.apt.lms.service.getToken
import io.quarkus.arc.Arc
import java.math.BigInteger
import java.security.Principal
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

/*
    Http Servlet Request configuration to override and retrieve Principal from jwt token
    that is stored on session
 */
class LmsHttpServletRequest(request: HttpServletRequest?) : HttpServletRequestWrapper(request) {
    override fun getUserPrincipal(): Principal? {
        val token = getToken()
        return if (token != null && !TokenService.isTokenExpired(token)) {
            val personId = TokenService.getPersonFromToken(token)
            val username = TokenService.getUsernameFromToken(token)
            val selectedRole = TokenService.getSelectedRoleFromToken(token)
            val roles = TokenService.getRolesFromToken(token)
            val personRepository = Arc.container().instance(PersonRepository::class.java).get()
            if (personId != null && username != null && roles != null && selectedRole != null) {
                val person =
                    personRepository.findById(BigInteger(personId.toString())) ?: throw LmsException("User not found.")
                checkPerson(person, username, roles)
                return LmsPrincipal(person, roles, BigInteger(selectedRole.toString()))
            } else
                return null
        } else {
            null
        }
    }

    override fun isUserInRole(role: String): Boolean {
        return (userPrincipal is LmsPrincipal
                && (userPrincipal as LmsPrincipal).isUserInRole(role)
                || super.isUserInRole(role))
    }

    private fun checkPerson(person: Person, username: String, roles: Set<String>) {
        val roleRepository = Arc.container().instance(RoleRepository::class.java).get()
        val personRoles = roleRepository.getPersonRoles(person.id!!).map { it.role }
        if (person.username != username || !personRoles.containsAll(roles)) throw LmsException("Invalid or malformed token")
    }
}