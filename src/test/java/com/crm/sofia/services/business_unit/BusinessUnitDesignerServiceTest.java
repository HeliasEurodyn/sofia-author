package com.crm.sofia.services.business_unit;

import com.crm.sofia.dto.business_unit.BusinessUnitDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.business_unit.BusinessUnitMapper;
import com.crm.sofia.model.business_unit.BusinessUnit;
import com.crm.sofia.repository.business_unit.BusinessUnitRepository;
import com.crm.sofia.services.auth.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BusinessUnitDesignerServiceTest {

    @Mock
    private BusinessUnitRepository businessUnitRepository;

    @InjectMocks
    private BusinessUnitDesignerService businessUnitDesignerService;

    @Mock
    private JWTService jwtService;

    private List<BusinessUnit> businessUnitList;

    private List<BusinessUnitDTO> businessUnitDTOList;

    private BusinessUnit businessUnit;

    private BusinessUnitDTO businessUnitDTO;

    @Mock
    private BusinessUnitMapper businessUnitMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        businessUnitList = new ArrayList<>();
        businessUnitDTOList = new ArrayList<>();
        businessUnit = new BusinessUnit().setTitle("dummyTitle").setDescription("dummyDescription");
        businessUnitList.add(businessUnit);
        businessUnitDTO = new BusinessUnitDTO().setTitle("dummyTitleDTO").setDescription("dummyDescriptionDTO");
        businessUnitDTO.setId("1");
        businessUnitDTOList.add(businessUnitDTO);
    }

    @Test
    public void getObjectTest() {
        given(businessUnitRepository.findAll()).willReturn(businessUnitList);
        given(businessUnitMapper.map(ArgumentMatchers.any(List.class))).willReturn(businessUnitDTOList);
        List<BusinessUnitDTO> list = businessUnitDesignerService.getObject();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getTitle()).isEqualTo("dummyTitleDTO");
    }


    @Test
    public void getObjectByIdTest() {
        given(businessUnitRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(businessUnit));
        given(businessUnitMapper.map(ArgumentMatchers.any(BusinessUnit.class))).willReturn(businessUnitDTO);
        BusinessUnitDTO dto = businessUnitDesignerService.getObject("1");
        assertThat(businessUnitDTO).isNotNull();
        assertThat(businessUnitDTO.getId().equals("1"));
    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(businessUnitRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            businessUnitDesignerService.getObject("1");
        });

        String expectedMessage = "BusinessUnit Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void postObjectTest() {
        given(businessUnitMapper.map(ArgumentMatchers.any(BusinessUnitDTO.class))).willReturn(businessUnit);
        given(businessUnitRepository.save(ArgumentMatchers.any(BusinessUnit.class))).willReturn(businessUnit);
        businessUnitDesignerService.postObject(businessUnitDTO);
        assertThat(businessUnitDTO).isNotNull();
        assertThat(businessUnitDTO.getId()).isEqualTo("1");
    }


    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(businessUnitRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            businessUnitDesignerService.deleteObject("1");
        });
        String expectedMessage = "BusinessUnit Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

}
