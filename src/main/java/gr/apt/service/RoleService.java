package gr.apt.service;

import gr.apt.persistence.dto.RoleDto;
import gr.apt.persistence.entity.Role;
import gr.apt.persistence.mapper.RoleMapper;
import gr.apt.repository.RoleRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@ApplicationScoped
@Transactional
public class RoleService {
    @Inject
    RoleRepository repository;
    @Inject
    RoleMapper mapper;

    public List<RoleDto> findAll() {
        return mapper.entitiesToDtos(repository.listAll());
    }

    public RoleDto findById(BigInteger id) {
        return mapper.entityToDto(repository.findById(id));
    }

    public Boolean create(RoleDto dto) {
        Role entity = mapper.DtoToEntity(dto);
        repository.persistAndFlush(entity);
        return true;
    }

    public Boolean update(RoleDto dto) {
        Role entity = repository.findById(dto.getId());
        if (entity != null) {
            entity = mapper.DtoToEntity(dto);
            repository.persistAndFlush(entity);
            return true;
        }
        return false;
    }

    public Boolean delete(RoleDto dto) {
        Role entity = repository.findById(dto.getId());
        if (entity != null) {
            repository.delete(entity);
            return true;
        }
        return false;
    }
}
