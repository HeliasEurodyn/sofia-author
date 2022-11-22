package com.crm.sofia.controllers.report;

import com.crm.sofia.dto.report.ReportDTO;
import com.crm.sofia.services.report.ReportDesignerService;
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
public class ReportDesignerControllerTest {

    private MockMvc mvc;

    private ReportDTO dto;

    @InjectMocks
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private ReportDesignerService reportDesignerService;

    private List<ReportDTO> reportDTOList;

    @InjectMocks
    private ReportDesignerController reportDesignerController;

    @BeforeEach
    void setup() {

        this.reportDTOList = new ArrayList<>();
        dto = new ReportDTO().setName("dummyNameDTO");
        this.reportDTOList.add(dto);

        mvc = MockMvcBuilders.standaloneSetup(reportDesignerController)
                .build();
    }

    @Test
    void getObjectTest() throws Exception {

        given(reportDesignerService.getObject()).willReturn(reportDTOList);
        MockHttpServletResponse response = mvc.perform(get("/report-designer")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$[0].name"), "dummyNameDTO");
    }


    @Test
    void getByIdTest() throws Exception {
        given(reportDesignerService.getObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(get("/report-designer/by-id?id=0")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


    @Test
    void postObjectTest() throws Exception {
        given(reportDesignerService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(post("/report-designer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


//    @Test
//    void putObjectTest() throws Exception {
//        given(reportDesignerService.postObject(any())).willReturn(dto);
//        MockHttpServletResponse response = mvc.perform(post("/report-designer/report-file")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(dto))
//                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
//        assertEquals(response.getStatus(), HttpStatus.OK.value());
//        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
//    }


    @Test
    void deleteObjectTest() throws Exception {
        doNothing().when(reportDesignerService).deleteObject(any());
        MockHttpServletResponse response = mvc.perform(delete("/report-designer?id=0")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }
}
