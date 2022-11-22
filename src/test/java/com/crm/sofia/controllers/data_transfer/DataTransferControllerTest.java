package com.crm.sofia.controllers.data_transfer;


import com.crm.sofia.dto.data_transfer.DataTransferDTO;
import com.crm.sofia.services.data_transfer.DataTransferService;
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
public class DataTransferControllerTest {

    private MockMvc mvc;

    private DataTransferDTO dto;

    @InjectMocks
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private DataTransferService dataTransferService;

    private List<DataTransferDTO> dataTransferDTOList;

    @InjectMocks
    private DataTransferController dataTransferController;

    @BeforeEach
    void setup() {

        this.dataTransferDTOList = new ArrayList<>();
        dto = new DataTransferDTO().setName("dummyNameDTO").setDescription("dummyDescriptionDTO");
        this.dataTransferDTOList.add(dto);

        mvc = MockMvcBuilders.standaloneSetup(dataTransferController)
                .build();
    }

    @Test
    void getObjectTest() throws Exception {

        given(dataTransferService.getObject()).willReturn(dataTransferDTOList);
        MockHttpServletResponse response = mvc.perform(get("/data-transfer")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$[0].name"), "dummyNameDTO");
    }


    @Test
    void getByIdTest() throws Exception {
        given(dataTransferService.getObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(get("/data-transfer/by-id?id=0")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


    @Test
    void postObjectTest() throws Exception {
        given(dataTransferService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(post("/data-transfer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


    @Test
    void putObjectTest() throws Exception {
        given(dataTransferService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(put("/data-transfer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


    @Test
    void deleteObjectTest() throws Exception {
        doNothing().when(dataTransferService).deleteObject(any());
        MockHttpServletResponse response = mvc.perform(delete("/data-transfer?id=0")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }
}
