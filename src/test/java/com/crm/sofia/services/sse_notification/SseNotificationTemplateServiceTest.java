package com.crm.sofia.services.sse_notification;

import com.crm.sofia.dto.sse_notification.SseNotificationDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.sse_notification.SseNotificationMapper;
import com.crm.sofia.model.sse_notification.SseNotification;
import com.crm.sofia.repository.sse_notification.SseNotificationRepository;
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
public class SseNotificationTemplateServiceTest {

    @Mock
    private SseNotificationRepository sseNotificationRepository;

    @InjectMocks
    private SseNotificationTemplateService sseNotificationTemplateService;

    @Mock
    private JWTService jwtService;

    private List<SseNotification> sseNotificationList;

    private SseNotification sseNotification;

    private SseNotificationDTO sseNotificationDTO;

    @Mock
    private SseNotificationMapper sseNotificationMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        sseNotificationList = new ArrayList<>();
        sseNotification = new SseNotification().setTitle("dummyTitle").setQuery("dummyQuery");
        sseNotificationList.add(sseNotification);
        sseNotificationDTO = new SseNotificationDTO().setTitle("dummyTitleDTO").setQuery(Base64.getEncoder().encodeToString("dummyQueryDTO".getBytes(StandardCharsets.UTF_8)));
        sseNotificationDTO.setId("1");
    }

    @Test
    public void getObjectTest() {
        given(sseNotificationRepository.findAll()).willReturn(sseNotificationList);
        given(sseNotificationMapper.mapEntitiesForList(ArgumentMatchers.any(List.class))).willReturn(List.of(sseNotificationDTO));
        List<SseNotificationDTO> list = sseNotificationTemplateService.getObject();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getTitle()).isEqualTo("dummyTitleDTO");
    }


    @Test
    public void getObjectByIdTest() {
        given(sseNotificationRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(sseNotification));
        given(sseNotificationMapper.map(ArgumentMatchers.any(SseNotification.class))).willReturn(sseNotificationDTO);
        SseNotificationDTO dto = sseNotificationTemplateService.getObject("1");
        assertThat(dto).isNotNull();
        assertThat(dto.getId().equals("1"));
    }

    @Test
    public void getObjectByIdWhenEmptyTest() {
        given(sseNotificationRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            sseNotificationTemplateService.getObject("1");
        });

        String expectedMessage = "SseNotificationTemplate Does Not Exist";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void postObjectTest() {
        given(sseNotificationMapper.map(ArgumentMatchers.any(SseNotificationDTO.class))).willReturn(sseNotification);
        given(sseNotificationRepository.save(ArgumentMatchers.any(SseNotification.class))).willReturn(sseNotification);
        given(sseNotificationMapper.map(ArgumentMatchers.any(SseNotification.class))).willReturn(sseNotificationDTO);
        SseNotificationDTO dto = sseNotificationTemplateService.postObject(sseNotificationDTO);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo("1");
    }


    @Test
    public void getDeleteByIdWhenEmptyTest() {
        given(sseNotificationRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        Exception exception = assertThrows(DoesNotExistException.class, () -> {
            sseNotificationTemplateService.deleteObject("1");
        });
        String expectedMessage = "SseNotificationTemplate Does Not Exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

}
