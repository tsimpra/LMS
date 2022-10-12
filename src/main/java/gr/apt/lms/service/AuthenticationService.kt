package gr.apt.lms.service

import gr.apt.lms.dto.person.PersonDto
import gr.apt.lms.exception.LmsException
import gr.apt.lms.mapper.PersonMapper
import gr.apt.lms.mapper.RoleMapper
import gr.apt.lms.repository.PersonRepository
import gr.apt.lms.repository.RoleRepository
import javax.inject.Inject
import javax.inject.Singleton

/*
    Service used to authenticate users and
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