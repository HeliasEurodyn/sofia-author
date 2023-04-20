package com.crm.sofia.repository.rule;

import com.crm.sofia.dto.rule.RuleSettingsDTO;
import com.crm.sofia.model.rule.RuleSettings;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleSettingsRepository extends BaseRepository<RuleSettings> {

    @Query("SELECT new com.crm.sofia.dto.rule.RuleSettingsDTO(r.id, r.name, r.modifiedOn) FROM RuleSettings r ORDER BY r.modifiedOn DESC")
    List<RuleSettingsDTO> getObject();

}
