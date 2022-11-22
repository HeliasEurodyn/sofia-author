package com.crm.sofia.services.component;

import com.crm.sofia.dto.component.ComponentDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.component.ComponentMapper;
import com.crm.sofia.model.component.Component;
import com.crm.sofia.repository.component.ComponentRepository;
import com.crm.sofia.services.auth.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class ComponentDesignerServiceTest {

    @Mock
    private ComponentRepository componentRepository;

    @InjectMocks
    private ComponentDesignerService componentDesignerService;

    @Mock
    private JWTService jwtService;

    private List<Component> componentList;

    private Component component;

    private ComponentDTO componentDTO;

    @Mock
    private ComponentMapper componentMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        componentList = new ArrayList<>();
        component = new Component().setName("dummyName").setDescription("dummyDescription");
        componentList.add(component);
        componentDTO = new ComponentDTO().setName("dummyNameDTO").setDescription(Base64.getEncoder().encodeToString("dummyDescriptionDTO".getBytes(StandardCharsets.UTF_8)));
        componentDTO.setId("1");
    }

    @Test
    public void getListTest() {
        given(componentRepository.findAll()).willReturn(componentList);
        given(componentMapper.map(ArgumentMatchers.any(List.class))).willReturn(List.of(componentDTO));
        List<ComponentDTO> list = componentDesignerService.getList();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getName()).isEqualTo("dummyNameDTO");
    }

    @Test
    public void getObjectByIdTest() {
        given(componentRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(component));
        given(componentMapper.map(ArgumentMatchers.any(Component.class))).willReturn(componentDTO);
        ComponentDTO dto = componentDesignerService.getObject("1");
        assertThat(componentDTO).isNotNull();
        assertThat(componentDTO.getId().equals("1"));
    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(componentRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            componentDesignerService.getObject("1");
        });

        String expectedMessage = "Component Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void postObjectTest() {
        lenient().when(componentMapper.map(ArgumentMatchers.any(ComponentDTO.class))).thenReturn(component);
        lenient().when(componentRepository.save(ArgumentMatchers.any(Component.class))).thenReturn(component);
        componentDesignerService.postObject(componentDTO);
        assertThat(componentDTO).isNotNull();
        assertThat(componentDTO.getId().equals("1"));
    }


    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(componentRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            componentDesignerService.deleteObject("1");
        });
        String expectedMessage = "Component Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
