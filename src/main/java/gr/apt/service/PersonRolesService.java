package gr.apt.service;

import gr.apt.persistence.dto.PersonRolesDto;
import gr.apt.persistence.entity.PersonRoles;
import gr.apt.persistence.mapper.PersonRolesMapper;
import gr.apt.repository.PersonRolesRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@ApplicationScoped
@Transactional
public class PersonRolesService {
    @Inject
    PersonRolesRepository repository;
    @Inject
    PersonRolesMapper mapper;

    public List<PersonRolesDto> findAll() {
        return mapper.entitiesToDtos(repository.listAll());
    }

    public PersonRolesDto findById(BigInteger id) {
        return mapper.entityToDto(repository.findById(id));
    }

    public Boolean create(PersonRolesDto dto) {
        PersonRoles entity = mapper.DtoToEntity(dto);
        repository.persistAndFlush(entity);
        return true;
    }

    public Boolean update(PersonRolesDto dto) {
        PersonRoles entity = repository.findById(dto.getId());
        if (entity != null) {
            entity = mapper.DtoToEntity(dto);
            repository.persistAndFlush(entity);
            return true;
        }
        return false;
    }

    public Boolean delete(PersonRolesDto dto) {
        PersonRoles entity = repository.findById(dto.getId());
        if (entity != null) {
            repository.delete(entity);
            return true;
        }
        return false;
    }
}
