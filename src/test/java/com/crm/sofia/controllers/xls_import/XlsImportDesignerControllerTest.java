package com.crm.sofia.controllers.xls_import;

import com.crm.sofia.controllers.sse_notification.SseNotificationTemplateController;
import com.crm.sofia.dto.sse_notification.SseNotificationDTO;
import com.crm.sofia.dto.xls_import.XlsImportDTO;
import com.crm.sofia.services.sse_notification.SseNotificationTemplateService;
import com.crm.sofia.services.xls_import.XlsImportDesignerService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@ExtendWith(MockitoExtension.class)
public class XlsImportDesignerControllerTest {
    private MockMvc mvc;

    private XlsImportDTO dto;

    @InjectMocks
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private XlsImportDesignerService xlsImportDesignerService;

    private List<XlsImportDTO> xlsImportDTOList;

    @InjectMocks
    private XlsImportDesignerController xlsImportDesignerController;

    @BeforeEach
    void setUp() {
        this.xlsImportDTOList = new ArrayList<>();
        dto = new XlsImportDTO().setName("dummyNameDTO");
        this.xlsImportDTOList.add(dto);

        mvc = MockMvcBuilders.standaloneSetup(xlsImportDesignerController).build();
    }

    @Test
    void getObjectTest() throws Exception {

        given(xlsImportDesignerService.getObject()).willReturn(xlsImportDTOList);
        MockHttpServletResponse response = mvc.perform(get("/xls-import-designer").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$[0].name"), "dummyNameDTO");
    }

    @Test
    void getByIdTest() throws Exception {
        given(xlsImportDesignerService.getObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(get("/xls-import-designer/by-id?id=0").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }

    @Test
    void postObjectTest() throws Exception {
        given(xlsImportDesignerService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(post("/xls-import-designer").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(dto)).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }

    @Test
    void putObjectTest() throws Exception {
        given(xlsImportDesignerService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(put("/xls-import-designer").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(dto)).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }

    @Test
    void deleteObjectTest() throws Exception {
        doNothing().when(xlsImportDesignerService).deleteObject(any());
        MockHttpServletResponse response = mvc.perform(delete("/xls-import-designer?id=0").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(dto)).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }
}
