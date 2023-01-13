package com.crm.sofia.dto.html_template;

import com.crm.sofia.dto.common.BaseDTO;
import com.crm.sofia.dto.component.ComponentDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)

public class HtmlTemplateDTO extends BaseDTO {
    @NotNull(message = "Title Cannot Be Null")
    @NotBlank(message = "Title Cannot Be Blank")
    private String title;

    private String description;

    private ComponentDTO component;

    @NotNull
    private String html;

    public HtmlTemplateDTO(String id,String title,Instant modifiedOn,String componentId,String componentName ){
        this.setId(id);
        this.setTitle(title);
        this.setModifiedOn(modifiedOn);
        ComponentDTO componentDTO = new ComponentDTO();
        componentDTO.setId(componentId);
        componentDTO.setName(componentName);
        this.setComponent(componentDTO);

    }
}
