package com.crm.sofia.services.timeline;

import com.crm.sofia.dto.timeline.TimelineDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.timeline.TimelineMapper;
import com.crm.sofia.model.timeline.Timeline;
import com.crm.sofia.repository.timeline.TimelineRepository;
import com.crm.sofia.services.auth.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class TimelineDesignerService {


    @Autowired
    private TimelineMapper timelineMapper;
    @Autowired
    private TimelineRepository timelineRepository;
    @Autowired
    private JWTService jwtService;


    public List<TimelineDTO> getObject() {
        List<Timeline> entities = timelineRepository.findAll();
        return timelineMapper.mapEntitiesForList(entities);
    }

    public TimelineDTO getObject(String id) {
        Timeline optionalEntity = timelineRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Timeline Does Not Exist"));

        TimelineDTO dto = timelineMapper.map(optionalEntity);

        String encodedQuery = Base64.getEncoder().encodeToString(dto.getQuery().getBytes(StandardCharsets.UTF_8));
        dto.setQuery(encodedQuery);

        return dto;
    }


    public TimelineDTO postObject(TimelineDTO timelineDTO) {

        byte[] decodedQuery = Base64.getDecoder().decode(timelineDTO.getQuery());
        timelineDTO.setQuery(new String(decodedQuery));

        Timeline timeline = timelineMapper.map(timelineDTO);
        if (timeline.getId() == null) {
            timeline.setCreatedOn(Instant.now());
            timeline.setCreatedBy(jwtService.getUserId());
        }
        timeline.setModifiedOn(Instant.now());
        timeline.setModifiedBy(jwtService.getUserId());
        Timeline savedData = timelineRepository.save(timeline);

        return timelineMapper.map(savedData);
    }

    public void deleteObject(String id) {
       Timeline optionalEntity = timelineRepository.findById(id)
                       .orElseThrow(() -> new DoesNotExistException("Timeline Does Not Exist"));

        timelineRepository.deleteById(optionalEntity.getId());
    }



}
