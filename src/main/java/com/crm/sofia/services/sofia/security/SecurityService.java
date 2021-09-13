package com.crm.sofia.services.sofia.security;

import com.crm.sofia.dto.sofia.security.SecurityDTO;
import com.crm.sofia.mapper.sofia.security.SecurityMapper;
import com.crm.sofia.model.sofia.security.Security;
import com.crm.sofia.repository.sofia.security.SecurityRepository;
import com.crm.sofia.services.sofia.auth.JWTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SecurityService {

    @Autowired
    private SecurityMapper securityMapper;
    @Autowired
    private SecurityRepository securityRepository;
    @Autowired
    private JWTService jwtService;

    public List<SecurityDTO> getObject() {
        List<Security> entities = securityRepository.findAll();
        return securityMapper.map(entities);
    }
    public SecurityDTO getObject(Long id) {
        Optional<Security> optionalEntity = securityRepository.findById(id);
        if (!optionalEntity.isPresent()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Object does not exist");
        }
        Security entity = optionalEntity.get();
        SecurityDTO dto = securityMapper.map(entity);
        return dto;
    }

    public SecurityDTO postObject(SecurityDTO securityDto) {

        Security security = securityMapper.map(securityDto);
        if (securityDto.getId() == null) {
            security.setCreatedOn(Instant.now());
            security.setCreatedBy(jwtService.getUserId());
        }
        security.setModifiedOn(Instant.now());
        security.setModifiedBy(jwtService.getUserId());
        Security savedData = securityRepository.save(security);

        return securityMapper.map(savedData);
    }

    public void deleteObject(Long id) {
        Optional<Security> optionalEntity = securityRepository.findById(id);
        if (!optionalEntity.isPresent()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Object does not exist");
        }
        securityRepository.deleteById(optionalEntity.get().getId());
    }
}