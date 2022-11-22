package com.crm.sofia.services.form;

import com.crm.sofia.dto.component.ComponentDTO;
import com.crm.sofia.dto.form.FormDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.form.FormMapper;
import com.crm.sofia.model.component.ComponentPersistEntity;
import com.crm.sofia.model.form.FormEntity;
import com.crm.sofia.repository.form.FormRepository;
import com.crm.sofia.services.auth.JWTService;
import com.crm.sofia.services.component.ComponentPersistEntityFieldAssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class FormDesignerServiceTest {

    @Mock
    private FormRepository formRepository;

    @InjectMocks
    private FormDesignerService formDesignerService;

    @Mock
    private ComponentPersistEntityFieldAssignmentService componentPersistEntityFieldAssignmentService;

    @Mock
    private FormJavascriptService formJavascriptService;

    @Mock
    private JWTService jwtService;

    private List<FormEntity> formEntityList;

    private FormEntity formEntity;

    private List<FormDTO> formDTOList;

    private List<ComponentPersistEntity> componentPersistEntityList;


    private FormDTO formDTO;

    @Mock
    private FormMapper formMapper;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        formEntityList = new ArrayList<>();
        formDTOList = new ArrayList<>();
        formEntity = new FormEntity().setName("dummyName").setDescription("dummyDescription");
        formEntityList.add(formEntity);
        formDTO = new FormDTO().setName("dummyNameDTO").setDescription("dummyDescriptionDTO");
        formDTOList.add(formDTO);
        formDTO.setId("1");
        formDTO.setComponent(new ComponentDTO().setComponentPersistEntityList(Collections.emptyList()));
        formDTO.setFormTabs(Collections.emptyList());
        formDTO.setFormPopups(Collections.emptyList());
        formDTO.setFormActionButtons(Collections.emptyList());

    }

    @Test
    public void getObjectTest() {
        given(formRepository.findAllByOrderByModifiedOn()).willReturn(formEntityList);
        given(formMapper.mapEntitiesForList(ArgumentMatchers.any(List.class))).willReturn(formDTOList);
        List<FormDTO> list = formDesignerService.getObject();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getName()).isEqualTo("dummyNameDTO");
    }

    @Test
    public void getObjectByIdTest() {
        given(formRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(formEntity));
        given(formMapper.map(ArgumentMatchers.any(FormEntity.class))).willReturn(formDTO);
        given(componentPersistEntityFieldAssignmentService
                .retrieveFieldAssignments(ArgumentMatchers.any(List.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .willReturn(Collections.emptyList());
        FormDTO dto = formDesignerService.getObject("1");
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(formRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            formDesignerService.getObject("1");
        });

        String expectedMessage = "Form Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void postObjectTest() throws Exception {
        given(formRepository.save(ArgumentMatchers.any(FormEntity.class))).willReturn(formEntity);
        given(formMapper.map(ArgumentMatchers.any(FormDTO.class))).willReturn(formEntity);
        given(formMapper.map(ArgumentMatchers.any(FormEntity.class))).willReturn(formDTO);
        given(formJavascriptService.generateDynamicScript(ArgumentMatchers.any(FormDTO.class))).willReturn("");
        FormDTO dto = formDesignerService.postObject(formDTO);
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }


    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(formRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            formDesignerService.deleteObject("1");
        });
        String expectedMessage = "Form Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
