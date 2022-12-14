package com.crm.sofia.repository.info_card;

import com.crm.sofia.dto.info_card.InfoCardDTO;
import com.crm.sofia.model.info_card.InfoCard;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfoCardRepository extends BaseRepository<InfoCard> {

    @Query(" SELECT DISTINCT l.id FROM InfoCard l ")
    public List<String> getListIds();

    @Query(" SELECT DISTINCT l.script FROM InfoCard l " +
            " WHERE l.id =:id ")
    public String getListScript(@Param("id") String id);

    @Query(" SELECT DISTINCT l.scriptMin FROM InfoCard l " +
            " WHERE l.id =:id ")
    String getListMinScript(String id);

    @Modifying
    @Query(value = "UPDATE InfoCard SET script = :script , scriptMin = :scriptMin  WHERE id = :id")
    void updateScripts(@Param("id") String id, @Param("script") String script , @Param("scriptMin") String scriptMin);
    @Query("SELECT new com.crm.sofia.dto.info_card.InfoCardDTO(i.id,i.title,i.modifiedOn) FROM InfoCard i ORDER BY i.modifiedOn DESC")
    List<InfoCardDTO> getObject();


}
