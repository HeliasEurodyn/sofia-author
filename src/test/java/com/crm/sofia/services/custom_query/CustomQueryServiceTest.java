package com.crm.sofia.services.custom_query;

import com.crm.sofia.dto.custom_query.CustomQueryDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.custom_query.CustomQueryMapper;
import com.crm.sofia.model.custom_query.CustomQuery;
import com.crm.sofia.repository.custom_query.CustomQueryRepository;
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
public class CustomQueryServiceTest {
    @Mock
    private CustomQueryRepository customQueryRepository;
    @InjectMocks
    private CustomQueryService customQueryService;

    @Mock
    private JWTService jwtService;
    private List<CustomQuery> customQueryList;

    private CustomQuery customQuery;
    private CustomQueryDTO customQueryDto;

    @Mock
    private CustomQueryMapper customQueryMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        customQueryList = new ArrayList<>();
        customQuery = new CustomQuery();
        customQueryDto = new CustomQueryDTO();
        customQuery.setQuery("Query");
        customQuery.setName("dummy");
        customQueryList.add(customQuery);
        customQueryDto.setName("dummy");
        customQueryDto.setQuery("LOTR");
        customQueryDto.setId("1");
    }


    @Test
    public void getObjectTest() {
        given(customQueryRepository.getObject()).willReturn(Collections.singletonList(customQueryDto));
        List<CustomQueryDTO> list = customQueryService.getObject();
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getName()).isEqualTo("dummy");
    }

    @Test
    public void getObjectByIdTest() {
        given(customQueryRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(customQuery));
        given(customQueryMapper.map(ArgumentMatchers.any(CustomQuery.class))).willReturn(customQueryDto);
        CustomQueryDTO dto = customQueryService.getObject("1");
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(customQueryRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            customQueryService.getObject("1");
        });

        String expectedMessage = "CustomQuery Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void postObjectTest() {
        given(customQueryMapper.map(ArgumentMatchers.any(CustomQueryDTO.class))).willReturn(customQuery);
        given(customQueryRepository.save(ArgumentMatchers.any(CustomQuery.class))).willReturn(customQuery);
        given(customQueryMapper.map(ArgumentMatchers.any(CustomQuery.class))).willReturn(customQueryDto);
        CustomQueryDTO dto = customQueryService.postObject(customQueryDto);
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }


    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(customQueryRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            customQueryService.deleteObject("6L");
        });
        String expectedMessage = "CustomQuery Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }


}
