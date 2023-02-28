package com.crm.sofia.services.timeline;

import com.crm.sofia.dto.timeline.TimelineDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.timeline.TimelineMapper;
import com.crm.sofia.model.timeline.Timeline;
import com.crm.sofia.repository.timeline.TimelineRepository;
import com.crm.sofia.services.auth.JWTService;
import com.crm.sofia.utils.EncodingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

@Service
public class TimelineDesignerService {


    @Autowired
    private TimelineMapper timelineMapper;
    @Autowired
    private TimelineRepository timelineRepository;
    @Autowired
    private JWTService jwtService;


    public List<TimelineDTO> getObject() {
        List<TimelineDTO> timelineList = timelineRepository.getObject();
        return timelineList;
    }

    public TimelineDTO getObject(String id) {
        Timeline optionalEntity = timelineRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Timeline Does Not Exist"));

        TimelineDTO dto = timelineMapper.map(optionalEntity);

        if (dto.getQuery() != null) {
            String uriEncoded = EncodingUtil.encodeURIComponent(dto.getQuery());
            String encodedQuery = Base64.getEncoder().encodeToString(uriEncoded.getBytes(StandardCharsets.UTF_8));
            dto.setQuery(encodedQuery);
        }
        return dto;

    }


    public TimelineDTO postObject(TimelineDTO timelineDTO) {

        if (timelineDTO.getQuery() != null) {
            byte[] decodedQuery = Base64.getDecoder().decode(timelineDTO.getQuery());
            String Query = EncodingUtil.decodeURIComponent(new String(decodedQuery));
            timelineDTO.setQuery(Query);
        }

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
