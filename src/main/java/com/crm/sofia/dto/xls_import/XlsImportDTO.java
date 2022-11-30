package com.crm.sofia.dto.xls_import;

import com.crm.sofia.dto.access_control.AccessControlDTO;
import com.crm.sofia.dto.common.BaseDTO;
import com.crm.sofia.dto.component.ComponentDTO;
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
public class XlsImportDTO extends BaseDTO {

    private String code;

    private String name;

    private String description;

    private String icon;

    private Long firstLine;

    private String xlsIterationColumn;

    private ComponentDTO component;

    private Boolean accessControlEnabled;

    private List<AccessControlDTO> accessControls;

    public XlsImportDTO(String id, String name, String description, Instant createdOn) {
        this.setId(id);
        this.name = name;
        this.description = description;
        this.setCreatedOn(createdOn);
    }
}
