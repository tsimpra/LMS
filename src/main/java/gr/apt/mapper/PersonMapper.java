package gr.apt.mapper;

import gr.apt.dto.RoleDto;
import gr.apt.dto.person.PersonBasicInfoDto;
import gr.apt.dto.person.PersonDto;
import gr.apt.persistence.entity.Person;
import gr.apt.repository.RoleRepository;
import gr.apt.utils.LeaveUtilsKt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import javax.enterprise.inject.spi.CDI;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "cdi", imports = LeaveUtilsKt.class)
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    Person DtoToEntity(PersonDto dto);

    @Mappings({
            @Mapping(source = "id", target = "roles", qualifiedByName = "getRoles"),
            @Mapping(target = "usedLeaves", expression = "java(LeaveUtilsKt.getUsedLeaves(entity))"),
            @Mapping(target = "remainingLeaves", expression = "java(LeaveUtilsKt.getRemainingLeaves(entity))")
    })
    PersonDto entityToDto(Person entity);

    List<PersonDto> entitiesToDtos(List<Person> entities);

    @Named("getRoles")
    static Collection<RoleDto> getRoles(BigInteger personId){
        RoleRepository roleRepository = CDI.current().select(RoleRepository.class).get();
        if(roleRepository!=null) {
            return RoleMapper.INSTANCE.entitiesToDtos(roleRepository.getPersonRoles(personId));
        }
        return null;
    }

    PersonBasicInfoDto entityToBasicDto(Person entity);

    List<PersonBasicInfoDto> entitiesToBasicDtos(List<Person> entities);

}
