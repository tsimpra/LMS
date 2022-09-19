package gr.apt.lms.service

import gr.apt.lms.dto.PersonRolesDto
import gr.apt.lms.exception.LmsException
import gr.apt.lms.mapper.PersonRolesMapper
import gr.apt.lms.persistence.entity.PersonRoles
import gr.apt.lms.repository.PersonRolesRepository
import io.quarkus.panache.common.Page
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
@Transactional
class PersonRolesService : CrudService<PersonRolesDto> {
    @Inject
    lateinit var repository: PersonRolesRepository

    @Inject
    lateinit var mapper: PersonRolesMapper

    override fun findAll(index: Int?, size: Int?): List<PersonRolesDto> {
        return try {
            if (index != null && size != null) {
                val page = Page.of(index, size)
                return mapper.entitiesToDtos(repository.findAll().page<PersonRoles>(page).list())
            }
            mapper.entitiesToDtos(repository.listAll())
        } catch (ex: Exception) {
            throw LmsException("An error occurred: ${ex.message}")
        }
    }

    fun findById(id: BigInteger): PersonRolesDto {
        return mapper.entityToDto(repository.findById(id))
    }

    override fun create(dto: PersonRolesDto): Boolean {
        val entity = mapper.DtoToEntity(dto)
        repository.persistAndFlush(entity)
        return true
    }

    override fun update(dto: PersonRolesDto): Boolean {
        var entity = repository.findById(dto.id)
        if (entity != null) {
            entity = mapper.DtoToEntity(dto)
            repository.entityManager.merge(entity)
            return true
        }
        return false
    }

    override fun delete(dto: PersonRolesDto): Boolean {
        val entity = repository.findById(dto.id)
        if (entity != null) {
            repository.delete(entity)
            return true
        }
        return false
    }
}