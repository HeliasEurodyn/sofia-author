package com.crm.sofia.services.user;

import com.crm.sofia.dto.user.RoleDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.user.RoleMapper;
import com.crm.sofia.model.user.Role;
import com.crm.sofia.repository.user.RoleRepository;
import com.crm.sofia.services.auth.JWTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RoleService {

    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;
    private final JWTService jwtService;

    public RoleService(RoleMapper roleMapper,
                       RoleRepository roleRepository,
                       JWTService jwtService) {
        this.roleMapper = roleMapper;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
    }

    public List<RoleDTO> getObject() {return roleRepository.getObject();}

    public RoleDTO getObject(String id)  {
        Optional<Role> optionalEntity = roleRepository.findById(id);
        if (!optionalEntity.isPresent()) {
            throw new DoesNotExistException("Role Does Not Exist");
        }
        Role entity = optionalEntity.get();
        RoleDTO dto = roleMapper.map(entity);
        return dto;
    }

    @Transactional
    public RoleDTO postObject(RoleDTO roleDTO) {
        Role role = roleMapper.map(roleDTO);

        if (roleDTO.getId() == null) {
            role.setCreatedOn(Instant.now());
            role.setCreatedBy(jwtService.getUserId());
        }
        role.setModifiedOn(Instant.now());
        role.setModifiedBy(jwtService.getUserId());
        Role savedData = roleRepository.save(role);

        return roleMapper.map(savedData);
    }

    public void deleteObject(String id) {
        Optional<Role> optionalEntity = roleRepository.findById(id);
        if (!optionalEntity.isPresent()) {
            throw new DoesNotExistException("Role Does Not Exist");
        }
        roleRepository.deleteById(id);
    }

}
