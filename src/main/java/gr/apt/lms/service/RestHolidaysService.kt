package gr.apt.lms.service

import gr.apt.lms.dto.RestHolidaysDto
import gr.apt.lms.mapper.RestHolidaysMapper
import gr.apt.lms.repository.RestHolidaysRepository
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
@Transactional
class RestHolidaysService : CrudService<RestHolidaysDto> {
    @Inject
    lateinit var repository: RestHolidaysRepository

    @Inject
    lateinit var mapper: RestHolidaysMapper

    fun findAll(): List<RestHolidaysDto> {
        return mapper.entitiesToDtos(repository.listAll())
    }

    fun findById(id: BigInteger): RestHolidaysDto {
        return mapper.entityToDto(repository.findById(id))
    }

    override fun create(dto: RestHolidaysDto): Boolean {
        val entity = mapper.DtoToEntity(dto)
        repository.persistAndFlush(entity)
        return true
    }

    override fun update(dto: RestHolidaysDto): Boolean {
        var entity = repository.findById(dto.id)
        if (entity != null) {
            entity = mapper.DtoToEntity(dto)
            repository.persistAndFlush(entity)
            return true
        }
        return false
    }

    override fun delete(dto: RestHolidaysDto): Boolean {
        val entity = repository.findById(dto.id)
        if (entity != null) {
            repository.delete(entity)
            return true
        }
        return false
    }
}