package com.crm.sofia.repository.custom_query;

import com.crm.sofia.dto.custom_query.CustomQueryDTO;
import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.model.custom_query.CustomQuery;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomQueryRepository extends BaseRepository<CustomQuery> {
    @Query("SELECT new com.crm.sofia.dto.custom_query.CustomQueryDTO(c.id,c.code,c.name,c.query,c.modifiedOn) FROM CustomQuery c ORDER BY c.modifiedOn DESC")
    List<CustomQueryDTO> getObject();

    @Query(" SELECT DISTINCT new com.crm.sofia.dto.tag.TagDTO(ct.title, ct.color) " +
            " FROM CustomQuery c  " +
            " INNER JOIN c.tags ct ")
    List<TagDTO> findTagDistinct();

    @Query("SELECT DISTINCT new com.crm.sofia.dto.custom_query.CustomQueryDTO(c.id,c.code,c.name,c.query,c.modifiedOn) " +
            "FROM CustomQuery c " +
            "INNER JOIN c.tags ct " +
            "WHERE ct.title = :tag " +
            "ORDER BY c.modifiedOn DESC")
    List<CustomQueryDTO> getObjectByTag(@Param("tag") String tag);
}
