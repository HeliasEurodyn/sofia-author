package com.crm.sofia.services.language;

import com.crm.sofia.dto.language.LanguageDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.language.LanguageMapper;
import com.crm.sofia.model.language.Language;
import com.crm.sofia.repository.language.LanguageRepository;
import com.crm.sofia.services.auth.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class LanguageDesignerService {

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private LanguageMapper languageMapper;

    @Autowired
    private JWTService jwtService;

    public List<LanguageDTO> getObject() {
        List<Language> entities = languageRepository.findAll();
        return languageMapper.map(entities);
    }

    public LanguageDTO getObject(String id) {
        Language optionalEntity = languageRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Language Does Not Exist"));

        Language entity = optionalEntity;
        LanguageDTO dto = languageMapper.map(entity);
        return dto;
    }

    public LanguageDTO postObject(LanguageDTO objectDto) {
        Language object = languageMapper.map(objectDto);
        if (objectDto.getId() == null) {
            object.setCreatedOn(Instant.now());
            object.setCreatedBy(jwtService.getUserId());
        }
        object.setModifiedOn(Instant.now());
        object.setModifiedBy(jwtService.getUserId());
        Language savedData = languageRepository.save(object);

        return languageMapper.map(savedData);
    }

    public void deleteObject(String id) {
        Language optionalEntity = languageRepository.findById(id)
                        .orElseThrow(() -> new DoesNotExistException("Language Does Not Exist"));

        languageRepository.deleteById(optionalEntity.getId());
    }

}
