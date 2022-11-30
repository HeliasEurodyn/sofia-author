package com.crm.sofia.dto.html_dashboard;

import com.crm.sofia.dto.common.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class HtmlDashboardDTO extends BaseDTO {

    private String code;

    private String name;

    private String html;

    public HtmlDashboardDTO(String id,String code, String name) {
        this.setId(id);
        this.code = code;
        this.name = name;
    }
}
