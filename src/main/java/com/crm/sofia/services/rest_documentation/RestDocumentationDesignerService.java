package com.crm.sofia.services.rest_documentation;



import com.crm.sofia.dto.form.FormActionButtonDTO;
import com.crm.sofia.dto.rest_documentation.RestDocumentationDTO;
import com.crm.sofia.dto.rest_documentation.rest_documentation_endpoint.ExcludeEndPointFieldDTO;
import com.crm.sofia.dto.rest_documentation.rest_documentation_endpoint.RestDocumentationEndpointDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.rest_documentation.RestDocumentationMapper;
import com.crm.sofia.model.rest_documentation.RestDocumentation;
import com.crm.sofia.repository.rest_documentation.RestDocumentationRepository;
import com.crm.sofia.services.auth.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.Instant;

import java.util.Comparator;
import java.util.List;

@Service
public class RestDocumentationDesignerService {

    @Autowired
    RestDocumentationRepository restDocumentationRepository;

    @Autowired
    RestDocumentationMapper restDocumentationMapper;

    @Autowired
    JWTService jwtService;

    public RestDocumentationDesignerService() {
    }

    public List<RestDocumentationDTO> getObject() {
        List<RestDocumentation> entities = restDocumentationRepository.findAll();
        return restDocumentationMapper.map(entities);
    }

    public RestDocumentationDTO getObject(String id) {
        RestDocumentation entity = restDocumentationRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("RestDocumentation Does Not Exist"));

        RestDocumentationDTO dto = restDocumentationMapper.map(entity);

        dto.getRestDocumentationEndpoints().sort(
                Comparator.comparingLong(
                        endpoint -> {
                            Long shortOrder = endpoint.getShortOrder();
                            return shortOrder != null ? shortOrder : Long.MIN_VALUE;
                        }
                )
        );

        for (RestDocumentationEndpointDTO endpointDTO : dto.getRestDocumentationEndpoints()) {
            endpointDTO.getExcludeEndPointFields().sort( Comparator.comparingLong(
                    endpoint -> {
                        Long shortOrder = endpoint.getShortOrder();
                        return shortOrder != null ? shortOrder : Long.MIN_VALUE;
                    }
            ));
        }

        return dto;

    }

    public RestDocumentationDTO postObject(RestDocumentationDTO restDocumentationDTO) {

        RestDocumentation restDocumentation = restDocumentationMapper.map(restDocumentationDTO);
        if (restDocumentation.getId() == null) {
            restDocumentation.setCreatedOn(Instant.now());
            restDocumentation.setCreatedBy(jwtService.getUserId());
        }
        restDocumentation.setModifiedOn(Instant.now());
        restDocumentation.setModifiedBy(jwtService.getUserId());

        RestDocumentation savedData = restDocumentationRepository.save(restDocumentation);

        return restDocumentationMapper.map(savedData);
    }

    public void deleteObject(String id) {
        RestDocumentation optionalEntity = restDocumentationRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("RestDocumentation Does Not Exist"));

        restDocumentationRepository.deleteById(optionalEntity.getId());
    }
}