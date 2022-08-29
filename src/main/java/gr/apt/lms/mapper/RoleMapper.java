package gr.apt.lms.mapper;

import gr.apt.lms.dto.RoleDto;
import gr.apt.lms.persistence.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    Role DtoToEntity(RoleDto dto);

    RoleDto entityToDto(Role entity);

    List<RoleDto> entitiesToDtos(List<Role> entities);
}
