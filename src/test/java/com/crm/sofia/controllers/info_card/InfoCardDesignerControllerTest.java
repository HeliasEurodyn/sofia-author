package com.crm.sofia.controllers.info_card;

import com.crm.sofia.dto.info_card.InfoCardDTO;
import com.crm.sofia.services.info_card.InfoCardDesignerService;
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
public class InfoCardDesignerControllerTest {

    private MockMvc mvc;

    private InfoCardDTO dto;

    @InjectMocks
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private InfoCardDesignerService infoCardDesignerService;

    private List<InfoCardDTO> infoCardDTOList;

    @InjectMocks
    private InfoCardDesignerController infoCardDesignerController;

    @BeforeEach
    void setup() {

        this.infoCardDTOList = new ArrayList<>();
        dto = new InfoCardDTO().setTitle("dummyTitleDTO").setQuery("dummyQueryDTO");
        this.infoCardDTOList.add(dto);

        mvc = MockMvcBuilders.standaloneSetup(infoCardDesignerController)
                .build();
    }

    @Test
    void getObjectTest() throws Exception {

        given(infoCardDesignerService.getObject()).willReturn(infoCardDTOList);
        MockHttpServletResponse response = mvc.perform(get("/info-card-designer")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$[0].title"), "dummyTitleDTO");
    }


    @Test
    void getByIdTest() throws Exception {
        given(infoCardDesignerService.getObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(get("/info-card-designer/by-id?id=0")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.title"), "dummyTitleDTO");
    }


    @Test
    void postObjectTest() throws Exception {
        given(infoCardDesignerService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(post("/info-card-designer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.title"), "dummyTitleDTO");
    }


    @Test
    void putObjectTest() throws Exception {
        given(infoCardDesignerService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(put("/info-card-designer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.title"), "dummyTitleDTO");
    }


    @Test
    void deleteObjectTest() throws Exception {
        doNothing().when(infoCardDesignerService).deleteObject(any());
        MockHttpServletResponse response = mvc.perform(delete("/info-card-designer?id=0")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }
}
