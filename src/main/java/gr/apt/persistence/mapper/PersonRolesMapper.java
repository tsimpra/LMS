package gr.apt.persistence.mapper;

import gr.apt.persistence.dto.PersonBasicInfoDto;
import gr.apt.persistence.dto.PersonDto;
import gr.apt.persistence.dto.PersonRolesDto;
import gr.apt.persistence.dto.RoleDto;
import gr.apt.persistence.entity.PersonRoles;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigInteger;
import java.util.List;

@Mapper(componentModel = "cdi", uses = {RoleMapper.class, PersonMapper.class},injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PersonRolesMapper {
    PersonRolesMapper INSTANCE = Mappers.getMapper(PersonRolesMapper.class);

    @Mappings({
            @Mapping(source = "roleId", target = "roleId", qualifiedByName = "getRoleId"),
            @Mapping(source = "personId", target = "personId", qualifiedByName = "getBasicPersonId")
    })
    PersonRoles DtoToEntity(PersonRolesDto dto);

    @Mappings({
            @Mapping(source = "roleByRoleId", target = "roleId"),
            @Mapping(source = "personByPersonId", target = "personId")
    })
    PersonRolesDto entityToDto(PersonRoles entity);

    List<PersonRolesDto> entitiesToDtos(List<PersonRoles> entities);

    @Named("getBasicPersonId")
    static BigInteger getBasicPersonId(PersonBasicInfoDto dto) {
        if(dto != null) {
            return dto.getId();
        }
        return null;
    }

    @Named("getRoleId")
    static BigInteger getRoleId(RoleDto dto) {
        if(dto != null) {
            return dto.getId();
        }
        return null;
    }
}
