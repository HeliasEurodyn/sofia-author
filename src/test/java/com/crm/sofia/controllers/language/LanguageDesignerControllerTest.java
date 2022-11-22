package com.crm.sofia.controllers.language;


import com.crm.sofia.dto.language.LanguageDTO;
import com.crm.sofia.services.language.LanguageDesignerService;
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
public class LanguageDesignerControllerTest {
    private MockMvc mvc;

    private LanguageDTO dto;

    @InjectMocks
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private LanguageDesignerService languageDesignerService;

    private List<LanguageDTO> languageDTOList;

    @InjectMocks
    private LanguageDesignerController languageDesignerController;

    @BeforeEach
    void setup() {

        this.languageDTOList = new ArrayList<>();
        dto = new LanguageDTO().setName("dummyNameDTO");
        this.languageDTOList.add(dto);

        mvc = MockMvcBuilders.standaloneSetup(languageDesignerController)
                .build();
    }

    @Test
    void getObjectTest() throws Exception {

        given(languageDesignerService.getObject()).willReturn(languageDTOList);
        MockHttpServletResponse response = mvc.perform(get("/language-designer")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$[0].name"), "dummyNameDTO");
    }


    @Test
    void getByIdTest() throws Exception {
        given(languageDesignerService.getObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(get("/language-designer/by-id?id=0")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


    @Test
    void postObjectTest() throws Exception {
        given(languageDesignerService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(post("/language-designer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


    @Test
    void putObjectTest() throws Exception {
        given(languageDesignerService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(put("/language-designer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


    @Test
    void deleteObjectTest() throws Exception {
        doNothing().when(languageDesignerService).deleteObject(any());
        MockHttpServletResponse response = mvc.perform(delete("/language-designer?id=0")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }
}
