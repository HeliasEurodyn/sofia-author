package com.crm.sofia.repository.component;

import com.crm.sofia.dto.component.ComponentDTO;
import com.crm.sofia.dto.list.ListDTO;
import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.model.component.Component;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponentRepository extends BaseRepository<Component> {

    @Query("SELECT new com.crm.sofia.dto.component.ComponentDTO(c.id,c.name,c.modifiedOn) FROM Component c ORDER BY c.modifiedOn DESC")
    List<ComponentDTO> getObject();

    @Query(" SELECT DISTINCT new com.crm.sofia.dto.tag.TagDTO(ct.title, ct.color) " +
            " FROM Component c  " +
            " INNER JOIN c.tags ct ")
    List<TagDTO> findTagDistinct();

    @Query("SELECT DISTINCT new com.crm.sofia.dto.component.ComponentDTO(c.id, c.name, c.modifiedOn) " +
            "FROM Component c " +
            "INNER JOIN c.tags ct " +
            "WHERE ct.title = :tag " +
            "ORDER BY c.modifiedOn DESC")
    List<ComponentDTO> getObjectByTag(@Param("tag") String tag);
}
