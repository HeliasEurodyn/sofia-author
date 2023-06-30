package com.crm.sofia.repository.list;

import com.crm.sofia.dto.list.ListDTO;
import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.model.list.ListEntity;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ListRepository extends BaseRepository<ListEntity> {

    List<ListEntity> findAllByOrderByModifiedOn();

    @Query(" SELECT DISTINCT new com.crm.sofia.dto.tag.TagDTO(lt.title, lt.color) " +
            " FROM ListEntity l  " +
            " INNER JOIN l.tags lt ")
    List<TagDTO> findTagDistinct();

    ListEntity findFirstByName(String name);

    @Query(" SELECT DISTINCT l.instanceVersion FROM ListEntity l " + " WHERE l.id =:id ")
    String getInstanceVersion(@Param("id") String id);

    @Query(" SELECT l.id FROM ListEntity l " + " WHERE l.jsonUrl =:jsonUrl ")
    List<String> getIdByJsonUrl(@Param("jsonUrl") String jsonUrl);

    @Modifying
    @Transactional
    @Query(value = "UPDATE list SET instance_version = instance_version + 1", nativeQuery = true)
    void increaseInstanceVersions();

    @Query(" SELECT DISTINCT ls.script FROM ListEntity l " + " INNER JOIN l.listScripts ls " + " WHERE l.id =:id ")
    List<String> getJavaScriptsById(@Param("id") String id);

    @Query(" SELECT DISTINCT l.script FROM ListEntity l " + " WHERE l.id =:id ")
    String getListScript(@Param("id") String id);

    @Query(" SELECT DISTINCT l.scriptMin FROM ListEntity l " + " WHERE l.id =:id ")
    String getListMinScript(String id);

    @Query(" SELECT DISTINCT ls.script FROM ListEntity l " + " INNER JOIN l.listCssList ls " + " WHERE l.id =:id ")
    List<String> getCssScriptsById(@Param("id") String id);


    @Query(" SELECT DISTINCT l.id FROM ListEntity l ")
    List<String> getListIds();

    @Modifying
    @Query(value = "UPDATE ListEntity SET script = :script , scriptMin = :scriptMin  WHERE id = :id")
    void updateScripts(@Param("id") String id, @Param("script") String script, @Param("scriptMin") String scriptMin);

    @Query(" SELECT l FROM ListEntity l " + " WHERE l.jsonUrl <> '' AND l.jsonUrl is not null ")
    List<ListEntity> getIdsByExistingJsonUrls();

    @Query("SELECT new com.crm.sofia.dto.list.ListDTO(l.id, l.name, l.modifiedOn, l.jsonUrl, c.id, c.name) FROM ListEntity l INNER JOIN l.component c ORDER BY l.modifiedOn DESC")
    List<ListDTO> getObject();

    @Query("SELECT new com.crm.sofia.dto.list.ListDTO(l.id, l.name, l.modifiedOn, l.jsonUrl, c.id, c.name) FROM ListEntity l INNER JOIN l.component c ORDER BY l.modifiedOn DESC")
    List<ListDTO> get10LatestObject(Pageable pageable);

    @Query("SELECT DISTINCT new com.crm.sofia.dto.list.ListDTO(l.id, l.name, l.modifiedOn, l.jsonUrl, c.id, c.name) " +
            "FROM ListEntity l " +
            "INNER JOIN l.component c " +
            "INNER JOIN l.tags lt " +
            "WHERE lt.title = :tag " +
            "ORDER BY l.modifiedOn DESC")
    List<ListDTO> getObjectByTag(@Param("tag") String tag);


}
