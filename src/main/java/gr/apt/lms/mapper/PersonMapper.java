package gr.apt.lms.mapper;

import gr.apt.lms.dto.RoleDto;
import gr.apt.lms.dto.person.PersonBasicInfoDto;
import gr.apt.lms.dto.person.PersonDto;
import gr.apt.lms.metamodel.dto.PersonDto_;
import gr.apt.lms.metamodel.entity.Person_;
import gr.apt.lms.persistence.entity.Person;
import gr.apt.lms.repository.RoleRepository;
import gr.apt.lms.utils.LeaveUtilsKt;
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
            @Mapping(source = Person_.ID, target = PersonDto_.ROLES, qualifiedByName = "getRoles"),
            @Mapping(target = PersonDto_.USED_LEAVES, expression = "java(LeaveUtilsKt.getUsedLeaves(entity))"),
            @Mapping(target = PersonDto_.REMAINING_LEAVES, expression = "java(LeaveUtilsKt.getRemainingLeaves(entity))")
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
