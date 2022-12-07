package com.crm.sofia.services.business_unit;

import com.crm.sofia.dto.business_unit.BusinessUnitDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.business_unit.BusinessUnitMapper;
import com.crm.sofia.model.business_unit.BusinessUnit;
import com.crm.sofia.repository.business_unit.BusinessUnitRepository;
import com.crm.sofia.services.auth.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class BusinessUnitDesignerService {

    @Autowired
    private BusinessUnitMapper businessUnitMapper;

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    private JWTService jwtService;

    public List<BusinessUnitDTO> getObject() {
        List<BusinessUnitDTO> businessUnitList = businessUnitRepository.getObject();
        return businessUnitList;
    }

    public BusinessUnitDTO getObject(String id) {
        BusinessUnit optionalEntity = businessUnitRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("BusinessUnit Does Not Exist"));

        BusinessUnitDTO dto = businessUnitMapper.map(optionalEntity);

        return dto;
    }

    public BusinessUnitDTO postObject(BusinessUnitDTO businessUnitDTO) {

        BusinessUnit businessUnit = businessUnitMapper.map(businessUnitDTO);
        if (businessUnit.getId() == null) {
            businessUnit.setCreatedOn(Instant.now());
            businessUnit.setCreatedBy(jwtService.getUserId());
        }
        businessUnit.setModifiedOn(Instant.now());
        businessUnit.setModifiedBy(jwtService.getUserId());
        BusinessUnit savedData = businessUnitRepository.save(businessUnit);

        return businessUnitMapper.map(savedData);
    }

    public void deleteObject(String id) {
        BusinessUnit optionalEntity = businessUnitRepository.findById(id)
                        .orElseThrow(() -> new DoesNotExistException("BusinessUnit Does Not Exist"));

        businessUnitRepository.deleteById(optionalEntity.getId());
    }

}
