package gr.apt.lms.service

import gr.apt.lms.dto.PersonRolesDto
import gr.apt.lms.mapper.PersonRolesMapper
import gr.apt.lms.repository.PersonRolesRepository
import java.math.BigInteger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional

@ApplicationScoped
@Transactional
class PersonRolesService {
    @get:Inject
    lateinit var repository: PersonRolesRepository

    @Inject
    lateinit var mapper: PersonRolesMapper

    fun findAll(): List<PersonRolesDto> {
        return mapper.entitiesToDtos(repository.listAll())
    }

    fun findById(id: BigInteger): PersonRolesDto {
        return mapper.entityToDto(repository.findById(id))
    }

    fun create(dto: PersonRolesDto?): Boolean {
        val entity = mapper.DtoToEntity(dto)
        repository.persistAndFlush(entity)
        return true
    }

    fun update(dto: PersonRolesDto): Boolean {
        var entity = repository.findById(dto.id)
        if (entity != null) {
            entity = mapper.DtoToEntity(dto)
            repository.persistAndFlush(entity)
            return true
        }
        return false
    }

    fun delete(dto: PersonRolesDto): Boolean {
        val entity = repository.findById(dto.id)
        if (entity != null) {
            repository.delete(entity)
            return true
        }
        return false
    }
}