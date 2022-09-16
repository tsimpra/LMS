package gr.apt.lms.mapper;

import gr.apt.lms.dto.leave.LeaveDto;
import gr.apt.lms.dto.person.PersonBasicInfoDto;
import gr.apt.lms.metamodel.dto.LeaveDto_;
import gr.apt.lms.metamodel.entity.Leave_;
import gr.apt.lms.persistence.entity.Leave;
import gr.apt.lms.repository.PersonRepository;
import gr.apt.lms.utils.LeaveUtilsKt;
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
            @Mapping(source = LeaveDto_.APPROVED_BY, target = Leave_.APPROVED_BY, ignore = true),
            @Mapping(source = LeaveDto_.APPROVED_DATE, target = Leave_.APPROVED_DATE, ignore = true),
            @Mapping(source = LeaveDto_.APPROVED, target = Leave_.APPROVED, ignore = true)
    })
    Leave DtoToEntity(LeaveDto dto);

    @Mappings({
            @Mapping(source = Leave_.APPROVED_BY, target = LeaveDto_.APPROVED_BY, qualifiedByName = "getApprovedByPersonInfo"),
            @Mapping(source = Leave_.APPROVED_DATE, target = LeaveDto_.APPROVED_DATE),
            @Mapping(source = Leave_.APPROVED, target = LeaveDto_.APPROVED),
            @Mapping(target = LeaveDto_.NUMBER_OF_REQUESTED_LEAVES, expression = "java(LeaveUtilsKt.getNumberOfRequestedLeaves(entity))")
    })
    LeaveDto entityToDto(Leave entity);

    List<LeaveDto> entitiesToDtos(List<Leave> entities);

    @Named("getApprovedByPersonInfo")
    static PersonBasicInfoDto getApprovedByPersonInfo(BigInteger id) {
        PersonRepository personRepository = CDI.current().select(PersonRepository.class).get();
        if (personRepository != null && id != null)
            return PersonMapper.INSTANCE.entityToBasicDto(personRepository.getPersonBasicInfo(id));
        else
            return null;
    }

}
