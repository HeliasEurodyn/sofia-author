package com.crm.sofia.repository.component;

import com.crm.sofia.dto.component.ComponentDTO;
import com.crm.sofia.model.component.Component;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponentRepository extends BaseRepository<Component> {

    @Query("SELECT new com.crm.sofia.dto.component.ComponentDTO(c.id,c.name,c.modifiedOn) FROM Component c ORDER BY c.modifiedOn DESC")
    List<ComponentDTO> getObject();
}
