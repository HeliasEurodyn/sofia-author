package com.crm.sofia.repository.timeline;

import com.crm.sofia.dto.timeline.TimelineDTO;
import com.crm.sofia.model.timeline.Timeline;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimelineRepository extends BaseRepository<Timeline> {
    @Query("SELECT new com.crm.sofia.dto.timeline.TimelineDTO(t.id,t.title,t.modifiedOn) FROM Timeline t ORDER BY t.modifiedOn DESC")
    List<TimelineDTO> getObject();
}
