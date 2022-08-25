package gr.apt.service

import gr.apt.dto.notification.CreateNotificationDto
import gr.apt.dto.person.PersonDto
import gr.apt.exception.LmsException
import gr.apt.mapper.PersonMapper
import gr.apt.persistence.entity.Person
import gr.apt.persistence.entity.Role
import gr.apt.repository.PersonRepository
import gr.apt.service.notification.NotificationService
import gr.apt.utils.calculateTotalNumberOfLeaves
import io.quarkus.panache.common.Page
import java.math.BigInteger
import java.util.Set
import java.util.function.Consumer
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional

@ApplicationScoped
@Transactional
class PersonService {
    @get:Inject
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
            val notification = CreateNotificationDto()
            notification.content = "New person had been added: ${entity.fname} ${entity.lname}"
            notification.recipientRoleIds = Set.of(Role.ROLE_ADMIN)
            notificationService.create(notification)
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
            repository.persistAndFlush(entity)

            //create notification
            val notification = CreateNotificationDto()
            notification.content = "The person with id: ${entity.id} has been modified"
            notification.recipientRoleIds = Set.of(Role.ROLE_ADMIN)
            notificationService.create(notification)
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