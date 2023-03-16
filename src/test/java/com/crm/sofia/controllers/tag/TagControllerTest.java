package com.crm.sofia.controllers.tag;

import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.services.tag.TagDesignerService;
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
public class TagControllerTest {

    private MockMvc mvc;

    private TagDTO dto;

    @InjectMocks
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private TagDesignerService tagDesignerService;

    private List<TagDTO> tagDTOList;

    @InjectMocks
    private TagDesignerController tagDesignerController;

    @BeforeEach
    void setup() {

        this.tagDTOList = new ArrayList<>();
        dto = new TagDTO().setTitle("dummyTitleDTO");
        this.tagDTOList.add(dto);

        mvc = MockMvcBuilders.standaloneSetup(tagDesignerController).build();
    }

    @Test
    void getObjectTest() throws Exception {

        given(tagDesignerService.getObject()).willReturn(tagDTOList);
        MockHttpServletResponse response = mvc.perform(get("/tag-designer").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$[0].title"), "dummyTitleDTO");
    }


    @Test
    void getByIdTest() throws Exception {
        given(tagDesignerService.getObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(get("/tag-designer/by-id?id=0").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.title"), "dummyTitleDTO");
    }


    @Test
    void postObjectTest() throws Exception {
        given(tagDesignerService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(post("/tag-designer").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(dto)).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.title"), "dummyTitleDTO");
    }


    @Test
    void putObjectTest() throws Exception {
        given(tagDesignerService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(put("/tag-designer").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(dto)).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.title"), "dummyTitleDTO");
    }


    @Test
    void deleteObjectTest() throws Exception {
        doNothing().when(tagDesignerService).deleteObject(any());
        MockHttpServletResponse response = mvc.perform(delete("/tag-designer?id=0").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(dto)).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }

}
