package com.crm.sofia.dto.persistEntity;

import com.crm.sofia.dto.common.BaseDTO;
import com.crm.sofia.dto.tag.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PersistEntityDTO extends BaseDTO {

    private String name;

    private String description;

    private String entitytype;

    private String query;

    private Integer creationVersion;

    private String indexes;

    private List<PersistEntityFieldDTO> persistEntityFieldList;

}


