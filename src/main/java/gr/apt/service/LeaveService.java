package gr.apt.service;

import gr.apt.exception.LmsException;
import gr.apt.persistence.dto.ApproveLeaveDto;
import gr.apt.persistence.dto.CreateNotificationDto;
import gr.apt.persistence.dto.LeaveDto;
import gr.apt.persistence.entity.Leave;
import gr.apt.persistence.entity.Person;
import gr.apt.persistence.entity.Role;
import gr.apt.persistence.enumeration.LeaveType;
import gr.apt.persistence.enumeration.YesOrNo;
import gr.apt.persistence.mapper.LeaveMapper;
import gr.apt.repository.LeaveRepository;
import gr.apt.repository.PersonRepository;
import gr.apt.service.notification.NotificationService;
import io.quarkus.panache.common.Page;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static gr.apt.utils.LeaveUtils.getNumberOfRequestedLeaves;
import static gr.apt.utils.LeaveUtils.getRemainingLeaves;

@ApplicationScoped
@Transactional
public class LeaveService {
    @Inject
    LeaveRepository repository;
    @Inject
    PersonRepository personRepository;
    @Inject
    NotificationService notificationService;
    @Inject
    LeaveMapper mapper;

    public List<ApproveLeaveDto> findAll(Integer index, Integer size) throws LmsException {
        try {
            if (index != null && size != null) {
                Page page = Page.of(index, size);
                return mapper.entitiesToDtos(repository.findAll().page(page).list());
            }
            return mapper.entitiesToDtos(repository.listAll());
        } catch (Exception ex) {
            throw new LmsException("An error occurred :" + ex.getMessage());
        }
    }

    public List<ApproveLeaveDto> findAllByPersonId(BigInteger personId, Integer index, Integer size) throws LmsException {
        try {
            if (index != null && size != null) {
                Page page = Page.of(index, size);
                return mapper.entitiesToDtos(repository.find("personId", personId).page(page).list());
            }
            return mapper.entitiesToDtos(repository.list("personId", personId));
        } catch (Exception ex) {
            throw new LmsException("An error occurred :" + ex.getMessage());
        }
    }

    public List<ApproveLeaveDto> findAllPending(Integer index, Integer size) throws LmsException {
        try {
            if (index != null && size != null) {
                Page page = Page.of(index, size);
                return mapper.entitiesToDtos(repository.find("approved is null").page(page).list());
            }
            return mapper.entitiesToDtos(repository.list("approved is null"));
        } catch (Exception ex) {
            throw new LmsException("An error occurred :" + ex.getMessage());
        }
    }

    public List<ApproveLeaveDto> findAllApproved(Integer index, Integer size) throws LmsException {
        try {
            if (index != null && size != null) {
                Page page = Page.of(index, size);
                return mapper.entitiesToDtos(repository.find("approved", YesOrNo.YES).page(page).list());
            }
            return mapper.entitiesToDtos(repository.list("approved", YesOrNo.YES));
        } catch (Exception ex) {
            throw new LmsException("An error occurred :" + ex.getMessage());
        }
    }

    public LeaveDto findById(BigInteger id) throws LmsException {
        try {
            return mapper.entityToDto(repository.findById(id));
        } catch (Exception ex) {
            throw new LmsException("An error occurred:" + ex.getMessage());
        }
    }

    public Boolean create(LeaveDto dto) throws LmsException {
        try {
            Leave entity = mapper.DtoToEntity(dto);
            if (entity.getType().equals(LeaveType.PAID_LEAVE)) {
                Person person = personRepository.findById(entity.getPersonId());
                if (person == null)
                    throw new LmsException("Person with id:" + dto.getPersonId().getId() + " does not exist");
                if (getRemainingLeaves(person) < getNumberOfRequestedLeaves(entity)) {
                    throw new LmsException("Person with id:" + dto.getPersonId().getId() + " does not have enough remaining days of leave");
                }
            }
            repository.persistAndFlush(entity);

            //create notification
            CreateNotificationDto notification = new CreateNotificationDto();
            notification.setContent("New leave request have been submitted from " + dto.getPersonId().getFname() + dto.getPersonId().getLname());
            notification.setRecipientRoleIds(Set.of(Role.ROLE_ADMIN));
            notificationService.create(notification);
            return true;
        } catch (Exception ex) {
            throw new LmsException("An error occurred:" + ex.getMessage());
        }
    }

    public Boolean update(LeaveDto dto) throws LmsException {
        Leave entity = repository.findById(dto.getId());
        if (entity != null) {
            if (entity.getApproved() == null) {
                entity = mapper.DtoToEntity(dto);
                if (entity.getType().equals(LeaveType.PAID_LEAVE)) {
                    Person person = personRepository.findById(entity.getPersonId());
                    if (person == null)
                        throw new LmsException("Person with id:" + dto.getPersonId().getId() + " does not exist");
                    if (getRemainingLeaves(person) < getNumberOfRequestedLeaves(entity)) {
                        throw new LmsException("Person with id:" + dto.getPersonId().getId() + " does not have enough remaining days of leave");
                    }
                }
                repository.persistAndFlush(entity);

                //create notification
                CreateNotificationDto notification = new CreateNotificationDto();
                notification.setContent("A leave request have been updated from " + dto.getPersonId().getFname() + " " + dto.getPersonId().getLname());
                notification.setRecipientRoleIds(Set.of(Role.ROLE_ADMIN));
                notificationService.create(notification);
                return true;
            } else {
                throw new LmsException("Leave with id:" + dto.getId() + " cannot get modified because has been marked as " + (entity.getApproved().equals(YesOrNo.YES) ? "approved" : "not approved"));
            }
        }
        throw new LmsException("Leave with id:" + dto.getId() + " does not exist");
    }

    public Boolean delete(LeaveDto dto) throws LmsException {
        Leave entity = repository.findById(dto.getId());
        if (entity != null) {
            repository.delete(entity);
            return true;
        }
        throw new LmsException("Leave with id:" + dto.getId() + " does not exist");
    }

    public Boolean approve(ApproveLeaveDto dto) throws LmsException {
        Leave entity = repository.findById(dto.getId());
        if (entity != null) {
            if (dto.getApproved().equals(YesOrNo.YES) && entity.getType().equals(LeaveType.PAID_LEAVE)) {
                if (getRemainingLeaves(entity.getPersonByPersonId()) < getNumberOfRequestedLeaves(entity)) {
                    throw new LmsException("Person with id:" + entity.getPersonId() + " does not have enough remaining days of leave");
                }
            }
            entity.setApproved(dto.getApproved());
            entity.setApprovedDate(LocalDate.now());
            entity.setApprovedBy(BigInteger.ONE);
            repository.persistAndFlush(entity);

            //create notification
            CreateNotificationDto notification = new CreateNotificationDto();
            notification.setContent("Your request have been updated as " + (dto.getApproved().equals(YesOrNo.YES) ? "approved" : "not approved"));
            notification.setRecipientPersonId(entity.getPersonId());
            notification.setRecipientRoleIds(Set.of(Role.ROLE_ADMIN));
            notificationService.create(notification);
            return true;
        }
        throw new LmsException("Leave with id:" + dto.getId() + " does not exist");
    }


}
