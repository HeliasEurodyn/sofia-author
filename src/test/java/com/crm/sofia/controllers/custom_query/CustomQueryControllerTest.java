package com.crm.sofia.controllers.custom_query;

import com.crm.sofia.dto.custom_query.CustomQueryDTO;
import com.crm.sofia.filters.JWTAuthFilter;
import com.crm.sofia.services.custom_query.CustomQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
public class CustomQueryControllerTest {

    private MockMvc mvc;
    private CustomQueryDTO dto;

    @InjectMocks
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private CustomQueryService customQueryService;

    private List<CustomQueryDTO> customQueryDTOList;

    @Mock
    private JWTAuthFilter filter;

    @InjectMocks
    private CustomQueryDesignerController customQueryDesignerController;

    @BeforeEach
    void setUp() {
        this.customQueryDTOList = new ArrayList<>();
        dto = new CustomQueryDTO();
        dto.setName("Query");
        dto.setQuery("what is the query");
        this.customQueryDTOList.add(dto);

        mvc = MockMvcBuilders.standaloneSetup(customQueryDesignerController)
                .build();
    }

    @Test
    void getObjectTest() throws Exception {

        given(customQueryService.getObject()).willReturn(customQueryDTOList);
        MockHttpServletResponse response = mvc.perform(get("/custom-query-designer")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$[0].name"), "Query");
    }

    @Test
    void getDownloadByIdTest() throws Exception {
        given(customQueryService.getObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(get("/custom-query-designer/by-id?id=0")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "Query");
    }

    @Test
    void postObjectTest() throws Exception {
        given(customQueryService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(post("/custom-query-designer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "Query");
    }


    @Test
    void putObjectTest() throws Exception {
        given(customQueryService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(put("/custom-query-designer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "Query");
    }

    @Test
    void deleteObjectTest() throws Exception {
        doNothing().when(customQueryService).deleteObject(any());
        MockHttpServletResponse response = mvc.perform(delete("/custom-query-designer?id=0")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }

}
