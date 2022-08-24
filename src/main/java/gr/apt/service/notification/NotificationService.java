package gr.apt.service.notification;

import gr.apt.dto.notification.CreateNotificationDto;
import gr.apt.dto.notification.NotificationDto;
import gr.apt.exception.LmsException;
import gr.apt.mapper.NotificationMapper;
import gr.apt.persistence.entity.Person;
import gr.apt.persistence.entity.Role;
import gr.apt.persistence.entity.notification.Notification;
import gr.apt.persistence.entity.notification.NotificationRecipientPersons;
import gr.apt.persistence.entity.notification.NotificationRecipientRoles;
import gr.apt.persistence.entity.notification.NotificationViewers;
import gr.apt.repository.PersonRepository;
import gr.apt.repository.RoleRepository;
import gr.apt.repository.notification.NotificationRecipientPersonsRepository;
import gr.apt.repository.notification.NotificationRecipientRolesRepository;
import gr.apt.repository.notification.NotificationRepository;
import gr.apt.repository.notification.NotificationViewersRepository;
import io.quarkus.panache.common.Page;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
@Transactional
public class NotificationService {
    @Inject
    NotificationRepository repository;
    @Inject
    NotificationRecipientPersonsRepository recipientPersonsRepository;
    @Inject
    NotificationRecipientRolesRepository recipientRolesRepository;
    @Inject
    NotificationViewersRepository viewersRepository;
    @Inject
    PersonRepository personRepository;
    @Inject
    RoleRepository roleRepository;
    @Inject
    NotificationMapper mapper;

    public List<NotificationDto> findAll(BigInteger personId, Integer index, Integer size) throws LmsException {
        try {
            Set<BigInteger> roleIds = new HashSet<>();
            Set<BigInteger> notifIds = new HashSet<>();
            Person person = personRepository.findById(personId);
            person.getPersonRolesById().forEach(role -> roleIds.add(role.getRoleId()));
            List<NotificationRecipientRoles> recipientRoles = recipientRolesRepository.find("roleId", roleIds).list();
            if (recipientRoles != null && !recipientRoles.isEmpty()) {
                recipientRoles.forEach(entry -> notifIds.add(entry.getNotifId()));
            }
            List<NotificationRecipientPersons> recipientPersons = recipientPersonsRepository.find("personId", person.getId()).list();
            if (recipientPersons != null && !recipientPersons.isEmpty()) {
                recipientPersons.forEach(entry -> notifIds.add(entry.getNotifId()));
            }
            if (index != null && size != null) {
                Page page = Page.of(index, size);
                return mapper.entitiesToDtos(repository.find("id", notifIds).page(page).list());
            }
            return mapper.entitiesToDtos(repository.find("id", notifIds).list());
        } catch (Exception ex) {
            throw new LmsException(ex.getMessage());
        }
    }

    public void readAll(BigInteger personId, List<NotificationDto> dtos) throws LmsException {
        try {
            if (dtos != null && !dtos.isEmpty()) {
                dtos.forEach(dto -> {
                    try {
                        read(personId, dto.getId());
                    } catch (LmsException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception ex) {
            throw new LmsException(ex.getMessage());
        }
    }

    public NotificationDto read(BigInteger personId, BigInteger id) throws LmsException {
        try {
            Notification notification = repository.findById(id);
            AtomicReference<Boolean> isRead = new AtomicReference<>(false);
            notification.getViewers().forEach(viewer -> {
                if (viewer.getPersonId().equals(personId)) {
                    isRead.set(true);
                }
            });
            if (!isRead.get()) {
                viewersRepository.persist(new NotificationViewers(personId, id));
            }
            return mapper.entityToDto(notification);
        } catch (Exception ex) {
            throw new LmsException(ex.getMessage());
        }
    }

    public Boolean create(CreateNotificationDto dto) throws LmsException {
        try {
            Notification entity = mapper.DtoToEntity(dto);
            repository.persistAndFlush(entity);
            repository.getEntityManager().refresh(entity);
            if (dto.getRecipientPersonId() != null) {
                recipientPersonsRepository.persist(new NotificationRecipientPersons(dto.getRecipientPersonId(), entity.getId()));
            }
            if (dto.getRecipientRoleIds() != null && !dto.getRecipientRoleIds().isEmpty()) {
                dto.getRecipientRoleIds().forEach(roleId -> {
                    recipientRolesRepository.persist(new NotificationRecipientRoles(roleId, entity.getId()));
                });
            }
            return true;
        } catch (Exception ex) {
            throw new LmsException(ex.getMessage());
        }
    }

    public Boolean createGeneralNotification(NotificationDto dto) throws LmsException {
        try {
            Notification entity = mapper.DtoToEntity(dto);
            repository.persistAndFlush(entity);
            repository.getEntityManager().refresh(entity);
            List<Role> roles = roleRepository.listAll();
            roles.forEach(role -> {
                recipientRolesRepository.persist(new NotificationRecipientRoles(role.getId(), entity.getId()));
            });
            return true;
        } catch (Exception ex) {
            throw new LmsException(ex.getMessage());
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
