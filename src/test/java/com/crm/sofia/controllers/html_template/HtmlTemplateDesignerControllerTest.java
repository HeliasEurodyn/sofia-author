package com.crm.sofia.controllers.html_template;

import com.crm.sofia.dto.html_template.HtmlTemplateDTO;
import com.crm.sofia.services.html_template.HtmlTemplateDesignerService;
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
public class HtmlTemplateDesignerControllerTest {

    private MockMvc mvc;

    private HtmlTemplateDTO dto;

    @InjectMocks
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private HtmlTemplateDesignerService htmlTemplateDesignerService;

    private List<HtmlTemplateDTO> htmlTemplateDTOList;

    @InjectMocks
    private HtmlTemplateDesignerController htmlTemplateDesignerController;

    @BeforeEach
    void setup() {

        this.htmlTemplateDTOList = new ArrayList<>();
        dto = new HtmlTemplateDTO().setTitle("dummyTitleDTO").setHtml("dummyHtmlDTO");
        this.htmlTemplateDTOList.add(dto);

        mvc = MockMvcBuilders.standaloneSetup(htmlTemplateDesignerController).build();
    }

    @Test
    void getObjectTest() throws Exception {

        given(htmlTemplateDesignerService.getObject()).willReturn(htmlTemplateDTOList);
        MockHttpServletResponse response = mvc.perform(get("/html-template-designer").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$[0].title"), "dummyTitleDTO");
    }

    @Test
    void getByIdTest() throws Exception {
        given(htmlTemplateDesignerService.getObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(get("/html-template-designer/by-id?id=0").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.title"), "dummyTitleDTO");
    }


    @Test
    void postObjectTest() throws Exception {
        given(htmlTemplateDesignerService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(post("/html-template-designer").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(dto)).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.title"), "dummyTitleDTO");
    }


    @Test
    void putObjectTest() throws Exception {
        given(htmlTemplateDesignerService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(put("/html-template-designer").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(dto)).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.title"), "dummyTitleDTO");
    }


    @Test
    void deleteObjectTest() throws Exception {
        doNothing().when(htmlTemplateDesignerService).deleteObject(any());
        MockHttpServletResponse response = mvc.perform(delete("/html-template-designer?id=0").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(dto)).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }
}
