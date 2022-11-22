package com.crm.sofia.controllers.dashboard;


import com.crm.sofia.dto.dashboard.DashboardDTO;
import com.crm.sofia.services.dashboard.DashboardDesignerService;
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
public class DashboardDesignerControllerTest {

    private MockMvc mvc;

    private DashboardDTO dto;

    @InjectMocks
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private DashboardDesignerService dashboardDesignerService;

    private List<DashboardDTO> dashboardDTOList;

    @InjectMocks
    private DashboardDesignerController dashboardDesignerController;

    @BeforeEach
    void setup() {

        this.dashboardDTOList = new ArrayList<>();
        dto = new DashboardDTO().setDescription("dummyDescriptionDTO");
        this.dashboardDTOList.add(dto);

        mvc = MockMvcBuilders.standaloneSetup(dashboardDesignerController)
                .build();
    }

    @Test
    void getObjectTest() throws Exception {

        given(dashboardDesignerService.getObject()).willReturn(dashboardDTOList);
        MockHttpServletResponse response = mvc.perform(get("/dashboard-designer")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$[0].description"), "dummyDescriptionDTO");
    }


    @Test
    void getByIdTest() throws Exception {
        given(dashboardDesignerService.getObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(get("/dashboard-designer/by-id?id=0")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.description"), "dummyDescriptionDTO");
    }


    @Test
    void postObjectTest() throws Exception {
        given(dashboardDesignerService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(post("/dashboard-designer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.description"), "dummyDescriptionDTO");
    }


    @Test
    void putObjectTest() throws Exception {
        given(dashboardDesignerService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(put("/dashboard-designer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.description"), "dummyDescriptionDTO");
    }


    @Test
    void deleteObjectTest() throws Exception {
        doNothing().when(dashboardDesignerService).deleteObject(any());
        MockHttpServletResponse response = mvc.perform(delete("/dashboard-designer?id=0")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }
}
