package gr.apt.service

import gr.apt.dto.RoleDto
import gr.apt.mapper.RoleMapper
import gr.apt.repository.RoleRepository
import java.math.BigInteger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional

@ApplicationScoped
@Transactional
class RoleService {
    @get:Inject
    lateinit var repository: RoleRepository

    @Inject
    lateinit var mapper: RoleMapper

    fun findAll(): List<RoleDto> {
        return mapper.entitiesToDtos(repository.listAll())
    }

    fun findById(id: BigInteger): RoleDto {
        return mapper.entityToDto(repository.findById(id))
    }

    fun create(dto: RoleDto?): Boolean {
        val entity = mapper.DtoToEntity(dto)
        repository.persistAndFlush(entity)
        return true
    }

    fun update(dto: RoleDto): Boolean {
        var entity = repository.findById(dto.id)
        if (entity != null) {
            entity = mapper.DtoToEntity(dto)
            repository.persistAndFlush(entity)
            return true
        }
        return false
    }

    fun delete(dto: RoleDto): Boolean {
        val entity = repository.findById(dto.id)
        if (entity != null) {
            repository.delete(entity)
            return true
        }
        return false
    }
}