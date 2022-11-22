package com.crm.sofia.services.xls_import;

import com.crm.sofia.dto.xls_import.XlsImportDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.xls_import.XlsImportMapper;
import com.crm.sofia.model.xls_import.XlsImport;
import com.crm.sofia.repository.xls_import.XlsImportRepository;
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
public class XlsImportDesignerServiceTest {

    @Mock
    private XlsImportRepository xlsImportRepository;

    @InjectMocks
    private XlsImportDesignerService xlsImportDesignerService;

    @Mock
    private JWTService jwtService;

    private List<XlsImport> xlsImportList;

    private XlsImport xlsImport;

    private XlsImportDTO xlsImportDTO;

    @Mock
    private XlsImportMapper xlsImportMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        xlsImportList = new ArrayList<>();
        xlsImport = new XlsImport().setName("dummyName");
        xlsImport.setId("1");
        xlsImportList.add(xlsImport);
        xlsImportDTO = new XlsImportDTO().setName("dummyNameDTO");
        xlsImportDTO.setId("1");
    }

    @Test
    public void getObjectTest() {
        given(xlsImportRepository.findAll()).willReturn(xlsImportList);
        given(xlsImportMapper.map(ArgumentMatchers.any(List.class))).willReturn(List.of(xlsImportDTO));
        List<XlsImportDTO> list = xlsImportDesignerService.getObject();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getName()).isEqualTo("dummyNameDTO");
    }


    @Test
    public void getObjectByIdTest() {
        given(xlsImportRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(xlsImport));
        given(xlsImportMapper.map(ArgumentMatchers.any(XlsImport.class))).willReturn(xlsImportDTO);
        XlsImportDTO dto = xlsImportDesignerService.getObject("1");
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(xlsImportRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            xlsImportDesignerService.getObject("1");
        });

        String expectedMessage = "XlsImport Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void postObjectTest() {
        given(xlsImportMapper.map(ArgumentMatchers.any(XlsImportDTO.class))).willReturn(xlsImport);
        given(xlsImportRepository.save(ArgumentMatchers.any(XlsImport.class))).willReturn(xlsImport);
        given(xlsImportMapper.map(ArgumentMatchers.any(XlsImport.class))).willReturn(xlsImportDTO);
        XlsImportDTO dto = xlsImportDesignerService.postObject(xlsImportDTO);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo("1");
    }


    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(xlsImportRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            xlsImportDesignerService.deleteObject("1");
        });
        String expectedMessage = "XlsImport Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
