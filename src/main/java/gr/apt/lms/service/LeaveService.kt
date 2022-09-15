package gr.apt.lms.service

import gr.apt.lms.dto.leave.ApproveLeaveDto
import gr.apt.lms.dto.leave.LeaveDto
import gr.apt.lms.exception.LmsException
import gr.apt.lms.mapper.LeaveMapper
import gr.apt.lms.metamodel.entity.Leave_
import gr.apt.lms.persistence.entity.Leave
import gr.apt.lms.persistence.entity.Person
import gr.apt.lms.persistence.entity.Role
import gr.apt.lms.persistence.enumeration.LeaveType
import gr.apt.lms.persistence.enumeration.YesOrNo
import gr.apt.lms.repository.LeaveRepository
import gr.apt.lms.repository.PersonRepository
import gr.apt.lms.service.notification.NotificationService
import gr.apt.lms.utils.getNumberOfRequestedLeaves
import gr.apt.lms.utils.getRemainingLeaves
import io.quarkus.panache.common.Page
import java.math.BigInteger
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
@Transactional
class LeaveService {
    @Inject
    lateinit var repository: LeaveRepository

    @Inject
    lateinit var personRepository: PersonRepository

    @Inject
    lateinit var notificationService: NotificationService

    @Inject
    lateinit var mapper: LeaveMapper

    @Throws(LmsException::class)
    fun findAll(index: Int?, size: Int?): List<LeaveDto> {
        return try {
            if (index != null && size != null) {
                val page = Page.of(index, size)
                return mapper.entitiesToDtos(repository.findAll().page<Leave>(page).list())
            }
            mapper.entitiesToDtos(repository.listAll())
        } catch (ex: Exception) {
            throw LmsException("An error occurred :${ex.message}")
        }
    }

    @Throws(LmsException::class)
    fun findAllByPersonId(personId: BigInteger, index: Int?, size: Int?): List<LeaveDto> {
        return try {
            if (index != null && size != null) {
                val page = Page.of(index, size)
                return mapper.entitiesToDtos(repository.find(Leave_.PERSON_ID, personId).page<Leave>(page).list())
            }
            mapper.entitiesToDtos(repository.list(Leave_.PERSON_ID, personId))
        } catch (ex: Exception) {
            throw LmsException("An error occurred :${ex.message}")
        }
    }

    @Throws(LmsException::class)
    fun findAllPending(index: Int?, size: Int?): List<LeaveDto> {
        return try {
            if (index != null && size != null) {
                val page = Page.of(index, size)
                return mapper.entitiesToDtos(repository.find("${Leave_.APPROVED} is null").page<Leave>(page).list())
            }
            mapper.entitiesToDtos(repository.list("${Leave_.APPROVED} is null"))
        } catch (ex: Exception) {
            throw LmsException("An error occurred :${ex.message}")
        }
    }

    @Throws(LmsException::class)
    fun findAllApproved(index: Int?, size: Int?): List<LeaveDto> {
        return try {
            if (index != null && size != null) {
                val page = Page.of(index, size)
                return mapper.entitiesToDtos(
                    repository.find(Leave_.APPROVED, YesOrNo.YES).page<Leave>(page).list<Leave>()
                )
            }
            mapper.entitiesToDtos(repository.list(Leave_.APPROVED, YesOrNo.YES))
        } catch (ex: Exception) {
            throw LmsException("An error occurred :${ex.message}")
        }
    }

    @Throws(LmsException::class)
    fun findById(id: BigInteger?): LeaveDto {
        return try {
            mapper.entityToDto(repository.findById(id))
        } catch (ex: Exception) {
            throw LmsException("An error occurred:${ex.message}")
        }
    }

    @Throws(LmsException::class)
    fun create(dto: LeaveDto): Boolean {
        return try {
            val person = processLeave(dto)

            //create notification
            notificationService.createNotification(
                content = "New leave request have been submitted from ${person.fname} ${person.lname}",
                recipientRoleIds = mutableSetOf(Role.ROLE_ADMIN)
            )
            true
        } catch (ex: Exception) {
            throw LmsException("An error occurred:${ex.message}")
        }
    }

    @Throws(LmsException::class)
    fun update(dto: LeaveDto): Boolean {
        val entity: Leave =
            repository.findById(dto.id) ?: throw LmsException("Leave with id: ${dto.id} does not exist")
        return if (entity.approved == null) {
            val person = processLeave(dto)

            //create notification
            notificationService.createNotification(
                content = "A leave request have been updated from ${person.fname} ${person.lname}",
                recipientRoleIds = mutableSetOf(Role.ROLE_ADMIN)
            )
            true
        } else {
            throw LmsException("Leave with id:" + entity.id + " cannot get modified because has been marked as " + if (entity.approved == YesOrNo.YES) "approved" else "not approved")
        }
    }

    private fun processLeave(dto: LeaveDto): Person {
        val entity: Leave = mapper.DtoToEntity(dto)
        val person = personRepository.findById(dto.personId)
            ?: throw LmsException("Person with id: ${dto.personId} does not exist")
        if (entity.type == LeaveType.PAID_LEAVE) {
            if (person.getRemainingLeaves() < entity.getNumberOfRequestedLeaves()) {
                throw LmsException("Person with id: ${person.id} does not have enough remaining days of leave")
            }
        }
        repository.persistAndFlush(entity)
        return person
    }

    @Throws(LmsException::class)
    fun delete(dto: LeaveDto): Boolean {
        val entity: Leave =
            repository.findById(dto.id) ?: throw LmsException("Leave with id: ${dto.id} does not exist")
        try {
            repository.delete(entity)
            return true
        } catch (ex: Exception) {
            throw LmsException("An error occurred:${ex.message}")
        }
    }

    @Throws(LmsException::class)
    fun approve(dto: ApproveLeaveDto): Boolean {
        val entity: Leave =
            repository.findById(dto.id) ?: throw LmsException("Leave with id: ${dto.id} does not exist")
        //check if person has enough remaining leaves only if approved is yes
        if (dto.approved == YesOrNo.YES && entity.type == LeaveType.PAID_LEAVE) {
            val person = personRepository.findById(entity.personId)
                ?: throw LmsException("Person with id: ${entity.personId} does not exist")
            if (person.getRemainingLeaves() < entity.getNumberOfRequestedLeaves()) {
                throw LmsException("Person with id: ${entity.personId} does not have enough remaining days of leave")
            }
        }
        entity.approved = dto.approved
        entity.approvedDate = LocalDate.now()
        entity.approvedBy = BigInteger.ONE // TODO:to be retrieved from token
        repository.persistAndFlush(entity)

        //create notification
        notificationService.createNotification(
            content = "Your request have been updated as " + if (dto.approved == YesOrNo.YES) "approved" else "not approved",
            recipientPersonId = entity.personId,
            recipientRoleIds = mutableSetOf(Role.ROLE_ADMIN)
        )
        return true
    }
}