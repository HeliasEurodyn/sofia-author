package com.crm.sofia.services.timeline;

import com.crm.sofia.dto.timeline.TimelineDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.timeline.TimelineMapper;
import com.crm.sofia.model.timeline.Timeline;
import com.crm.sofia.repository.timeline.TimelineRepository;
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
public class TimelineDesignerServiceTest {

    @Mock
    private TimelineRepository timelineRepository;

    @InjectMocks
    private TimelineDesignerService timelineDesignerService;

    @Mock
    private JWTService jwtService;

    private List<Timeline> timelineList;

    private Timeline timeline;
    private TimelineDTO timelineDTO;

    @Mock
    private TimelineMapper timelineMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        timelineList = new ArrayList<>();
        timeline = new Timeline().setTitle("dummyTitle").setQuery("dummyQuery");
        timelineList.add(timeline);
        timelineDTO = new TimelineDTO().setTitle("dummyTitleDTO").setQuery(Base64.getEncoder().encodeToString("dummyQueryDTO".getBytes(StandardCharsets.UTF_8)));
        timelineDTO.setId("1");
    }

    @Test
    public void getObjectTest() {
        given(timelineRepository.findAll()).willReturn(timelineList);
        given(timelineMapper.mapEntitiesForList(ArgumentMatchers.any(List.class))).willReturn(List.of(timelineDTO));
        List<TimelineDTO> list = timelineDesignerService.getObject();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getTitle()).isEqualTo("dummyTitleDTO");
    }

    @Test
    public void getObjectByIdTest() {
        given(timelineRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(timeline));
        given(timelineMapper.map(ArgumentMatchers.any(Timeline.class))).willReturn(timelineDTO);
        TimelineDTO dto = timelineDesignerService.getObject("1");
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(timelineRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            timelineDesignerService.getObject("1");
        });

        String expectedMessage = "Timeline Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void postObjectTest() {
        given(timelineMapper.map(ArgumentMatchers.any(TimelineDTO.class))).willReturn(timeline);
        given(timelineRepository.save(ArgumentMatchers.any(Timeline.class))).willReturn(timeline);
        given(timelineMapper.map(ArgumentMatchers.any(Timeline.class))).willReturn(timelineDTO);
        TimelineDTO dto = timelineDesignerService.postObject(timelineDTO);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo("1");
    }


    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(timelineRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            timelineDesignerService.deleteObject("1");
        });
        String expectedMessage = "Timeline Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }


}
