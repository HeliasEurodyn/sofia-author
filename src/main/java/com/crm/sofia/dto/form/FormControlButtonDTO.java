package com.crm.sofia.dto.form;

import com.crm.sofia.dto.common.BaseDTO;
import com.crm.sofia.dto.form.translation.FormControlButtonTranslationDTO;
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
public class FormControlButtonDTO extends BaseDTO {
    private String code;
    private String icon;
    private String description;
    private String editor;
    private  Boolean visible;
    private String cssClass;
    private List<FormControlButtonTranslationDTO> translations;
}
