package com.crm.sofia.integration.controllers;

import com.crm.sofia.SofiaApplication;
import com.crm.sofia.controllers.timeline.TimelineDesignerController;
import com.crm.sofia.dto.timeline.TimelineDTO;
import com.crm.sofia.exception.handler.RestResponseEntityExceptionHandler;
import com.crm.sofia.mapper.timeline.TimelineMapper;
import com.crm.sofia.model.timeline.Timeline;
import com.crm.sofia.repository.timeline.TimelineRepository;
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

@EnabledIf(expression = "#{environment['spring.profiles.active'] == 'dev'}", loadContext = true)
@TestPropertySource("/application-test.properties")
@SpringBootTest(classes = {SofiaApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@Transactional
public class TimelineDesignerControllerIntegrationTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Value("${sql.script.create.timeline}")
    private String sqlCreateTimeLine;

    @Value("${sql.script.create.timeline2}")
    private String sqlCreateTimeLine2;

    @Value("${sql.script.delete.timeline}")
    private String sqlDeleteTimeLine;

    @Autowired
    private TimelineDesignerController timelineDesignerController;

    @Autowired
    private TimelineRepository timelineRepository;

    @Autowired
    private TimelineMapper timelineMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static TimelineDTO timelineDTO;

    @BeforeAll
    public static void beforeAll(){
        timelineDTO = new TimelineDTO()
                .setTitle("TestTitle3")
                .setDescription("testDescription3")
                .setQuery("dGVzdFF1ZXJ5Mw==")
                .setIcon("testIcon3")
                .setHasPagination(false)
                .setPageSize(0);
    }

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(timelineDesignerController).setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
        jdbc.execute(sqlCreateTimeLine);
        jdbc.execute(sqlCreateTimeLine2);
    }

    @AfterEach
    public void AfterEach() {
        jdbc.execute(sqlDeleteTimeLine);
    }

    @Test
    @DisplayName("Get Timeline List Http Request ")
    @Order(1)
    public void getTimelineListHttpRequest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/timeline-designer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$",hasSize(2)));
    }

    @Test
    @DisplayName("Create Timeline Http Request ")
    @Order(2)
    public void createTimelineHttpRequest() throws Exception{

        mockMvc.perform(post("/timeline-designer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(timelineDTO)))
                .andExpect(status().isOk());

        List<Timeline> timelineList = timelineRepository.findAll();

        assertEquals(3,timelineList.size());
        assertTrue(timelineList.stream().anyMatch(timeline -> timeline.getTitle().equals("TestTitle3")));
        assertTrue(timelineList.stream().anyMatch(timeline -> timeline.getDescription().equals("testDescription3")));
        assertTrue(timelineList.stream().anyMatch(timeline -> timeline.getQuery().equals("testQuery3")));
        assertTrue(timelineList.stream().anyMatch(timeline -> timeline.getIcon().equals("testIcon3")));

    }


    @Test
    @DisplayName("Update Timeline Http Request ")
    @Order(3)
    public void updateTimelineHttpRequest() throws Exception{

       Timeline timeline1 = timelineRepository.findById("1").get();
       timeline1.setTitle("newTestTitle");
       timeline1.setDescription("newTestDescription");
       timeline1.setQuery("bmV3VGVzdFF1ZXJ5");
       TimelineDTO timelineDTO1 = timelineMapper.map(timeline1);

        mockMvc.perform(put("/timeline-designer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(timelineDTO1)))
                .andExpect(status().isOk());

        List<Timeline> timelineList = timelineRepository.findAll();

        assertEquals(2,timelineList.size());
        assertTrue(timelineList.stream().anyMatch(timeline -> timeline.getTitle().equals("newTestTitle")));
        assertTrue(timelineList.stream().noneMatch(timeline -> timeline.getTitle().equals("testTitle")));
        assertTrue(timelineList.stream().anyMatch(timeline -> timeline.getDescription().equals("newTestDescription")));
        assertTrue(timelineList.stream().noneMatch(timeline -> timeline.getDescription().equals("testDescription")));
        assertTrue(timelineList.stream().anyMatch(timeline -> timeline.getQuery().equals("newTestQuery")));
        assertTrue(timelineList.stream().noneMatch(timeline -> timeline.getQuery().equals("testQuery")));

        Optional<Timeline> timeline2 = timelineRepository.findById("1");
        assertTrue(timeline2.isPresent());


    }


    @Test
    @DisplayName("Get Timeline Http Request ")
    @Order(4)
    public void getTimelineHttpRequest() throws Exception{

        Optional<Timeline> timeline1 = timelineRepository.findById("1");
        assertTrue(timeline1.isPresent());

        mockMvc.perform(MockMvcRequestBuilders.get("/timeline-designer/by-id?id={id}",1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id",is("1")))
                .andExpect(jsonPath("$.title",is("testTitle")))
                .andExpect(jsonPath("$.query",is("dGVzdFF1ZXJ5")))
                .andExpect(jsonPath("$.icon",is("testIcon")));

    }

    @Test
    @DisplayName("Get Timeline Http Request With Empty Response")
    @Order(5)
    public void getTimelineHttpRequestEmptyResponse() throws Exception{

        Optional<Timeline> timeline1 = timelineRepository.findById("0");
        assertFalse(timeline1.isPresent());

        mockMvc.perform(MockMvcRequestBuilders.get("/timeline-designer/by-id?id={id}",0))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code",is("002-1")))
                .andExpect(jsonPath("$.category",is("OBJECT_NOT_FOUND")))
                .andExpect(jsonPath("$.message",is("Timeline Does Not Exist")));

    }

    @Test
    @DisplayName("Delete Timeline Http Request ")
    @Order(6)
    public void deleteTimelineHttpRequest() throws Exception{

        Optional<Timeline> timeline1 = timelineRepository.findById("1");
        assertTrue(timeline1.isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/timeline-designer?id={id}",1))
                .andExpect(status().isOk());

        timeline1 = timelineRepository.findById("1");
        assertFalse(timeline1.isPresent());

    }

    @Test
    @DisplayName("Delete Timeline Http Request With Empty Response")
    @Order(7)
    public void deleteTimelineHttpRequestEmptyResponse() throws Exception{

        Optional<Timeline> timeline1 = timelineRepository.findById("0");
        assertFalse(timeline1.isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/timeline-designer?id={id}",0))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code",is("002-1")))
                .andExpect(jsonPath("$.category",is("OBJECT_NOT_FOUND")))
                .andExpect(jsonPath("$.message",is("Timeline Does Not Exist")));

    }



}
