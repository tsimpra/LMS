package gr.apt.persistence.mapper;

import gr.apt.persistence.dto.PersonBasicInfoDto;
import gr.apt.persistence.dto.PersonDto;
import gr.apt.persistence.dto.RoleDto;
import gr.apt.persistence.entity.Person;
import gr.apt.persistence.entity.PersonRoles;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "cdi")
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    Person DtoToEntity(PersonDto dto);

    @Mappings({
            @Mapping(source = "personRolesById", target = "roles",qualifiedByName = "getRoles")
    })
    PersonDto entityToDto(Person entity);

    List<PersonDto> entitiesToDtos(List<Person> entities);

    @Named("getRoles")
    static Collection<RoleDto> getRoles(Collection<PersonRoles> personRoles){
        Collection<RoleDto> roles = new ArrayList<>();
        personRoles.forEach(personRole ->
            roles.add( RoleMapper.INSTANCE.entityToDto(personRole.getRoleByRoleId()))
        );
        return roles;
    }

    PersonBasicInfoDto entityToBasicDto(Person entity);

    List<PersonBasicInfoDto> entitiesToBasicDtos(List<Person> entities);

}
