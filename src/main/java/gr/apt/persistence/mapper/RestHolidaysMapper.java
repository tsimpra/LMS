package gr.apt.persistence.mapper;

import gr.apt.persistence.dto.RestHolidaysDto;
import gr.apt.persistence.holiday.RestHolidays;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface RestHolidaysMapper {
    RestHolidaysMapper INSTANCE = Mappers.getMapper(RestHolidaysMapper.class);

    RestHolidays DtoToEntity(RestHolidaysDto dto);

    RestHolidaysDto entityToDto(RestHolidays entity);

    List<RestHolidaysDto> entitiesToDtos(List<RestHolidays> entities);
}
