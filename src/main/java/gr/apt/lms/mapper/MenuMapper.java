package gr.apt.lms.mapper;

import gr.apt.lms.dto.MenuDto;
import gr.apt.lms.persistence.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface MenuMapper {

    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

    Menu DtoToEntity(MenuDto dto);

    MenuDto entityToDto(Menu entity);

    List<MenuDto> entitiesToDtos(List<Menu> entities);

}
