package com.crm.sofia.controllers.menu;

import com.crm.sofia.dto.menu.MenuDTO;
import com.crm.sofia.services.menu.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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

import static com.jayway.jsonpath.JsonPath.parse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@ExtendWith(MockitoExtension.class)
public class MenuControllerTest {
    private MockMvc mvc;

    private MenuDTO dto;

    @InjectMocks
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private MenuService menuService;

    private List<MenuDTO> menuDTOList;

    @InjectMocks
    private MenuController menuController;

    @BeforeEach
    void setup() {

        this.menuDTOList = new ArrayList<>();
        dto = new MenuDTO().setName("dummyNameDTO");
        this.menuDTOList.add(dto);

        mvc = MockMvcBuilders.standaloneSetup(menuController).build();
    }

    @Test
    void getObjectTest() throws Exception {

        given(menuService.getObject()).willReturn(menuDTOList);
        MockHttpServletResponse response = mvc.perform(get("/menu").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(parse(response.getContentAsString()).read("$[0].name"), "dummyNameDTO");
    }


    @Test
    void getByIdTest() throws Exception {
        given(menuService.getObject(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(get("/menu/by-id?id=0").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


    @Test
    void postObjectTest() throws Exception {
        given(menuService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(post("/menu").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(dto)).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


    @Test
    void putObjectTest() throws Exception {
        given(menuService.putObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(put("/menu").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(dto)).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(parse(response.getContentAsString()).read("$.name"), "dummyNameDTO");
    }


    @Test
    void deleteObjectTest() throws Exception {
        doNothing().when(menuService).deleteObject(any());
        MockHttpServletResponse response = mvc.perform(delete("/menu?id=0").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(dto)).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }
}
