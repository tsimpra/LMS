package gr.apt.mapper;

import gr.apt.dto.notification.CreateNotificationDto;
import gr.apt.dto.notification.NotificationDto;
import gr.apt.persistence.entity.notification.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    Notification DtoToEntity(CreateNotificationDto dto);

    Notification DtoToEntity(NotificationDto dto);

    NotificationDto entityToDto(Notification entity);

    List<NotificationDto> entitiesToDtos(List<Notification> entities);
}
