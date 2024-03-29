package com.crm.sofia.dto.form;

import com.crm.sofia.dto.common.BaseDTO;
import com.crm.sofia.dto.form.translation.FormTabTranslationDTO;
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
public class FormPopupDto extends BaseDTO {

    private String code;

    private String description;

    private String icon;

    private Boolean editable;

    private List<FormAreaDTO> formAreas;

    private List<FormTabTranslationDTO> translations;

}
