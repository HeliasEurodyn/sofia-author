package com.crm.sofia.repository.rule;

import com.crm.sofia.dto.rule.RuleFieldDTO;
import com.crm.sofia.dto.rule.RuleSettingsDTO;
import com.crm.sofia.model.rule.RuleField;
import com.crm.sofia.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface RuleFieldRepository extends BaseRepository<RuleField> {

    @Query("SELECT new com.crm.sofia.dto.rule.RuleFieldDTO(r.id, r.code, r.name, r.modifiedOn) FROM RuleField r ORDER BY r.modifiedOn DESC")
    List<RuleFieldDTO> getObject();

}
