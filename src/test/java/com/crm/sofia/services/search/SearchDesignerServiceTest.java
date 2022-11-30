package com.crm.sofia.services.search;

import com.crm.sofia.dto.search.SearchDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.search.SearchMapper;
import com.crm.sofia.model.search.Search;
import com.crm.sofia.repository.search.SearchRepository;
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
public class SearchDesignerServiceTest {

    @Mock
    private SearchRepository searchRepository;

    @InjectMocks
    private SearchDesignerService searchDesignerService;

    @Mock
    private JWTService jwtService;

    private List<Search> searchList;

    private Search search;

    private SearchDTO searchDTO;

    @Mock
    private SearchMapper searchMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        searchList = new ArrayList<>();
        search = new Search().setName("dummyName");
        searchList.add(search);
        searchDTO = new SearchDTO().setName("dummyNameDTO");
        searchDTO.setId("1");

    }

    @Test
    public void getObjectTest() {
        given(searchRepository.getObject()).willReturn(Collections.singletonList(searchDTO));
        List<SearchDTO> list = searchDesignerService.getObject();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getName()).isEqualTo("dummyNameDTO");
    }

    @Test
    public void getObjectByIdTest() {
        given(searchRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(search));
        given(searchMapper.map(ArgumentMatchers.any(Search.class))).willReturn(searchDTO);
        SearchDTO dto = searchDesignerService.getObject("1");
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));

    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(searchRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            searchDesignerService.getObject("1");
        });

        String expectedMessage = "Search Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void postObjectTest() {
        given(searchRepository.save(ArgumentMatchers.any(Search.class))).willReturn(search);
        given(searchMapper.map(ArgumentMatchers.any(SearchDTO.class))).willReturn(search);
        given(searchMapper.map(ArgumentMatchers.any(Search.class))).willReturn(searchDTO);
        SearchDTO dto = searchDesignerService.postObject(searchDTO);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo("1");
    }


    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(searchRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            searchDesignerService.deleteObject("1");
        });
        String expectedMessage = "Search Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
