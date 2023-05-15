package com.crm.sofia.repository.persistEntity;

import com.crm.sofia.dto.appview.AppViewDTO;
import com.crm.sofia.dto.list.ListDTO;
import com.crm.sofia.dto.table.TableDTO;
import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.dto.view.ViewDTO;
import com.crm.sofia.model.persistEntity.PersistEntity;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT DISTINCT new com.crm.sofia.dto.table.TableDTO(p.id, p.name, p.modifiedOn) " +
            "FROM PersistEntity p " +
            "INNER JOIN p.tags t " +
            "WHERE t.title = :tag " +
            "AND p.entitytype ='Table' "+
            "ORDER BY p.modifiedOn DESC")
    List<TableDTO> getObjectByTagTable(@Param("tag") String tag);

    @Query("SELECT DISTINCT new com.crm.sofia.dto.view.ViewDTO(p.id, p.name, p.modifiedOn) " +
            "FROM PersistEntity p " +
            "INNER JOIN p.tags t " +
            "WHERE t.title = :tag " +
            "AND p.entitytype ='View' "+
            "ORDER BY p.modifiedOn DESC")
    List<ViewDTO> getObjectByTagView(@Param("tag") String tag);

    @Query("SELECT DISTINCT new com.crm.sofia.dto.appview.AppViewDTO(p.id, p.name, p.modifiedOn) " +
            "FROM PersistEntity p " +
            "INNER JOIN p.tags t " +
            "WHERE t.title = :tag " +
            "AND p.entitytype ='AppView' "+
            "ORDER BY p.modifiedOn DESC")
    List<AppViewDTO> getObjectByTagAppView(@Param("tag") String tag);

    @Query(" SELECT DISTINCT new com.crm.sofia.dto.tag.TagDTO(t.title, t.color) " +
            " FROM PersistEntity p  " +
            " INNER JOIN p.tags t " +
            " WHERE p.entitytype = :entityType ")
    List<TagDTO> findTagDistinct(String entityType);



}

