package com.crm.sofia.controllers.calendar;


import com.crm.sofia.dto.appview.controllers.calendar.CalendarDesignerController;
import com.crm.sofia.dto.calendar.CalendarDTO;
import com.crm.sofia.dto.timeline.TimelineDTO;
import com.crm.sofia.services.calendar.CalendarDesignerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
public class CalendarDesignerControllerTest {


    private MockMvc mvc;

    private CalendarDTO dto;

    @InjectMocks
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private CalendarDesignerService calendarDesignerService;

    private List<CalendarDTO> calendarDTOList;

    @InjectMocks
    private CalendarDesignerController calendarDesignerController;

    @BeforeEach
    void setup(){

        this.calendarDTOList = new ArrayList<>();
        dto = new CalendarDTO().setTitle("dummyTitleDTO").setQuery("dummyQueryDTO");
        this.calendarDTOList.add(dto);

        mvc = MockMvcBuilders.standaloneSetup(calendarDesignerController)
                .build();
    }


    @Test
    void getObjectTest() throws Exception {

        given(calendarDesignerService.getObject()).willReturn(calendarDTOList);
        MockHttpServletResponse response = mvc.perform(get("/calendar-designer")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$[0].title")  ,"dummyTitleDTO");
    }


    @Test
    void getByIdTest() throws Exception {
        given(calendarDesignerService.getObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(get("/calendar-designer/by-id?id=0")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.title")  ,"dummyTitleDTO");
    }


    @Test
    void postObjectTest() throws Exception {
        given(calendarDesignerService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(post("/calendar-designer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.title")  ,"dummyTitleDTO");
    }


    @Test
    void putObjectTest() throws Exception {
        given(calendarDesignerService.postObject(any())).willReturn(dto);
        MockHttpServletResponse response = mvc.perform(put("/calendar-designer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(JsonPath.parse(response.getContentAsString()).read("$.title")  ,"dummyTitleDTO");
    }


    @Test
    void deleteObjectTest() throws Exception {
        doNothing().when(calendarDesignerService).deleteObject(any());
        MockHttpServletResponse response = mvc.perform(delete("/calendar-designer?id=0")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }








}
