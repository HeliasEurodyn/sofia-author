package com.crm.sofia.dto.table;

import com.crm.sofia.dto.common.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class TableDTO extends BaseDTO {

    private String name;

    private String description;

    private String query;

    private Integer creationVersion;

    private String indexes;

    private List<TableFieldDTO> tableFieldList;

    private String entitytype;

    public TableDTO(String id,String name,Instant modifiedOn) {
        this.setId(id);
        this.name = name;
        this.setModifiedOn(modifiedOn);


    }
}
