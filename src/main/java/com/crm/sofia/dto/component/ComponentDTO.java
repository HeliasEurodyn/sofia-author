package com.crm.sofia.dto.component;

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
public class ComponentDTO extends BaseDTO {

    private String name;

    private String description;

    private List<ComponentPersistEntityDTO> componentPersistEntityList;

    private Boolean accessControlEnabled;

    private List<AccessControlDTO> accessControls;

    private String tag;

    private List<TagDTO> tags;

    public ComponentDTO(String id, String name, Instant modifiedOn) {
        this.setId(id);
        this.name = name;
        this.setModifiedOn(modifiedOn);
    }
}
