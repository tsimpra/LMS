package gr.apt.service

import gr.apt.dto.RestHolidaysDto
import gr.apt.mapper.RestHolidaysMapper
import gr.apt.repository.RestHolidaysRepository
import java.math.BigInteger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional

@ApplicationScoped
@Transactional
class RestHolidaysService {
    @get:Inject
    lateinit var repository: RestHolidaysRepository

    @Inject
    lateinit var mapper: RestHolidaysMapper

    fun findAll(): List<RestHolidaysDto> {
        return mapper.entitiesToDtos(repository.listAll())
    }

    fun findById(id: BigInteger): RestHolidaysDto {
        return mapper.entityToDto(repository.findById(id))
    }

    fun create(dto: RestHolidaysDto?): Boolean {
        val entity = mapper.DtoToEntity(dto)
        repository.persistAndFlush(entity)
        return true
    }

    fun update(dto: RestHolidaysDto): Boolean {
        var entity = repository.findById(dto.id)
        if (entity != null) {
            entity = mapper.DtoToEntity(dto)
            repository.persistAndFlush(entity)
            return true
        }
        return false
    }

    fun delete(dto: RestHolidaysDto): Boolean {
        val entity = repository.findById(dto.id)
        if (entity != null) {
            repository.delete(entity)
            return true
        }
        return false
    }
}