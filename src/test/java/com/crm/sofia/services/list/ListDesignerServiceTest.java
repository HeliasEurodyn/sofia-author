package com.crm.sofia.services.list;


import com.crm.sofia.dto.component.ComponentDTO;
import com.crm.sofia.dto.list.ListDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.list.ListMapper;
import com.crm.sofia.model.component.ComponentPersistEntity;
import com.crm.sofia.model.list.ListEntity;
import com.crm.sofia.repository.list.ListRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class ListDesignerServiceTest {

    @Mock
    private ListRepository listRepository;

    @InjectMocks
    private ListDesignerService listDesignerService;

    @Mock
    private ListJavascriptService listJavascriptService;

    @Mock
    private JWTService jwtService;

    private List<ListEntity> listEntityList;

    private List<ListDTO> listDTOList;

    private ListEntity listEntity;

    private ListDTO listDTO;

    private List<ComponentPersistEntity> componentPersistEntityList;

    @Mock
    private ListMapper listMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        listEntityList = new ArrayList<>();
        listDTOList = new ArrayList<>();
        listEntity = new ListEntity().setName("dummyName");
        listEntityList.add(listEntity);
        listDTO = new ListDTO().setName("dummyNameDTO");
        listDTOList.add(listDTO);
        listDTO.setId("1");
        listDTO.setComponent(new ComponentDTO().setComponentPersistEntityList(Collections.emptyList()));
        listDTO.setListComponentFilterFieldList(Collections.emptyList());
        listDTO.setListComponentActionFieldList(Collections.emptyList());
        listDTO.setListComponentColumnFieldList(Collections.emptyList());
        listDTO.setListComponentLeftGroupFieldList(Collections.emptyList());
        listDTO.setListComponentOrderByFieldList(Collections.emptyList());
        listDTO.setListComponentTopGroupFieldList(Collections.emptyList());


    }

    @Test
    public void getObjectTest() {
        given(listRepository.findAllByOrderByModifiedOn()).willReturn(Collections.singletonList(listEntity));
        given(listMapper.mapEntitiesForList(ArgumentMatchers.any(List.class))).willReturn(listDTOList);
        List<ListDTO> list = listDesignerService.getObject();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getName()).isEqualTo("dummyNameDTO");
    }

    @Test
    public void getObjectByIdTest() {
        given(listRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(listEntity));
        given(listMapper.mapList(ArgumentMatchers.any(ListEntity.class))).willReturn(listDTO);
        ListDTO dto = listDesignerService.getObject("1");
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(listRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            listDesignerService.getObject("1");
        });

        String expectedMessage = "ListEntity Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void postObjectTest() throws Exception {
        given(listRepository.save(ArgumentMatchers.any(ListEntity.class))).willReturn(listEntity);
        given(listMapper.mapListDTO(ArgumentMatchers.any(ListDTO.class))).willReturn(listEntity);
        given(listMapper.map(ArgumentMatchers.any(ListEntity.class))).willReturn(listDTO);
        ListDTO dto = listDesignerService.postObject(listDTO);
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }


    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(listRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            listDesignerService.deleteObject("1");
        });
        String expectedMessage = "List Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
