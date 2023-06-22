package com.crm.sofia.services.rule;

import com.crm.sofia.dto.rule.RuleSettingsDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.rule.RuleSettingsMapper;
import com.crm.sofia.model.rule.RuleSettings;
import com.crm.sofia.repository.rule.RuleSettingsRepository;
import com.crm.sofia.services.auth.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class RuleDesignerService {

    @Autowired
    private RuleSettingsRepository ruleSettingsRepository;

    @Autowired
    private RuleSettingsMapper ruleSettingsMapper;

    @Autowired
    private JWTService jwtService;


    public List<RuleSettingsDTO> getObject() {
        List<RuleSettingsDTO> list = this.ruleSettingsRepository.getObject();
        return list;
    }

    public RuleSettingsDTO getObject(String id) {

        RuleSettings entity = this.ruleSettingsRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Rule Does Not Exist"));

        return this.ruleSettingsMapper.map(entity);
    }

    public void deleteObject(String id) {
        RuleSettings optionalEntity = this.ruleSettingsRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Rule Does Not Exist"));

        this.ruleSettingsRepository.deleteById(optionalEntity.getId());
    }

    @Transactional
    public RuleSettingsDTO postObject(RuleSettingsDTO ruleSettingsDTO) {
        RuleSettings component = this.ruleSettingsMapper.map(ruleSettingsDTO);
        component.setCreatedOn(Instant.now());
        component.setModifiedOn(Instant.now());
        component.setCreatedBy(jwtService.getUserId());
        component.setModifiedBy(jwtService.getUserId());

        RuleSettings createdComponent = this.ruleSettingsRepository.save(component);
        return this.ruleSettingsMapper.map(createdComponent);
    }

    @Transactional
    @Modifying
    public RuleSettingsDTO putObject(RuleSettingsDTO ruleSettingsDTO) {
        RuleSettings entity = ruleSettingsMapper.map(ruleSettingsDTO);

        entity.setModifiedOn(Instant.now());
        entity.setModifiedBy(jwtService.getUserId());

        RuleSettings createdEntity = this.ruleSettingsRepository.save(entity);
        return this.ruleSettingsMapper.map(createdEntity);
    }
}
