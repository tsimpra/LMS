package gr.apt.service;

import gr.apt.dto.notification.CreateNotificationDto;
import gr.apt.dto.person.PersonDto;
import gr.apt.exception.LmsException;
import gr.apt.mapper.PersonMapper;
import gr.apt.persistence.entity.Person;
import gr.apt.persistence.entity.Role;
import gr.apt.repository.PersonRepository;
import gr.apt.service.notification.NotificationService;
import io.quarkus.panache.common.Page;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import static gr.apt.utils.LeaveUtils.calculateTotalNumberOfLeaves;

@ApplicationScoped
@Transactional
public class PersonService {

    @Inject
    PersonRepository repository;
    @Inject
    NotificationService notificationService;
    @Inject
    PersonMapper mapper;

    public List<PersonDto> findAll(Integer index, Integer size) throws LmsException {
        try {
            if (index != null && size != null) {
                Page page = Page.of(index, size);
                return mapper.entitiesToDtos(repository.findAll().page(page).list());
            }
            return mapper.entitiesToDtos(repository.listAll());
        } catch (Exception ex) {
            throw new LmsException("An error occurred:" + ex.getMessage());
        }
    }

    public PersonDto findById(BigInteger id) throws LmsException {
        try {
            return mapper.entityToDto(repository.findById(id));
        } catch (Exception ex) {
            throw new LmsException("An error occurred:" + ex.getMessage());
        }
    }

    public Boolean create(PersonDto dto) throws LmsException {
        try {
            Person entity = mapper.DtoToEntity(dto);
            entity.setNumberOfLeaves(calculateTotalNumberOfLeaves(dto.getDateOfEmployment()));
            repository.persistAndFlush(entity);

            //create notification
            CreateNotificationDto notification = new CreateNotificationDto();
            notification.setContent("New person had been added: " + entity.getFname() + " " + entity.getLname());
            notification.setRecipientRoleIds(Set.of(Role.ROLE_ADMIN));
            notificationService.create(notification);
            return true;
        } catch (Exception ex) {
            throw new LmsException("An error occurred:" + ex.getMessage());
        }
    }

    public Boolean update(PersonDto dto) throws LmsException {
        Person entity = repository.findById(dto.getId());
        if (entity != null) {
            try {
                entity = mapper.DtoToEntity(dto);
                repository.persistAndFlush(entity);

                //create notification
                CreateNotificationDto notification = new CreateNotificationDto();
                notification.setContent("The person with id: " + entity.getId() + " has been modified");
                notification.setRecipientRoleIds(Set.of(Role.ROLE_ADMIN));
                notificationService.create(notification);
                return true;
            } catch (Exception ex) {
                throw new LmsException("An error occurred:" + ex.getMessage());
            }
        }
        throw new LmsException("Could not find Person with id:" + dto.getId());
    }

    public Boolean delete(PersonDto dto) throws LmsException {
        Person entity = repository.findById(dto.getId());
        if (entity != null) {
            try {
                repository.delete(entity);
                return true;
            } catch (Exception ex) {
                throw new LmsException("An error occurred:" + ex.getMessage());
            }
        }
        throw new LmsException("Could not find Person with id:" + dto.getId());
    }

    public Boolean refreshDaysOfLeaves() throws LmsException {
        try {
            List<Person> list = repository.listAll();
            list.forEach(p -> p.setNumberOfLeaves(calculateTotalNumberOfLeaves(p.getDateOfEmployment())));
            repository.persist(list);
            return true;
        } catch (Exception ex) {
            throw new LmsException("An error occurred:" + ex.getMessage());
        }
    }


}
