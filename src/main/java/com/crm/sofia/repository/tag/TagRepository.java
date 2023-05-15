package com.crm.sofia.repository.tag;

import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.model.tag.Tag;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends BaseRepository<Tag> {
    @Query("SELECT new com.crm.sofia.dto.tag.TagDTO(t.id,t.title,t.modifiedOn,t.color) FROM Tag t ORDER BY t.modifiedOn DESC")
    List<TagDTO> getObject();
}
