package com.crm.sofia.dto.form;

import com.crm.sofia.dto.common.BaseDTO;
import com.crm.sofia.dto.component.ComponentPersistEntityDTO;
import com.crm.sofia.dto.component.ComponentPersistEntityFieldDTO;
import com.crm.sofia.dto.form.translation.FormControlFieldTranslationDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class FormControlFieldDTO extends BaseDTO {

    private String editor;
    private String description;
    private String message;
    private String placeholder;
    private Boolean visible;
    private Boolean editable;
    private Boolean required;
    private Boolean headerFilter;
    private String css;
    private ComponentPersistEntityDTO componentPersistEntity;
    private ComponentPersistEntityFieldDTO componentPersistEntityField;
    private String mask;

    private List<FormControlFieldTranslationDTO> translations;
}
