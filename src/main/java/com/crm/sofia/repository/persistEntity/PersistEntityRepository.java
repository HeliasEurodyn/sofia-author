package com.crm.sofia.repository.persistEntity;

import com.crm.sofia.dto.appview.AppViewDTO;
import com.crm.sofia.dto.table.TableDTO;
import com.crm.sofia.dto.view.ViewDTO;
import com.crm.sofia.model.persistEntity.PersistEntity;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersistEntityRepository extends BaseRepository<PersistEntity> {

    List<PersistEntity> findByEntitytypeOrderByModifiedOn(String entityType);
    @Query("SELECT new com.crm.sofia.dto.table.TableDTO(t.id,t.name,t.modifiedOn) FROM PersistEntity t WHERE t.entitytype =:entityType  ORDER BY t.modifiedOn DESC")
    List<TableDTO> getObjectTable(String entityType);
    @Query("SELECT new com.crm.sofia.dto.appview.AppViewDTO(a.id,a.name,a.modifiedOn) FROM PersistEntity a WHERE a.entitytype =:entityType  ORDER BY a.modifiedOn DESC")
    List<AppViewDTO> getObjectAppView(String entityType);
    @Query("SELECT new com.crm.sofia.dto.view.ViewDTO(v.id,v.name,v.modifiedOn) FROM PersistEntity v WHERE v.entitytype =:entityType  ORDER BY v.modifiedOn DESC")
    List<ViewDTO> getObjectView(String entityType);

}

