package gr.apt.mapper;

import gr.apt.dto.leave.LeaveDto;
import gr.apt.dto.person.PersonBasicInfoDto;
import gr.apt.persistence.entity.Leave;
import gr.apt.repository.PersonRepository;
import gr.apt.utils.LeaveUtilsKt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import javax.enterprise.inject.spi.CDI;
import java.math.BigInteger;
import java.util.List;

@Mapper(componentModel = "cdi", imports = LeaveUtilsKt.class)
public interface LeaveMapper {
    LeaveMapper INSTANCE = Mappers.getMapper(LeaveMapper.class);

    @Mappings({
            @Mapping(source = "approvedBy", target = "approvedBy", ignore = true),
            @Mapping(source = "approvedDate", target = "approvedDate", ignore = true),
            @Mapping(source = "approved", target = "approved", ignore = true)
    })
    Leave DtoToEntity(LeaveDto dto);

    @Mappings({
            @Mapping(source = "approvedBy", target = "approvedBy", qualifiedByName = "getApprovedByPersonInfo"),
            @Mapping(source = "approvedDate", target = "approvedDate"),
            @Mapping(source = "approved", target = "approved"),
            @Mapping(target = "numberOfRequestedLeaves", expression = "java(LeaveUtilsKt.getNumberOfRequestedLeaves(entity))")
    })
    LeaveDto entityToDto(Leave entity);

    List<LeaveDto> entitiesToDtos(List<Leave> entities);

    @Named("getApprovedByPersonInfo")
    static PersonBasicInfoDto getApprovedByPersonInfo(BigInteger id) {
        PersonRepository personRepository = CDI.current().select(PersonRepository.class).get();
        if (personRepository != null)
            return PersonMapper.INSTANCE.entityToBasicDto(personRepository.getPersonBasicInfo(id).singleResult());
        else
            return null;
    }

}
