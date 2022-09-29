package gr.apt.lms.service

import gr.apt.lms.dto.RoleDto
import gr.apt.lms.exception.LmsException
import gr.apt.lms.mapper.RoleMapper
import gr.apt.lms.persistence.entity.Role
import gr.apt.lms.repository.RoleRepository
import io.quarkus.panache.common.Page
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
@Transactional
class RoleService : CrudService<RoleDto> {
    @Inject
    private lateinit var repository: RoleRepository

    @Inject
    private lateinit var mapper: RoleMapper

    override fun findAll(index: Int?, size: Int?): List<RoleDto> {
        return try {
            if (index != null && size != null) {
                val page = Page.of(index, size)
                return mapper.entitiesToDtos(repository.findAll().page<Role>(page).list())
            }
            mapper.entitiesToDtos(repository.listAll())
        } catch (ex: Exception) {
            throw LmsException("An error occurred: ${ex.message}")
        }
    }

    fun findById(id: BigInteger): RoleDto {
        return mapper.entityToDto(repository.findById(id))
    }

    override fun create(dto: RoleDto): Boolean {
        val entity = mapper.DtoToEntity(dto)
        repository.persistAndFlush(entity)
        return true
    }

    override fun update(dto: RoleDto): Boolean {
        var entity = repository.findById(dto.id)
        if (entity != null) {
            entity = mapper.DtoToEntity(dto)
            repository.entityManager.merge(entity)
            return true
        }
        return false
    }

    override fun delete(dto: RoleDto): Boolean {
        val entity = repository.findById(dto.id)
        if (entity != null) {
            repository.delete(entity)
            return true
        }
        return false
    }
}