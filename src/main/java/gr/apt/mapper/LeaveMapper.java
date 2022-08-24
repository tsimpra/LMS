package gr.apt.mapper;

import gr.apt.dto.leave.ApproveLeaveDto;
import gr.apt.dto.leave.LeaveDto;
import gr.apt.dto.person.PersonBasicInfoDto;
import gr.apt.persistence.entity.Leave;
import gr.apt.repository.PersonRepository;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import javax.enterprise.inject.spi.CDI;
import java.math.BigInteger;
import java.util.List;

@Mapper(componentModel = "cdi", uses = PersonMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LeaveMapper {
    LeaveMapper INSTANCE = Mappers.getMapper(LeaveMapper.class);

    Leave DtoToEntity(LeaveDto dto);

    @Mappings({
            @Mapping(source = "approvedBy", target = "approvedBy", qualifiedByName = "getApprovedByPersonInfo")
    })
    ApproveLeaveDto entityToDto(Leave entity);

    List<ApproveLeaveDto> entitiesToDtos(List<Leave> entities);

    @Named("getApprovedByPersonInfo")
    static PersonBasicInfoDto getApprovedByPersonInfo(BigInteger id) {
        PersonRepository personRepository = CDI.current().select(PersonRepository.class).get();
        if (personRepository != null)
            return PersonMapper.INSTANCE.entityToBasicDto(personRepository.getPersonBasicInfo(id).singleResult());
        else
            return null;
    }

}
