package com.crm.sofia.dto.rule;

import com.crm.sofia.dto.common.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class RuleSettingsDTO extends BaseDTO {

    private String name;

    private String title;

    private String description;

    private String ruleSectionTitle;

    private String ruleSectionDescription;

    private String fieldCommand;

    private String operatorCommand;

    private List<RuleSettingsQueryDTO> queryList;

    public RuleSettingsDTO(String id, String name, Instant modifiedOn) {
        this.setId(id);
        this.name = name;
        this.setModifiedOn(modifiedOn);
    }

}