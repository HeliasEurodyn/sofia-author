package com.crm.sofia.services.sse_notification;

import com.crm.sofia.dto.sse_notification.SseNotificationDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.sse_notification.SseNotificationMapper;
import com.crm.sofia.model.sse_notification.SseNotification;
import com.crm.sofia.repository.sse_notification.SseNotificationRepository;
import com.crm.sofia.services.auth.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

@Service
public class SseNotificationTemplateService {


    @Autowired
    private SseNotificationMapper sseNotificationMapper;
    @Autowired
    private SseNotificationRepository sseNotificationRepository;
    @Autowired
    private JWTService jwtService;


    public List<SseNotificationDTO> getObject() {
        List<SseNotification> entities = sseNotificationRepository.findAll();
        return sseNotificationMapper.mapEntitiesForList(entities);
    }

    public SseNotificationDTO getObject(String id) {
        SseNotification optionalEntity = sseNotificationRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("SseNotificationTemplate Does Not Exist"));

        SseNotificationDTO dto = sseNotificationMapper.map(optionalEntity);

        String encodedQuery = Base64.getEncoder().encodeToString(dto.getQuery().getBytes(StandardCharsets.UTF_8));
        dto.setQuery(encodedQuery);

        return dto;
    }


    public SseNotificationDTO postObject(SseNotificationDTO sseNotificationDTO) {

        byte[] decodedQuery = Base64.getDecoder().decode(sseNotificationDTO.getQuery());
        sseNotificationDTO.setQuery(new String(decodedQuery));

        SseNotification sseNotification = sseNotificationMapper.map(sseNotificationDTO);
        if (sseNotification.getId() == null) {
            sseNotification.setCreatedOn(Instant.now());
            sseNotification.setCreatedBy(jwtService.getUserId());
        }
        sseNotification.setModifiedOn(Instant.now());
        sseNotification.setModifiedBy(jwtService.getUserId());
        SseNotification savedData = sseNotificationRepository.save(sseNotification);

        return sseNotificationMapper.map(savedData);
    }

    public void deleteObject(String id) {
        SseNotification optionalEntity = sseNotificationRepository.findById(id)
                        .orElseThrow(() -> new DoesNotExistException("SseNotificationTemplate Does Not Exist"));

        sseNotificationRepository.deleteById(optionalEntity.getId());
    }

}
