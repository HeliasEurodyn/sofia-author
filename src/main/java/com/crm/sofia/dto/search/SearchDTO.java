package com.crm.sofia.dto.search;

import com.crm.sofia.dto.access_control.AccessControlDTO;
import com.crm.sofia.dto.common.BaseDTO;
import com.crm.sofia.dto.tag.TagDTO;
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
public class SearchDTO extends BaseDTO {

    private String name;

    private String query;

    private Boolean accessControlEnabled;

    private List<AccessControlDTO> accessControls;

    private List<TagDTO> tags;

    public SearchDTO(String id, String name, Instant modifiedOn) {
        this.setId(id);
        this.name = name;
        this.setModifiedOn(modifiedOn);
    }
}
