package gr.apt.service;

import gr.apt.dto.RestHolidaysDto;
import gr.apt.mapper.RestHolidaysMapper;
import gr.apt.persistence.holiday.RestHolidays;
import gr.apt.repository.RestHolidaysRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@ApplicationScoped
@Transactional
public class RestHolidaysService {
    @Inject
    RestHolidaysRepository repository;
    @Inject
    RestHolidaysMapper mapper;

    public List<RestHolidaysDto> findAll() {
        return mapper.entitiesToDtos(repository.listAll());
    }

    public RestHolidaysDto findById(BigInteger id) {
        return mapper.entityToDto(repository.findById(id));
    }

    public Boolean create(RestHolidaysDto dto) {
        RestHolidays entity = mapper.DtoToEntity(dto);
        repository.persistAndFlush(entity);
        return true;
    }

    public Boolean update(RestHolidaysDto dto) {
        RestHolidays entity = repository.findById(dto.getId());
        if (entity != null) {
            entity = mapper.DtoToEntity(dto);
            repository.persistAndFlush(entity);
            return true;
        }
        return false;
    }

    public Boolean delete(RestHolidaysDto dto) {
        RestHolidays entity = repository.findById(dto.getId());
        if (entity != null) {
            repository.delete(entity);
            return true;
        }
        return false;
    }
}
