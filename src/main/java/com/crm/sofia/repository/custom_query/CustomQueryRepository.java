package com.crm.sofia.repository.custom_query;

import com.crm.sofia.dto.custom_query.CustomQueryDTO;
import com.crm.sofia.model.custom_query.CustomQuery;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CustomQueryRepository extends BaseRepository<CustomQuery> {
    @Query("SELECT new com.crm.sofia.dto.custom_query.CustomQueryDTO(c.id,c.code,c.name,c.query) FROM CustomQuery c ORDER BY c.modifiedOn DESC")
    List<CustomQueryDTO> getObject();
}
