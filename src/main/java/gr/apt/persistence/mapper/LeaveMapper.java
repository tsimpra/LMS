package gr.apt.persistence.mapper;

import gr.apt.persistence.dto.ApproveLeaveDto;
import gr.apt.persistence.dto.LeaveDto;
import gr.apt.persistence.dto.PersonBasicInfoDto;
import gr.apt.persistence.entity.Leave;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigInteger;
import java.util.List;

@Mapper(componentModel = "cdi", uses = PersonMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LeaveMapper {
    LeaveMapper INSTANCE = Mappers.getMapper(LeaveMapper.class);

    @Mappings({
            //@Mapping(source = "approvedBy", target = "approvedBy", qualifiedByName = "getBasicPersonId"),
            @Mapping(source = "personId", target = "personId", qualifiedByName = "getBasicPersonId")
    })
    Leave DtoToEntity(LeaveDto dto);

    @Mappings({
            @Mapping(source = "personByApprovedBy", target = "approvedBy"),
            @Mapping(source = "personByPersonId", target = "personId")
    })
    ApproveLeaveDto entityToDto(Leave entity);

    List<ApproveLeaveDto> entitiesToDtos(List<Leave> entities);


    @Named("getBasicPersonId")
    static BigInteger getBasicPersonId(PersonBasicInfoDto dto) {
        if (dto != null) {
            return dto.getId();
        }
        return null;
    }

}
