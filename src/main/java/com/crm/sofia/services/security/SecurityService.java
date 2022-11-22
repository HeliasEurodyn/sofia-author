package com.crm.sofia.services.security;

import com.crm.sofia.dto.security.SecurityDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.security.AccessControlMapper;
import com.crm.sofia.model.security.Security;
import com.crm.sofia.repository.security.SecurityRepository;
import com.crm.sofia.services.auth.JWTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class SecurityService {

    @Autowired
    private AccessControlMapper accessControlMapper;
    @Autowired
    private SecurityRepository securityRepository;
    @Autowired
    private JWTService jwtService;

    public List<SecurityDTO> getObject() {
        List<Security> entities = securityRepository.findAll();
        return accessControlMapper.map(entities);
    }
    public SecurityDTO getObject(String id) {
        Security optionalEntity = securityRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Security Does Not Exist"));

        SecurityDTO dto = accessControlMapper.map(optionalEntity);
        return dto;
    }

    public SecurityDTO postObject(SecurityDTO securityDto) {

        Security security = accessControlMapper.map(securityDto);
        if (securityDto.getId() == null) {
            security.setCreatedOn(Instant.now());
            security.setCreatedBy(jwtService.getUserId());
        }
        security.setModifiedOn(Instant.now());
        security.setModifiedBy(jwtService.getUserId());
        Security savedData = securityRepository.save(security);

        return accessControlMapper.map(savedData);
    }

    public void deleteObject(String id) {
        Security optionalEntity = securityRepository.findById(id)
                        .orElseThrow(() -> new DoesNotExistException("Security Does Not Exist"));

        securityRepository.deleteById(optionalEntity.getId());
    }
}
