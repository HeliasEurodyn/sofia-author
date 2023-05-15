package com.crm.sofia.dto.appview;

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
public class AppViewDTO extends BaseDTO {

    private String name;

    private String description;

    private String query;

    private Integer creationVersion;

    private String indexes;

    private List<AppViewFieldDTO> appViewFieldList;

    private String entitytype;

    private List<TagDTO> tags;

    public AppViewDTO(String id, String name, Instant modifiedOn) {
        this.setId(id);
        this.name = name;
        this.setModifiedOn(modifiedOn);

    }
}
