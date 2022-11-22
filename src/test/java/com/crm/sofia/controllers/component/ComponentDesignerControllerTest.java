package com.crm.sofia.controllers.component;


import com.crm.sofia.dto.component.ComponentDTO;
import com.crm.sofia.services.component.ComponentDesignerService;
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
public class ComponentDesignerControllerTest {

    private MockMvc mvc;

    private ComponentDTO dto;

    @InjectMocks
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private ComponentDesignerService componentDesignerService;

    private List<ComponentDTO> componentDTOList;

    @InjectMocks
    private ComponentDesignerController componentDesignerController;

    @BeforeEach
    void setup() {

        this.componentDTOList = new ArrayList<>();
        dto = new ComponentDTO().setName("dummyNameDTO").setDescription("dummyDescriptionDTO");
        this.componentDTOList.add(dto);

        mvc = MockMvcBuilders.standaloneSetup(componentDesignerController)
                .build();
    }

    @Test
    void getListTest() throws Exception {

        given(componentDesignerService.getList()).willReturn(componentDTOList);
        MockHttpServletResponse response = mvc.perform(get("/component-designer")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$[0].name"), "dummyNameDTO");
    }


    @Test
    void getByIdTest() throws Exception {
        given(componentDesignerService.getObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(get("/component-designer/by-id?id=0")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


    @Test
    void postObjectTest() throws Exception {
        given(componentDesignerService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(post("/component-designer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


    @Test
    void putObjectTest() throws Exception {
        given(componentDesignerService.putObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(put("/component-designer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


    @Test
    void deleteObjectTest() throws Exception {
        doNothing().when(componentDesignerService).deleteObject(any());
        MockHttpServletResponse response = mvc.perform(delete("/component-designer?id=0")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }
}
