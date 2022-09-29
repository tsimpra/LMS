package gr.apt.lms.service

import gr.apt.lms.dto.RestHolidaysDto
import gr.apt.lms.exception.LmsException
import gr.apt.lms.mapper.RestHolidaysMapper
import gr.apt.lms.persistence.holiday.RestHolidays
import gr.apt.lms.repository.RestHolidaysRepository
import io.quarkus.panache.common.Page
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
@Transactional
class RestHolidaysService : CrudService<RestHolidaysDto> {
    @Inject
    private lateinit var repository: RestHolidaysRepository

    @Inject
    private lateinit var mapper: RestHolidaysMapper

    override fun findAll(index: Int?, size: Int?): List<RestHolidaysDto> {
        return try {
            if (index != null && size != null) {
                val page = Page.of(index, size)
                return mapper.entitiesToDtos(repository.findAll().page<RestHolidays>(page).list())
            }
            mapper.entitiesToDtos(repository.listAll())
        } catch (ex: Exception) {
            throw LmsException("An error occurred: ${ex.message}")
        }
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
            repository.entityManager.merge(entity)
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