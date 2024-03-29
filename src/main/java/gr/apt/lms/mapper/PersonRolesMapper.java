package gr.apt.lms.mapper;

import gr.apt.lms.dto.PersonRolesDto;
import gr.apt.lms.persistence.entity.PersonRoles;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface PersonRolesMapper {
    PersonRolesMapper INSTANCE = Mappers.getMapper(PersonRolesMapper.class);

    PersonRoles DtoToEntity(PersonRolesDto dto);

    PersonRolesDto entityToDto(PersonRoles entity);

    List<PersonRolesDto> entitiesToDtos(List<PersonRoles> entities);

}
