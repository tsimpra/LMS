package gr.apt.lms.service

import gr.apt.lms.dto.MenuDto
import gr.apt.lms.exception.LmsException
import gr.apt.lms.mapper.MenuMapper
import gr.apt.lms.persistence.entity.Menu
import gr.apt.lms.repository.MenuRepository
import io.quarkus.panache.common.Page
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
@Transactional
class MenuService : CrudService<MenuDto> {

    @Inject
    private lateinit var repository: MenuRepository

    @Inject
    private lateinit var mapper: MenuMapper

    override fun create(dto: MenuDto): Boolean {
        val entity = mapper.DtoToEntity(dto)
        repository.persistAndFlush(entity)
        return true
    }

    override fun update(dto: MenuDto): Boolean {
        var entity = repository.findById(dto.id)
        if (entity != null) {
            entity = mapper.DtoToEntity(dto)
            repository.entityManager.merge(entity)
            return true
        }
        return false
    }

    override fun delete(dto: MenuDto): Boolean {
        val entity = repository.findById(dto.id)
        if (entity != null) {
            repository.delete(entity)
            return true
        }
        return false
    }

    override fun findAll(index: Int?, size: Int?): List<MenuDto> {
        return try {
            if (index != null && size != null) {
                val page = Page.of(index, size)
                return mapper.entitiesToDtos(repository.findAll().page<Menu>(page).list())
            }
            mapper.entitiesToDtos(repository.listAll())
        } catch (ex: Exception) {
            throw LmsException("An error occurred: ${ex.message}")
        }
    }

    fun findById(id: BigInteger): MenuDto {
        return mapper.entityToDto(repository.findById(id))
    }

}