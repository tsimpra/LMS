package gr.apt.lms.service

import gr.apt.lms.dto.person.PersonDto
import gr.apt.lms.exception.LmsException
import gr.apt.lms.mapper.PersonMapper
import gr.apt.lms.persistence.entity.Person
import gr.apt.lms.persistence.entity.Role
import gr.apt.lms.repository.PersonRepository
import gr.apt.lms.service.notification.NotificationService
import gr.apt.lms.utils.calculateTotalNumberOfLeaves
import io.quarkus.panache.common.Page
import java.math.BigInteger
import java.util.function.Consumer
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
@Transactional
class PersonService {
    @Inject
    lateinit var repository: PersonRepository

    @Inject
    lateinit var notificationService: NotificationService

    @Inject
    lateinit var mapper: PersonMapper

    @Throws(LmsException::class)
    fun findAll(index: Int?, size: Int?): List<PersonDto> {
        return try {
            if (index != null && size != null) {
                val page = Page.of(index, size)
                return mapper.entitiesToDtos(repository.findAll().page<Person>(page).list())
            }
            mapper.entitiesToDtos(repository.listAll())
        } catch (ex: Exception) {
            throw LmsException("An error occurred: ${ex.message}")
        }
    }

    @Throws(LmsException::class)
    fun findById(id: BigInteger): PersonDto {
        return try {
            mapper.entityToDto(repository.findById(id))
        } catch (ex: Exception) {
            throw LmsException("An error occurred: ${ex.message}")
        }
    }

    @Throws(LmsException::class)
    fun create(dto: PersonDto): Boolean {
        return try {
            val entity: Person = mapper.DtoToEntity(dto)
            entity.numberOfLeaves = calculateTotalNumberOfLeaves(
                dto.dateOfEmployment ?: throw LmsException("Date of employment cannot be null")
            )
            repository.persistAndFlush(entity)

            //create notification
            notificationService.createNotification(
                content = "New person had been added: ${entity.fname} ${entity.lname}",
                recipientRoleIds = mutableSetOf(Role.ROLE_ADMIN)
            )
            true
        } catch (ex: Exception) {
            throw LmsException("An error occurred: ${ex.message}")
        }
    }

    @Throws(LmsException::class)
    fun update(dto: PersonDto): Boolean {
        repository.findById(dto.id ?: throw LmsException("Id cannot be null"))
            ?: throw LmsException("Could not find person with id ${dto.id}")
        return try {
            val entity = mapper.DtoToEntity(dto)
            repository.entityManager.merge(entity)

            //create notification
            notificationService.createNotification(
                content = "The person with id: ${entity.id} has been modified",
                recipientRoleIds = mutableSetOf(Role.ROLE_ADMIN)
            )
            true
        } catch (ex: Exception) {
            throw LmsException("An error occurred: ${ex.message}")
        }
    }

    @Throws(LmsException::class)
    fun delete(dto: PersonDto): Boolean {
        val entity: Person = repository.findById(dto.id ?: throw LmsException("Id cannot be null"))
            ?: throw LmsException("Could not find person with id ${dto.id}")
        return try {
            repository.delete(entity)
            true
        } catch (ex: Exception) {
            throw LmsException("An error occurred: ${ex.message}")
        }

    }

    @Throws(LmsException::class)
    fun refreshDaysOfLeaves(): Boolean {
        return try {
            val list: List<Person> = repository.listAll().filterNotNull()
            list.forEach(Consumer<Person> { p: Person ->
                p.numberOfLeaves = calculateTotalNumberOfLeaves(
                    p.dateOfEmployment ?: throw LmsException("Date of employment cannot be null")
                )
            })
            repository.persist(list)
            true
        } catch (ex: Exception) {
            throw LmsException("An error occurred: ${ex.message}")
        }
    }
}