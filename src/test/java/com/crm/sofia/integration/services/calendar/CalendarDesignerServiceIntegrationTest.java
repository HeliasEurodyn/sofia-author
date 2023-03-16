package com.crm.sofia.integration.services.calendar;

import com.crm.sofia.SofiaApplication;
import com.crm.sofia.dto.calendar.CalendarDTO;
import com.crm.sofia.exception.common.SofiaException;
import com.crm.sofia.mapper.calendar.CalendarMapper;
import com.crm.sofia.model.calendar.Calendar;
import com.crm.sofia.repository.calendar.CalendarRepository;
import com.crm.sofia.services.calendar.CalendarDesignerService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@EnabledIf(expression = "#{environment['spring.profiles.active'] == 'test'}", loadContext = true)
@TestPropertySource("/application-test.properties")
@SpringBootTest(classes = {SofiaApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class CalendarDesignerServiceIntegrationTest {
    @Autowired
    private JdbcTemplate jdbc;

    @Value("${sql.script.create.calendar}")
    private String sqlCreateCalendar;

    @Value("${sql.script.create.calendar2}")
    private String sqlCreateCalendar2;

    @Value("${sql.script.delete.calendar}")
    private String sqlDeleteCalendar;

    @Autowired
    private CalendarDesignerService calendarDesignerService;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private CalendarMapper calendarMapper;

    @BeforeEach
    void beforeEach() {
        jdbc.execute(sqlCreateCalendar);
        jdbc.execute(sqlCreateCalendar2);
    }

    @AfterEach
    public void AfterEach() {
        jdbc.execute(sqlDeleteCalendar);
    }

    @Test
    @DisplayName("Get Calendar List")
    @Order(1)
    public void getObjectTest() {
        List<CalendarDTO> list = calendarDesignerService.getObject();
        assertFalse(list.isEmpty(), "List should not empty");
        assertEquals(2, list.size());
        assertEquals("testTitle", list.get(0).getTitle());
        assertDoesNotThrow(() -> calendarDesignerService.getObject());

    }

    @Test
    @DisplayName("Get Calendar")
    @Order(2)
    public void getObjectByIdTest() {
        Optional<Calendar> calendar = calendarRepository.findById("1");
        Optional<Calendar> calendar2 = calendarRepository.findById("2");
        Optional<Calendar> calendar3 = calendarRepository.findById("3");

        assertTrue(calendar.isPresent());
        assertTrue(calendar2.isPresent());
        assertFalse(calendar3.isPresent());

        CalendarDTO calendarDTO1 = calendarMapper.map(calendar.get());
        CalendarDTO calendarDTO2 = calendarMapper.map(calendar.get());

        CalendarDTO calendarDTO11 = calendarDesignerService.getObject("1");
        CalendarDTO calendarDTO22 = calendarDesignerService.getObject("2");


        assertNotNull(calendarDTO1);
        assertNotNull(calendarDTO2);
        assertNotNull(calendarDTO11);
        assertNotNull(calendarDTO22);


        assertNotEquals(calendarDTO1.getQuery(), calendarDTO11.getQuery());
        assertEquals("testQuery", calendarDTO1.getQuery());
        assertNotEquals("testQuery", calendarDTO11.getQuery());
        assertEquals("dGVzdFF1ZXJ5", calendarDTO11.getQuery());

        assertDoesNotThrow(() -> calendarDesignerService.getObject("1"));
        assertDoesNotThrow(() -> calendarDesignerService.getObject("2"));
        Exception exception = assertThrows(SofiaException.class, () -> {
            calendarDesignerService.getObject("3");
        });

        String expectedMessage = "Calendar Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    @DisplayName("Delete Calendar")
    @Order(3)
    public void deleteObjectByIdTest() {
        List<Calendar> calendarList = calendarRepository.findAll();
        assertFalse(calendarList.isEmpty());
        assertEquals(2, calendarList.size());

        calendarDesignerService.deleteObject("1");
        Optional<Calendar> calendar = calendarRepository.findById("1");
        assertFalse(calendar.isPresent());

        calendarList = calendarRepository.findAll();
        assertEquals(1, calendarList.size());

        calendarDesignerService.deleteObject("2");
        Optional<Calendar> calendar2 = calendarRepository.findById("2");
        assertFalse(calendar2.isPresent());

        calendarList = calendarRepository.findAll();
        assertEquals(0, calendarList.size());
        assertTrue(calendar.isEmpty());

        Exception exception = assertThrows(SofiaException.class, () -> {
            calendarDesignerService.getObject("1");
        });

        String expectedMessage = "Calendar Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);

        Exception exception2 = assertThrows(SofiaException.class, () -> {
            calendarDesignerService.deleteObject("1");
        });

        actualMessage = exception2.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }


    @Test
    @DisplayName("Post-Put Calendar")
    @Order(4)
    public void postObjectTest() {

        CalendarDTO calendarDTO1 = new CalendarDTO()
                .setTitle("TestTitle3")
                .setDescription("testDescription3")
                .setQuery("dGVzdFF1ZXJ5Mw==")
                .setIcon("testIcon3");


        CalendarDTO calendarDTO2 = calendarDesignerService.getObject("1");
        calendarDTO2.setTitle("newTitle");


        List<Calendar> calendarList = calendarRepository.findAll();
        assertFalse(calendarList.isEmpty());
        assertEquals(2, calendarList.size());

        CalendarDTO calendarDTO3 = calendarDesignerService.postObject(calendarDTO1);
        calendarList = calendarRepository.findAll();
        assertEquals(3, calendarList.size());
        assertEquals("testQuery3", calendarDTO3.getQuery());

        CalendarDTO calendarDTO4 = calendarDesignerService.postObject(calendarDTO2);
        calendarList = calendarRepository.findAll();
        assertEquals(3, calendarList.size());
        assertEquals("newTitle", calendarDTO4.getTitle());
        assertEquals("testQuery", calendarDTO4.getQuery());


    }
}
