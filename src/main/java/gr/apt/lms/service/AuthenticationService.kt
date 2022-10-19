package gr.apt.lms.service

import com.vaadin.flow.component.UI
import com.vaadin.flow.server.VaadinSession
import gr.apt.lms.config.security.TokenService
import gr.apt.lms.dto.person.PersonDto
import gr.apt.lms.exception.LmsException
import gr.apt.lms.mapper.PersonMapper
import gr.apt.lms.mapper.RoleMapper
import gr.apt.lms.persistence.entity.Role
import gr.apt.lms.repository.PersonRepository
import gr.apt.lms.repository.RoleRepository
import io.quarkus.arc.Arc
import javax.inject.Inject
import javax.inject.Singleton

/*
    Service used to authenticate users
 */
@Singleton
class AuthenticationService {
    @Inject
    private lateinit var personRepository: PersonRepository

    @Inject
    private lateinit var roleRepository: RoleRepository

    fun authenticate(username: String?, password: String?): PersonDto {
        if (username == null || password == null) throw LmsException("Bad credentials")
        val personDto = PersonMapper.INSTANCE.entityToDto(
            personRepository.getPersonByUsernameAndPassword(username, password)
                ?: throw LmsException("Authentication Failed")
        )
        personDto.roles = RoleMapper.INSTANCE.entitiesToDtos(
            roleRepository.getPersonRoles(personDto.id ?: throw LmsException())
        ) ?: setOf()
        return personDto
    }
}

fun getToken(): String? = VaadinSession.getCurrent().getAttribute("token")?.toString()
fun login(username: String, password: String) {
    val authenticationService = Arc.container().instance(AuthenticationService::class.java).get()
    val loggedInUser = authenticationService.authenticate(username, password)
    val token = TokenService.generateToken(
        loggedInUser.id!!,
        loggedInUser.username!!,
        loggedInUser.roles!!.mapNotNull { it.role }.toSet(),
        loggedInUser.roles!!.filter { it.id == Role.ROLE_ADMIN }.map { it.id }.first()
            ?: throw LmsException("User has no roles assigned. Please assign role to user and try again")
        //loggedInUser.roles!!.firstNotNullOfOrNull { it.id } ?: throw LmsException("User has no roles assigned. Please assign role to user and try again")
    )
    VaadinSession.getCurrent().setAttribute("token", token)
    UI.getCurrent().navigate("")
}

fun logout() = VaadinSession.getCurrent()?.session?.invalidate()