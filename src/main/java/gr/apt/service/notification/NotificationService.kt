package gr.apt.service.notification

import gr.apt.dto.notification.CreateNotificationDto
import gr.apt.dto.notification.NotificationDto
import gr.apt.exception.LmsException
import gr.apt.mapper.NotificationMapper
import gr.apt.persistence.entity.Role
import gr.apt.persistence.entity.notification.Notification
import gr.apt.persistence.entity.notification.NotificationRecipientPersons
import gr.apt.persistence.entity.notification.NotificationRecipientRoles
import gr.apt.persistence.entity.notification.NotificationViewers
import gr.apt.repository.PersonRepository
import gr.apt.repository.PersonRolesRepository
import gr.apt.repository.RoleRepository
import gr.apt.repository.notification.NotificationRecipientPersonsRepository
import gr.apt.repository.notification.NotificationRecipientRolesRepository
import gr.apt.repository.notification.NotificationRepository
import gr.apt.repository.notification.NotificationViewersRepository
import gr.apt.utils.isNeitherNullNorEmpty
import io.quarkus.panache.common.Page
import java.math.BigInteger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional

@ApplicationScoped
@Transactional
class NotificationService {
    @get:Inject
    lateinit var repository: NotificationRepository

    @get:Inject
    lateinit var recipientPersonsRepository: NotificationRecipientPersonsRepository

    @get:Inject
    lateinit var recipientRolesRepository: NotificationRecipientRolesRepository

    @get:Inject
    lateinit var viewersRepository: NotificationViewersRepository

    @get:Inject
    lateinit var personRepository: PersonRepository

    @get:Inject
    lateinit var roleRepository: RoleRepository

    @get:Inject
    lateinit var personRoleRepository: PersonRolesRepository

    @Inject
    lateinit var mapper: NotificationMapper

    @Throws(LmsException::class)
    fun findAll(personId: BigInteger, index: Int?, size: Int?): List<NotificationDto> {
        return try {
            val person =
                personRepository.findById(personId) ?: throw LmsException("Person with id:$personId does not exist")
            val roleIds: Set<BigInteger> = personRoleRepository.getRoleIdsByPersonId(personId).toSet()
//            val notifIds: MutableSet<BigInteger?> = HashSet()
//            val recipientRoles = recipientRolesRepository.list("roleId", roleIds).filterNotNull()
//            if (recipientRoles.isNeitherNullNorEmpty()) {
//                recipientRoles.forEach { entry -> notifIds.add(entry.notifId) }
//            }
//            val recipientPersons =
//                recipientPersonsRepository.list("personId", person.id).filterNotNull()
//            if (recipientPersons.isNeitherNullNorEmpty()) {
//                recipientPersons.forEach { entry -> notifIds.add(entry.notifId) }
//            }
            val notificationsByPersonIdAndRoleIds = repository.getNotificationsByPersonIdAndRoleIds(roleIds, personId)
            if (index != null && size != null) {
                val page = Page.of(index, size)
                return mapper.entitiesToDtos(notificationsByPersonIdAndRoleIds.page<Notification>(page).list())
            }
            mapper.entitiesToDtos(notificationsByPersonIdAndRoleIds.list())
        } catch (ex: Exception) {
            throw LmsException("An error occurred:${ex.message}")
        }
    }

    @Throws(LmsException::class)
    fun readAll(personId: BigInteger?, ids: List<BigInteger>) {
        try {
            if (ids.isNeitherNullNorEmpty()) {
                ids.forEach { id ->
                    read(personId, id)
                }
            }
        } catch (ex: Exception) {
            throw LmsException("An error occurred:${ex.message}")
        }
    }

    @Throws(LmsException::class)
    fun read(personId: BigInteger?, id: BigInteger?): Boolean {
        return try {
            val notification = repository.findById(id) ?: throw LmsException("Notification with id:$id does not exist")
            var isRead = false
            //TODO: as sequence filter and any
            viewersRepository.list("notifId", notification.id).filterNotNull().forEach { viewer ->
                if (viewer.personId == personId) {
                    isRead = true
                }
            }
            if (!isRead) {
                viewersRepository.persist(NotificationViewers(personId, id))
            }
            true
        } catch (ex: Exception) {
            throw LmsException("An error occurred:${ex.message}")
        }
    }

    @Throws(LmsException::class)
    fun create(dto: CreateNotificationDto): Boolean {
        return try {
            val entity = mapper.DtoToEntity(dto)
            repository.persistAndFlush(entity)
            //repository.entityManager.refresh(entity)
            if (dto.recipientPersonId != null) {
                recipientPersonsRepository.persist(NotificationRecipientPersons(dto.recipientPersonId, entity.id))
            }
            if (dto.recipientRoleIds.isNeitherNullNorEmpty()) {
                dto.recipientRoleIds!!.forEach { roleId ->
                    recipientRolesRepository.persist(
                        NotificationRecipientRoles(
                            roleId,
                            entity.id
                        )
                    )
                }
            }
            true
        } catch (ex: Exception) {
            throw LmsException("An error occurred:${ex.message}")
        }
    }

    @Throws(LmsException::class)
    fun createGeneralNotification(dto: NotificationDto?): Boolean {
        return try {
            val entity = mapper.DtoToEntity(dto)
            repository.persistAndFlush(entity)
            //repository.entityManager.refresh(entity)
            val roles = roleRepository.listAll().filterNotNull()
            roles.forEach { role: Role ->
                recipientRolesRepository.persist(
                    NotificationRecipientRoles(
                        role.id,
                        entity.id
                    )
                )
            }
            true
        } catch (ex: Exception) {
            throw LmsException("An error occurred:${ex.message}")
        }
    }

    //    public Boolean delete(NotificationDto dto) {
    //        Notification entity = repository.findById(dto.getId());
    //        if (entity != null) {
    //            repository.delete(entity);
    //            return true;
    //        }
    //        return false;
    //    }
}