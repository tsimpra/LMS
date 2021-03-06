package gr.apt.service;

import gr.apt.exception.LmsException;
import gr.apt.persistence.dto.CreateNotificationDto;
import gr.apt.persistence.dto.PersonDto;
import gr.apt.persistence.entity.Person;
import gr.apt.persistence.entity.Role;
import gr.apt.persistence.mapper.PersonMapper;
import gr.apt.repository.PersonRepository;
import gr.apt.service.notification.NotificationService;
import io.quarkus.panache.common.Page;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Set;

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
            entity.setNumberOfLeaves(calculateDaysOfLeaves(dto.getDateOfEmployment()));
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
            list.forEach(p -> p.setNumberOfLeaves(calculateDaysOfLeaves(p.getDateOfEmployment())));
            repository.persist(list);
            return true;
        } catch (Exception ex) {
            throw new LmsException("An error occurred:" + ex.getMessage());
        }
    }

    private Integer calculateDaysOfLeaves(LocalDate dateOfEmployment) {
        Integer leaves = 0;
        Long totalDays = ChronoUnit.DAYS.between(dateOfEmployment,LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER, 31));//compareTo(dateOfEmployment);
        if (totalDays > 25 * 365) {
            leaves = 26;
        } else if (totalDays > 10 * 365) {
            leaves = 25;
        } else if (totalDays > 2 * 365) {
            leaves = 22;
        } else if (totalDays >= 365) {
            leaves = 21;
        } else {
            Integer workingDays = 0;
            for (int i = dateOfEmployment.getDayOfMonth(); i <= dateOfEmployment.getMonth().maxLength(); i++) {
                DayOfWeek day = LocalDate.of(dateOfEmployment.getYear(), dateOfEmployment.getMonth(), i).getDayOfWeek();
                if (!(/*day.equals(DayOfWeek.SATURDAY) || */day.equals(DayOfWeek.SUNDAY))) {
                    workingDays++;
                }
            }
            leaves = (int) Math.round(((12 - dateOfEmployment.getMonthValue()) * 25 + workingDays) * (20. / 12. / 25.));
        }
        return leaves;
    }
}
