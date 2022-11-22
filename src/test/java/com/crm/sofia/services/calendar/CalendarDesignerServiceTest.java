package com.crm.sofia.services.calendar;

import com.crm.sofia.dto.calendar.CalendarDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.calendar.CalendarMapper;
import com.crm.sofia.model.calendar.Calendar;
import com.crm.sofia.repository.calendar.CalendarRepository;
import com.crm.sofia.services.auth.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class CalendarDesignerServiceTest {


    @Mock
    private CalendarRepository calendarRepository;

    @InjectMocks
    private CalendarDesignerService calendarDesignerService;

    @Mock
    private JWTService jwtService;

    private List<Calendar> calendarList;

    private Calendar calendar;

    private CalendarDTO calendarDTO;

    private List<CalendarDTO> calendarDTOList;


    @Mock
    private CalendarMapper calendarMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        calendarList = new ArrayList<>();
        calendarDTOList = new ArrayList<>();
        calendar = new Calendar().setTitle("dummyTitle").setQuery("dummyQuery");
        calendarList.add(calendar);
        calendarDTO = new CalendarDTO().setTitle("dummyTitleDTO").setQuery(Base64.getEncoder().encodeToString("dummyQueryDTO".getBytes(StandardCharsets.UTF_8)));
        calendarDTO.setId("1");
        calendarDTOList.add(calendarDTO);

    }

    @Test
    public void getObjectTest() {
        given(calendarRepository.findAll()).willReturn(calendarList);
        given(calendarMapper.map(ArgumentMatchers.any(List.class))).willReturn(List.of(calendarDTO));
        List<CalendarDTO> list = calendarDesignerService.getObject();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getTitle()).isEqualTo("dummyTitleDTO");
    }

    @Test
    public void getObjectByIdTest() {
        given(calendarRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(calendar));
        given(calendarMapper.map(ArgumentMatchers.any(Calendar.class))).willReturn(calendarDTO);
        CalendarDTO dto = calendarDesignerService.getObject("1");
        assertThat(calendarDTO).isNotNull();
        assertThat(calendarDTO.getId().equals("1"));
    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(calendarRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            calendarDesignerService.getObject("1");
        });

        String expectedMessage = "Calendar Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void postObjectTest() {
        given(calendarMapper.map(ArgumentMatchers.any(CalendarDTO.class))).willReturn(calendar);
        given(calendarRepository.save(ArgumentMatchers.any(Calendar.class))).willReturn(calendar);
        calendarDesignerService.postObject(calendarDTO);
        assertThat(calendarDTO).isNotNull();
        assertThat(calendarDTO.getId()).isEqualTo("1");
    }


    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(calendarRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            calendarDesignerService.deleteObject("1");
        });
        String expectedMessage = "Calendar Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }


}
