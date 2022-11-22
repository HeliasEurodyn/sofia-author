package com.crm.sofia.controllers.list;


import com.crm.sofia.dto.list.ListDTO;
import com.crm.sofia.services.list.ListDesignerService;
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
public class ListDesignerControllerTest {

    private MockMvc mvc;

    private ListDTO dto;

    @InjectMocks
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private ListDesignerService listDesignerService;

    private List<ListDTO> listDTOList;

    @InjectMocks
    private ListDesignerController listDesignerController;

    @BeforeEach
    void setup() {

        this.listDTOList = new ArrayList<>();
        dto = new ListDTO().setName("dummyNameDTO");
        this.listDTOList.add(dto);

        mvc = MockMvcBuilders.standaloneSetup(listDesignerController)
                .build();
    }

    @Test
    void getObjectTest() throws Exception {

        given(listDesignerService.getObject()).willReturn(listDTOList);
        MockHttpServletResponse response = mvc.perform(get("/list-designer")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$[0].name"), "dummyNameDTO");
    }


    @Test
    void getByIdTest() throws Exception {
        given(listDesignerService.getObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(get("/list-designer/by-id?id=0")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


    @Test
    void postObjectTest() throws Exception {
        given(listDesignerService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(post("/list-designer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


    @Test
    void putObjectTest() throws Exception {
        given(listDesignerService.putObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(put("/list-designer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


    @Test
    void deleteObjectTest() throws Exception {
        doNothing().when(listDesignerService).deleteObject(any());
        MockHttpServletResponse response = mvc.perform(delete("/list-designer?id=0")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }
}
