package com.crm.sofia.dto.list;

import com.crm.sofia.dto.common.BaseDTO;
import com.crm.sofia.dto.component.ComponentPersistEntityDTO;
import com.crm.sofia.dto.component.ComponentPersistEntityFieldDTO;
import com.crm.sofia.dto.list.translation.ListComponentFieldTranslationDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class ListComponentFieldDTO extends BaseDTO {
    private String code;
    private String editor;
    private String description;
    private String type;
    private String mask;
    private ComponentPersistEntityDTO componentPersistEntity;
    private ComponentPersistEntityFieldDTO componentPersistEntityField;
    private Boolean visible;
    private Boolean editable;
    private Boolean headerEditable;
    private String editableRelFieldCode;
    private Boolean headerFilter;
    private Boolean required ;
    private String defaultValue;
    private Integer decimals;
    private String fieldtype;
    private String shortLocation;
    private String operator;
    private String bclass;
    private String css;
    private Object fieldValue;
    private String formulaType;
    private List<ListComponentFieldDTO> listComponentActionFieldList;
    private List<ListComponentFieldTranslationDTO> translations;
}
