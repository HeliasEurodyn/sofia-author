package com.crm.sofia.integration.services;

import com.crm.sofia.SofiaApplication;
import com.crm.sofia.dto.timeline.TimelineDTO;
import com.crm.sofia.exception.common.SofiaException;
import com.crm.sofia.mapper.timeline.TimelineMapper;
import com.crm.sofia.model.timeline.Timeline;
import com.crm.sofia.repository.timeline.TimelineRepository;
import com.crm.sofia.services.timeline.TimelineDesignerService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@IfProfileValue(name = "spring.profiles.active", values = {"dev"})
@TestPropertySource("/application-test.properties")
@SpringBootTest(classes = {SofiaApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class TimelineDesignerServiceIntegrationTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Value("${sql.script.create.timeline}")
    private String sqlCreateTimeLine;

    @Value("${sql.script.create.timeline2}")
    private String sqlCreateTimeLine2;

    @Value("${sql.script.delete.timeline}")
    private String sqlDeleteTimeLine;

    @Autowired
    private TimelineDesignerService timelineDesignerService;

    @Autowired
    private TimelineRepository timelineRepository;

    @Autowired
    private TimelineMapper timelineMapper;

    @BeforeEach
    void beforeEach() {
        jdbc.execute(sqlCreateTimeLine);
        jdbc.execute(sqlCreateTimeLine2);
    }

    @AfterEach
    public void AfterEach() {
        jdbc.execute(sqlDeleteTimeLine);
    }


    @Test
    @DisplayName("Get Timeline List")
    @Order(1)
    @EnabledIfSystemProperty(named = "deployment", matches = "DEV")
    public void getObjectTest() {
        List<TimelineDTO> list = timelineDesignerService.getObject();
        assertFalse(list.isEmpty(), "List should not empty");
        assertEquals(2, list.size());
        assertEquals("testTitle", list.get(0).getTitle());
        assertDoesNotThrow(() -> timelineDesignerService.getObject());

    }

    @Test
    @DisplayName("Get Timeline")
    @Order(2)
    @EnabledIfSystemProperty(named = "deployment", matches = "DEV")
    public void getObjectByIdTest() {
        Optional<Timeline> timeline = timelineRepository.findById("1");
        Optional<Timeline> timeline2 = timelineRepository.findById("2");
        Optional<Timeline> timeline3 = timelineRepository.findById("3");

        assertTrue(timeline.isPresent());
        assertTrue(timeline2.isPresent());
        assertFalse(timeline3.isPresent());

        TimelineDTO timelineDTO1 = timelineMapper.map(timeline.get());
        TimelineDTO timelineDTO2 = timelineMapper.map(timeline.get());

        TimelineDTO timelineDTO11 = timelineDesignerService.getObject("1");
        TimelineDTO timelineDTO22 = timelineDesignerService.getObject("2");


        assertNotNull(timelineDTO1);
        assertNotNull(timelineDTO2);
        assertNotNull(timelineDTO11);
        assertNotNull(timelineDTO22);


        assertNotEquals(timelineDTO1.getQuery(), timelineDTO11.getQuery());
        assertEquals("testQuery", timelineDTO1.getQuery());
        assertNotEquals("testQuery", timelineDTO11.getQuery());
        assertEquals("dGVzdFF1ZXJ5", timelineDTO11.getQuery());

        assertDoesNotThrow(() -> timelineDesignerService.getObject("1"));
        assertDoesNotThrow(() -> timelineDesignerService.getObject("2"));
        Exception exception = assertThrows(SofiaException.class, () -> {
            timelineDesignerService.getObject("3");
        });

        String expectedMessage = "Timeline Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    @DisplayName("Delete Timeline")
    @Order(3)
    @EnabledIfSystemProperty(named = "deployment", matches = "DEV")
    public void deleteObjectByIdTest() {
        List<Timeline> timelineList = timelineRepository.findAll();
        assertFalse(timelineList.isEmpty());
        assertEquals(2, timelineList.size());

        timelineDesignerService.deleteObject("1");
        Optional<Timeline> timeline = timelineRepository.findById("1");
        assertFalse(timeline.isPresent());

        timelineList = timelineRepository.findAll();
        assertEquals(1, timelineList.size());

        timelineDesignerService.deleteObject("2");
        Optional<Timeline> timeline2 = timelineRepository.findById("2");
        assertFalse(timeline2.isPresent());

        timelineList = timelineRepository.findAll();
        assertEquals(0, timelineList.size());
        assertTrue(timeline.isEmpty());

        Exception exception = assertThrows(SofiaException.class, () -> {
            timelineDesignerService.getObject("1");
        });

        String expectedMessage = "Timeline Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);

        Exception exception2 = assertThrows(SofiaException.class, () -> {
            timelineDesignerService.deleteObject("1");
        });

        actualMessage = exception2.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }


    @Test
    @DisplayName("Post-Put Timeline")
    @Order(4)
    @EnabledIfSystemProperty(named = "deployment", matches = "DEV")
    public void postObjectTest() {

        TimelineDTO timelineDTO1 = new TimelineDTO()
                .setTitle("TestTitle3")
                .setDescription("testDescription3")
                .setQuery("dGVzdFF1ZXJ5Mw==")
                .setIcon("testIcon3")
                .setHasPagination(false)
                .setPageSize(0);

        TimelineDTO timelineDTO2 = timelineDesignerService.getObject("1");
        timelineDTO2.setTitle("newTitle");


        List<Timeline> timelineList = timelineRepository.findAll();
        assertFalse(timelineList.isEmpty());
        assertEquals(2, timelineList.size());

        TimelineDTO timelineDTO3 = timelineDesignerService.postObject(timelineDTO1);
        timelineList = timelineRepository.findAll();
        assertEquals(3, timelineList.size());
        assertEquals("testQuery3", timelineDTO3.getQuery());

        TimelineDTO timelineDTO4 = timelineDesignerService.postObject(timelineDTO2);
        timelineList = timelineRepository.findAll();
        assertEquals(3, timelineList.size());
        assertEquals("newTitle", timelineDTO4.getTitle());
        assertEquals("testQuery", timelineDTO4.getQuery());


    }


}
