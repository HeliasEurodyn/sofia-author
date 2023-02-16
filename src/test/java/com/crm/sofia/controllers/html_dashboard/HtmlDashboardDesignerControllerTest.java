package com.crm.sofia.controllers.html_dashboard;


import com.crm.sofia.dto.html_dashboard.HtmlDashboardDTO;
import com.crm.sofia.services.html_dashboard.HtmlDashboardDesignerService;
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
public class HtmlDashboardDesignerControllerTest {

    private MockMvc mvc;

    private HtmlDashboardDTO dto;

    @InjectMocks
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private HtmlDashboardDesignerService htmlDashboardDesignerService;

    private List<HtmlDashboardDTO> htmlDashboardDTOList;

    @InjectMocks
    private HtmlDashboardDesignerController htmlDashboardDesignerController;

    @BeforeEach
    void setup() {

        this.htmlDashboardDTOList = new ArrayList<>();
        dto = new HtmlDashboardDTO().setName("dummyNameDTO");
        dto.setId("1");
        this.htmlDashboardDTOList.add(dto);

        mvc = MockMvcBuilders.standaloneSetup(htmlDashboardDesignerController)
                .build();
    }

    @Test
    void getObjectTest() throws Exception {

        given(htmlDashboardDesignerService.getObject()).willReturn(htmlDashboardDTOList);
        MockHttpServletResponse response = mvc.perform(get("/html-dashboard-designer")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$[0].name"), "dummyNameDTO");
    }


    @Test
    void getByIdTest() throws Exception {
        given(htmlDashboardDesignerService.getObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(get("/html-dashboard-designer/by-id?id=0")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


    @Test
    void postObjectTest() throws Exception {
        given(htmlDashboardDesignerService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(post("/html-dashboard-designer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


    @Test
    void putObjectTest() throws Exception {
        given(htmlDashboardDesignerService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(put("/html-dashboard-designer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


    @Test
    void deleteObjectTest() throws Exception {
        doNothing().when(htmlDashboardDesignerService).deleteObject(any());
        MockHttpServletResponse response = mvc.perform(delete("/html-dashboard-designer?id=0")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }
}
