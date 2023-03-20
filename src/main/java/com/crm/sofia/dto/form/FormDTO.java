package com.crm.sofia.dto.form;

import com.crm.sofia.dto.access_control.AccessControlDTO;
import com.crm.sofia.dto.common.BaseDTO;
import com.crm.sofia.dto.component.ComponentDTO;
import com.crm.sofia.dto.form.translation.FormTranslationDTO;
import com.crm.sofia.dto.tag.TagDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class FormDTO extends BaseDTO {

    private String name;

    private String title;

    private String icon;

    private String description;

    private Boolean accessControlEnabled;

    private String tag;

    private List<TagDTO> tags;

    private ComponentDTO component;

    private List<FormTabDTO> formTabs;

    private List<FormPopupDto> formPopups;

    private List<FormScriptDTO> formScripts;

    private List<FormCssDTO> formCssList;

    private String jsonUrl;

    private List<FormActionButtonDTO> formActionButtons;

    private Long instanceVersion;

    private List<AccessControlDTO> accessControls;

    private List<FormTranslationDTO> translations;

    public FormDTO(String id, String name, Instant modifiedOn,String jsonUrl,String componentId,String componentName) {
        this.setId(id);
        this.name = name;
        this.setModifiedOn(modifiedOn);
        this.setJsonUrl(jsonUrl);
        ComponentDTO componentDTO = new ComponentDTO();
        componentDTO.setId(componentId);
        componentDTO.setName(componentName);
        this.setComponent(componentDTO);

    }
}
