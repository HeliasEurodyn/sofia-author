package com.crm.sofia.integration.controllers.calendar;

import com.crm.sofia.SofiaApplication;
import com.crm.sofia.controllers.calendar.CalendarDesignerController;
import com.crm.sofia.dto.calendar.CalendarDTO;
import com.crm.sofia.exception.handler.RestResponseEntityExceptionHandler;
import com.crm.sofia.mapper.calendar.CalendarMapper;
import com.crm.sofia.model.calendar.Calendar;
import com.crm.sofia.repository.calendar.CalendarRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@EnabledIf(expression = "#{environment['spring.profiles.active'] == 'test'}", loadContext = true)
@TestPropertySource("/application-test.properties")
@SpringBootTest(classes = {SofiaApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@Transactional
public class CalendarDesignerControllerIntegrationTest {

    private static CalendarDTO calendarDTO;
    @Autowired
    private JdbcTemplate jdbc;
    @Value("${sql.script.create.calendar}")
    private String sqlCreateCalendar;
    @Value("${sql.script.create.calendar2}")
    private String sqlCreateCalendar2;
    @Value("${sql.script.delete.calendar}")
    private String sqlDeleteCalendar;
    @Autowired
    private CalendarDesignerController calendarDesignerController;
    @Autowired
    private CalendarRepository calendarRepository;
    @Autowired
    private CalendarMapper calendarMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void beforeAll() {
        calendarDTO = new CalendarDTO()
                .setTitle("TestTitle3")
                .setDescription("testDescription3")
                .setQuery("dGVzdFF1ZXJ5Mw==")
                .setIcon("testIcon3");

    }

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(calendarDesignerController).setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
        jdbc.execute(sqlCreateCalendar);
        jdbc.execute(sqlCreateCalendar2);
    }

    @AfterEach
    public void AfterEach() {
        jdbc.execute(sqlDeleteCalendar);
    }

    @Test
    @DisplayName("Get Calendar List Http Request ")
    @Order(1)
    public void getCalendarListHttpRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/calendar-designer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Create Calendar Http Request ")
    @Order(2)
    public void createCalendarHttpRequest() throws Exception {

        mockMvc.perform(post("/calendar-designer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(calendarDTO)))
                .andExpect(status().isOk());

        List<Calendar> calendarList = calendarRepository.findAll();

        assertEquals(3, calendarList.size());
        assertTrue(calendarList.stream().anyMatch(calendar -> calendar.getTitle().equals("TestTitle3")));
        assertTrue(calendarList.stream().anyMatch(calendar -> calendar.getDescription().equals("testDescription3")));
        assertTrue(calendarList.stream().anyMatch(calendar -> calendar.getQuery().equals("testQuery3")));
        assertTrue(calendarList.stream().anyMatch(calendar -> calendar.getIcon().equals("testIcon3")));

    }

    @Test
    @DisplayName("Update Calendar Http Request ")
    @Order(3)
    public void updateCalendarHttpRequest() throws Exception {

        Calendar calendar1 = calendarRepository.findById("1").get();
        calendar1.setTitle("newTestTitle");
        calendar1.setDescription("newTestDescription");
        calendar1.setQuery("bmV3VGVzdFF1ZXJ5");
        CalendarDTO calendarDTO1 = calendarMapper.map(calendar1);

        mockMvc.perform(put("/calendar-designer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(calendarDTO1)))
                .andExpect(status().isOk());

        List<Calendar> calendarList = calendarRepository.findAll();

        assertEquals(2, calendarList.size());
        assertTrue(calendarList.stream().anyMatch(calendar -> calendar.getTitle().equals("newTestTitle")));
        assertTrue(calendarList.stream().noneMatch(calendar -> calendar.getTitle().equals("testTitle")));
        assertTrue(calendarList.stream().anyMatch(calendar -> calendar.getDescription().equals("newTestDescription")));
        assertTrue(calendarList.stream().noneMatch(calendar -> calendar.getDescription().equals("testDescription")));
        assertTrue(calendarList.stream().anyMatch(calendar -> calendar.getQuery().equals("newTestQuery")));
        assertTrue(calendarList.stream().noneMatch(calendar -> calendar.getQuery().equals("testQuery")));

        Optional<Calendar> calendar2 = calendarRepository.findById("1");
        assertTrue(calendar2.isPresent());


    }

    @Test
    @DisplayName("Get Calendar Http Request ")
    @Order(4)
    public void getCalendarHttpRequest() throws Exception {

        Optional<Calendar> calendar1 = calendarRepository.findById("1");
        assertTrue(calendar1.isPresent());

        mockMvc.perform(MockMvcRequestBuilders.get("/calendar-designer/by-id?id={id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.title", is("testTitle")))
                .andExpect(jsonPath("$.query", is("dGVzdFF1ZXJ5")))
                .andExpect(jsonPath("$.icon", is("testIcon")));

    }

    @Test
    @DisplayName("Get Calendar Http Request With Empty Response")
    @Order(5)
    public void getCalendarHttpRequestEmptyResponse() throws Exception {

        Optional<Calendar> calendar1 = calendarRepository.findById("0");
        assertFalse(calendar1.isPresent());

        mockMvc.perform(MockMvcRequestBuilders.get("/calendar-designer/by-id?id={id}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code", is("002-1")))
                .andExpect(jsonPath("$.category", is("OBJECT_NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("Calendar Does Not Exist")));

    }

    @Test
    @DisplayName("Delete Calendar Http Request ")
    @Order(6)
    public void deleteCalendarHttpRequest() throws Exception {

        Optional<Calendar> calendar1 = calendarRepository.findById("1");
        assertTrue(calendar1.isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/calendar-designer?id={id}", 1))
                .andExpect(status().isOk());

        calendar1 = calendarRepository.findById("1");
        assertFalse(calendar1.isPresent());

    }

    @Test
    @DisplayName("Delete Calendar Http Request With Empty Response")
    @Order(7)
    public void deleteCalendarHttpRequestEmptyResponse() throws Exception {

        Optional<Calendar> calendar1 = calendarRepository.findById("0");
        assertFalse(calendar1.isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/calendar-designer?id={id}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code", is("002-1")))
                .andExpect(jsonPath("$.category", is("OBJECT_NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("Timeline Does Not Exist")));

    }
}
